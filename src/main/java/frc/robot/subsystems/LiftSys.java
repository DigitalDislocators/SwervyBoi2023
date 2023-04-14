package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANDevices;
import frc.robot.Constants.LiftConstants;
import frc.robot.Constants.PneumaticChannels;

public class LiftSys extends SubsystemBase {

    private final CANSparkMax masterMtr;
    private final CANSparkMax slaveMtr;
 
    private final RelativeEncoder liftEnc;
 
    private final SparkMaxPIDController controller;
 
    private final DoubleSolenoid liftSol;
 
    private double targetInches = 0.0;

    private boolean isManual = false;

    private boolean isArticulationOverride = false;
    public boolean isArticulationOverride() {
        return isArticulationOverride;
    }
    public void setArticulationOverride(boolean isArticulationOverride) {
        this.isArticulationOverride = isArticulationOverride;
    }

    /**
     * Constructs a new LiftSys.
     * 
     * <p>LiftSys contains methods for actuation and articulation of the lift.
     * <p>There are no methods for articulating the lift, since it would be possible to extend taller than the max height.
     * Instead, the lift is articulated soley based on its height.
     */
    public LiftSys() {
        masterMtr = new CANSparkMax(CANDevices.masterMtrId, MotorType.kBrushless);
        slaveMtr = new CANSparkMax(CANDevices.slaveMtrId, MotorType.kBrushless);

        masterMtr.setInverted(true);
        slaveMtr.setInverted(false);

        masterMtr.setSmartCurrentLimit(LiftConstants.maxCurrentAmps);

        masterMtr.setIdleMode(IdleMode.kBrake);

        slaveMtr.follow(masterMtr, true);

        liftEnc = masterMtr.getEncoder();

        liftEnc.setPosition(0);

        liftEnc.setPositionConversionFactor(LiftConstants.inchesPerEncRev);
        liftEnc.setVelocityConversionFactor(LiftConstants.feetPerSecondPerRPM);

        controller = masterMtr.getPIDController();

        controller.setP(LiftConstants.kP);
        controller.setD(LiftConstants.kD);
        
        controller.setIZone(0);
        
        liftSol = new DoubleSolenoid(CANDevices.pneumaticHubId, PneumaticsModuleType.REVPH, PneumaticChannels.liftSolChs[0], PneumaticChannels.liftSolChs[1]);
    }

    @Override
    public void periodic() {
        if(isManual) {
            controller.setOutputRange(-LiftConstants.manualPower, LiftConstants.manualPower);
        }
        else {
            controller.setReference(targetInches, ControlType.kPosition);
        }

        if(
            ((isManual && liftEnc.getPosition() < LiftConstants.upActuationHeightInches && masterMtr.get() < 0) ||
            (!isManual && targetInches < LiftConstants.upActuationHeightInches)) ||
            isArticulationOverride
        )
            actuateUp();
        else if(
            ((isManual && liftEnc.getPosition() > LiftConstants.downActuationHeightInches && masterMtr.get() > 0) ||
            (!isManual && targetInches > LiftConstants.downActuationHeightInches)) &&
            !isArticulationOverride
        )
            actuateDown();

        if(targetInches < 0.0) targetInches = 0.0;
        else if(!isArticulationOverride && targetInches > LiftConstants.maxHeightInches) targetInches = LiftConstants.maxHeightInches;
        else if(isArticulationOverride && targetInches > LiftConstants.maxUnarticulatedHeightInches) targetInches = LiftConstants.maxUnarticulatedHeightInches;
    }

    public double getCurrentPosition() {
        return liftEnc.getPosition();
    }
        // Carl was here; 
    public void setPower(double power) {
        if(
            (liftEnc.getPosition() <= LiftConstants.manualControlPaddingInches && power < 0.0) ||
            (liftEnc.getPosition() >= (isArticulationOverride ? LiftConstants.maxUnarticulatedHeightInches : LiftConstants.maxHeightInches) - LiftConstants.manualControlPaddingInches && power > 0.0)
        ) {
            masterMtr.set(0.0);
        }
        else {
            masterMtr.set(power);
        }
    }

    public void setTarget(double inches, double power) {
        isManual = false;

        if(inches > LiftConstants.maxHeightInches) inches = LiftConstants.maxHeightInches;
        else if(inches < 0.0) inches = 0.0;

        controller.setOutputRange(-power, power);

        targetInches = inches;
    }

    public void manualControl(double manual) {
        if(manual != 0.0) {
            isManual = true;
            setPower(manual * LiftConstants.manualPower);
        }
        else {
            if(isManual)
                targetInches = liftEnc.getPosition();

            isManual = false;
        }
    }

    public double getTargetInches() {
        return targetInches;
    }

    public boolean isAtTarget() {
        return Math.abs(getCurrentPosition() - targetInches) < LiftConstants.targetToleranceInches;
    }

    public void actuateUp() {
        liftSol.set(Value.kReverse);
    }

    public void actuateDown() {
        liftSol.set(Value.kForward);
    }

    public boolean isArticulatedDown() {
        return liftSol.get().equals(Value.kForward);
    }

    public boolean isManual() {
        return isManual;
    }
}
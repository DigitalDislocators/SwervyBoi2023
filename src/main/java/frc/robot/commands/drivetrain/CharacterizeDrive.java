package frc.robot.commands.drivetrain;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SwerveSys;

public class CharacterizeDrive extends CommandBase {

    /**
     * Runs along with the FRC Characterziation Tool to generate feedforward gains for the drivetrain
     */

    private final SwerveSys drive;

    private final NetworkTableEntry autoSpeedEntry = NetworkTableInstance.getDefault().getEntry("/robot/autospeed");
    private final NetworkTableEntry telemetryEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry");
    
    private final double[] telemetryArray = new double[6];

    private double priorAutoSpeed = 0;

    public CharacterizeDrive(SwerveSys subsystem) {

        drive = subsystem;

        addRequirements(drive);

    }

    @Override
    public void initialize() {

        drive.resetDriveDistances();

        priorAutoSpeed = 0;
    }

    @Override
    public void execute() {

        double now = Timer.getFPGATimestamp();

        double averageDistanceMeters = drive.getAverageDriveDistanceMeters();
        double averageVelocityMetersPerSecond = drive.getAverageDriveVelocityMetersPerSecond();

        double batteryVoltage = RobotController.getBatteryVoltage();
        double motorVoltage = batteryVoltage * Math.abs(priorAutoSpeed);

        double autoSpeedCurrent = autoSpeedEntry.getDouble(0);
        priorAutoSpeed = autoSpeedCurrent;

        drive.drive(autoSpeedCurrent, 0, 0, false, false);

        telemetryArray[0] = now;
        telemetryArray[1] = batteryVoltage;
        telemetryArray[2] = autoSpeedCurrent;
        telemetryArray[3] = motorVoltage;
        telemetryArray[4] = averageDistanceMeters;
        telemetryArray[5] = averageVelocityMetersPerSecond;

        telemetryEntry.setDoubleArray(telemetryArray);

    }

}

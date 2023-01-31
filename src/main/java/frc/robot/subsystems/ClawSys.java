package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PneumaticChannels;

public class ClawSys extends SubsystemBase {

    private DoubleSolenoid clawSols;

    public ClawSys() {
        clawSols = new DoubleSolenoid(PneumaticsModuleType.REVPH, PneumaticChannels.clawSolsCh[0], PneumaticChannels.clawSolsCh[1]);
    
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        
    }
    
}
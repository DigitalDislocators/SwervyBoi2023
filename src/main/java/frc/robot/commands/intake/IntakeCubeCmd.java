package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakeSys;
import frc.robot.subsystems.LightsSys;

public class IntakeCubeCmd extends CommandBase {

    private final IntakeSys intakeSys;
    private final LightsSys lightsSys;

    /**
     * Constructs a new ExampleCmd.
     * 
     * <p>ExampleCmd contains the basic framework of a robot command for use in command-based programming.
     * 
     * <p>The command finishes once the isFinished method returns true.
     * 
     * @param exampleSys The required ExampleSys.
     */
    public IntakeCubeCmd(IntakeSys intakeSys, LightsSys lightsSys) {
        this.intakeSys = intakeSys;
        this.lightsSys = lightsSys;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
<<<<<<< HEAD:src/main/java/frc/robot/commands/intake/IntakeCubeCmd.java
    
        intakeSys.setRelativeSpeed(IntakeConstants.rollerRelativeMetersPerSecond);
        
=======
        if(lightsSys.getStatus().equals(GameElement.kCone)) {
            intakeSys.setAbsoluteSpeed(IntakeConstants.rollerConeRPM);
        }
        else {
            intakeSys.setRelativeSpeed(IntakeConstants.rollerRelativeMetersPerSecond);
        }
>>>>>>> parent of 48d4a61 (game element mode bug fixes and improvements):src/main/java/frc/robot/commands/intake/SetRelativeSpeedCmd.java
        lightsSys.setIntaking(true);
    }
    
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }
    
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }

    // Whether the command should run when robot is disabled.
    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
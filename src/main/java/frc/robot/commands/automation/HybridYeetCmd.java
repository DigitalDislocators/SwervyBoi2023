package frc.robot.commands.automation;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.LiftConstants;
import frc.robot.subsystems.ClawSys;
import frc.robot.subsystems.LiftSys;

public class HybridYeetCmd extends CommandBase {
    
    private final LiftSys liftSys;
    private final ClawSys clawSys;

    private final Timer holdTimer;

    public HybridYeetCmd(LiftSys liftSys, ClawSys clawSys) {
        this.liftSys = liftSys;
        this.clawSys = clawSys;

        holdTimer = new Timer();

        addRequirements(liftSys, clawSys);
    }

    @Override
    public void initialize() {
        if(clawSys.isOpen() || liftSys.getTargetInches() > LiftConstants.hoverInches + 6.0){
            cancel();
        }
        else {
            liftSys.setArticulationOverride(true);
            liftSys.setTarget(LiftConstants.hybridYeetHeightInches + liftSys.getTargetInches(), LiftConstants.hybridYeetPower);
        }
    }

    @Override
    public void execute() {
        if(liftSys.getCurrentPosition() >= LiftConstants.hybridYeetReleaseInches) {
            clawSys.open();
        }

        if(liftSys.isAtTarget() && liftSys.getTargetInches() > LiftConstants.hybridYeetHeightInches) {
            holdTimer.start();
        }
    }

    @Override
    public void end(boolean interrupted) {
        liftSys.setTarget(LiftConstants.downInches, LiftConstants.downPower);
        liftSys.setArticulationOverride(false);

        holdTimer.stop();
        holdTimer.reset();
    }

    @Override
    public boolean isFinished() {
        return holdTimer.hasElapsed(0.5);
    }
}

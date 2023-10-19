package frc.robot.commands.auto.programs;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.WaitCmd;
import frc.robot.commands.WaitUntilCmd;
import frc.robot.commands.auto.FollowTrajectoryCmd;
import frc.robot.commands.automation.YEETCmd;
import frc.robot.commands.claw.CloseCmd;
import frc.robot.commands.claw.OpenCmd;
import frc.robot.commands.drivetrain.SetPoseCmd;
import frc.robot.commands.intake.InCmd;
import frc.robot.commands.intake.OutCmd;
import frc.robot.commands.intake.SetRelativeSpeedCmd;
import frc.robot.commands.intake.StopRollersCmd;
import frc.robot.commands.lift.DownCmd;
import frc.robot.commands.lift.ShelfPickupCmd;
import frc.robot.subsystems.ClawSys;
import frc.robot.subsystems.IntakeSys;
import frc.robot.subsystems.LiftSys;
import frc.robot.subsystems.LightsSys;
import frc.robot.subsystems.SwerveSys;

public class LeftDumpCubeYEETCubeScoreCube extends SequentialCommandGroup {
    
    public LeftDumpCubeYEETCubeScoreCube(SwerveSys swerveSys, LiftSys liftSys, ClawSys clawSys, IntakeSys intakeSys, LightsSys lightsSys) {
        super(
            new SetPoseCmd(new Pose2d(1.83, 4.98, new Rotation2d(0.0)), swerveSys),
            new ShelfPickupCmd(false, liftSys),
            new DownCmd(true, liftSys),
            // new OpenCmd(clawSys),
            // new InCmd(intakeSys),
            new FollowTrajectoryCmd("LeftStartToYEETCube1", swerveSys).alongWith(
                // new WaitUntilCmd(() -> swerveSys.getPose().getX() > 5.75)
                // .andThen(new OutCmd(intakeSys).alongWith(new SetRelativeSpeedCmd(intakeSys, lightsSys)))
                // .andThen(new WaitCmd(1.5))
                // .andThen(new InCmd(intakeSys).alongWith(new StopRollersCmd(intakeSys, lightsSys)))
                // .andThen(new WaitUntilCmd(() -> swerveSys.getPose().getX() < 3.62))
                // .andThen(new CloseCmd(clawSys))
                // .andThen(new WaitCmd(0.5))
                // .andThen(new YEETCmd(liftSys, clawSys))
            ),
            new WaitCmd(1.0),
            new DownCmd(true, liftSys),
            new FollowTrajectoryCmd("LeftYEETCube1ToScoreCube2", swerveSys).alongWith(
                // new WaitUntilCmd(() -> swerveSys.getPose().getX() > 6.0)
                // .andThen(new OutCmd(intakeSys).alongWith(new SetRelativeSpeedCmd(intakeSys, lightsSys)))
                // .andThen(new WaitCmd(1.5))
                // .andThen(new InCmd(intakeSys).alongWith(new StopRollersCmd(intakeSys, lightsSys)))
                // .andThen(new WaitUntilCmd(() -> swerveSys.getPose().getX() < 3.75))
                // .andThen(new CloseCmd(clawSys))
            )
        );
    }
}

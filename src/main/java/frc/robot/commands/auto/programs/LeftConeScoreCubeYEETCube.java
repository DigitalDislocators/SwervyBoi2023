package frc.robot.commands.auto.programs;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.GameElement;
import frc.robot.commands.WaitCmd;
import frc.robot.commands.WaitUntilCmd;
import frc.robot.commands.auto.FollowTrajectoryCmd;
import frc.robot.commands.automation.AutoRow3PoleCmd;
import frc.robot.commands.automation.YEETCmd;
import frc.robot.commands.claw.CloseCmd;
import frc.robot.commands.claw.OpenCmd;
import frc.robot.commands.drivetrain.SetPoseCmd;
import frc.robot.commands.intake.InCmd;
import frc.robot.commands.intake.OutCmd;
import frc.robot.commands.intake.IntakeCubeCmd;
import frc.robot.commands.intake.StopRollersCmd;
import frc.robot.commands.lift.DownCmd;
import frc.robot.commands.lift.Row3Cmd;
import frc.robot.subsystems.ClawSys;
import frc.robot.subsystems.IntakeSys;
import frc.robot.subsystems.LiftSys;
import frc.robot.subsystems.LightsSys;
import frc.robot.subsystems.SwerveSys;

public class LeftConeScoreCubeYEETCube extends SequentialCommandGroup {
    
    public LeftConeScoreCubeYEETCube(SwerveSys swerveSys, LiftSys liftSys, ClawSys clawSys, IntakeSys intakeSys, LightsSys lightsSys) {
        super(
            new SetPoseCmd(new Pose2d(1.83, 4.98, new Rotation2d(Math.PI)), swerveSys),
            // new CloseCmd(clawSys),
            new InCmd(intakeSys),
            new WaitCmd(0.25),
            new AutoRow3PoleCmd(liftSys, clawSys),
            new SetPoseCmd(new Pose2d(1.83, 0.5, new Rotation2d(Math.PI)), swerveSys),
            new FollowTrajectoryCmd("LeftStartToScoreCube1", 3.25, 2.25, swerveSys).alongWith(
                new WaitUntilCmd(() -> swerveSys.getPose().getX() > 5.75)
                .andThen(new OutCmd(intakeSys, lightsSys).alongWith(new IntakeCubeCmd(intakeSys, lightsSys)))
                .andThen(new WaitCmd(1.0))
                .andThen(new InCmd(intakeSys).alongWith(new StopRollersCmd(intakeSys, lightsSys)))
                .andThen(new WaitUntilCmd(() -> swerveSys.getPose().getX() < 3.75))
                .andThen(new CloseCmd(clawSys))
                .andThen(new WaitCmd(0.1))
                .andThen(new Row3Cmd(GameElement.kCube, false, liftSys))
            ),
            new OpenCmd(clawSys),
            new DownCmd(true, liftSys),
            new FollowTrajectoryCmd("LeftScoreCube1ToYEETCube2", 3.25, 2.25, swerveSys).alongWith(
                new WaitUntilCmd(() -> swerveSys.getPose().getX() > 6.0)
                .andThen(new OutCmd(intakeSys, lightsSys).alongWith(new IntakeCubeCmd(intakeSys, lightsSys)))
                .andThen(new WaitCmd(1.0))
                .andThen(new InCmd(intakeSys).alongWith(new StopRollersCmd(intakeSys, lightsSys)))
                .andThen(new WaitUntilCmd(() -> swerveSys.getPose().getX() < 3.75))
                .andThen(new CloseCmd(clawSys))
                .andThen(new WaitUntilCmd(() -> DriverStation.getMatchTime() < 0.75))
                .andThen(new YEETCmd(liftSys, clawSys))
            )
        );
    }
}

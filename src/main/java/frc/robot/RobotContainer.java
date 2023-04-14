package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerType;
import frc.robot.Constants.GameElement;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.auto.programs.CenterConeDock;
import frc.robot.commands.auto.programs.CenterConeGrabCubeDock;
import frc.robot.commands.auto.programs.CenterConeMobilityDock;
import frc.robot.commands.auto.programs.CenterCubeDock;
import frc.robot.commands.auto.programs.CenterCubeMobilityDock;
import frc.robot.commands.auto.programs.Cone;
import frc.robot.commands.auto.programs.Cube;
import frc.robot.commands.auto.programs.LeftConeGrabCubeDock;
import frc.robot.commands.auto.programs.LeftConeScoreCube;
import frc.robot.commands.auto.programs.LeftConeScoreCubeGrabCube;
import frc.robot.commands.auto.programs.RightConeGrabCubeDock;
import frc.robot.commands.auto.programs.RightConeScoreCube;
import frc.robot.commands.auto.programs.RightConeScoreCubeGrabCube;
import frc.robot.commands.automation.AutoAlignCmd;
import frc.robot.commands.automation.HybridYeetCmd;
import frc.robot.commands.automation.SetElementStatusCmd;
import frc.robot.commands.automation.YEETCmd;
import frc.robot.commands.claw.CloseCmd;
import frc.robot.commands.claw.OpenCmd;
import frc.robot.commands.drivetrain.LockCmd;
import frc.robot.commands.drivetrain.ResetHeadingCmd;
import frc.robot.commands.drivetrain.SetHeadingCmd;
import frc.robot.commands.drivetrain.SwerveDriveCmd;
import frc.robot.commands.intake.InCmd;
import frc.robot.commands.intake.OutCmd;
import frc.robot.commands.intake.SetRelativeSpeedCmd;
import frc.robot.commands.intake.StopRollersCmd;
import frc.robot.commands.intake.IntakeManualControlCmd;
import frc.robot.commands.lift.DownCmd;
import frc.robot.commands.lift.HoverCmd;
import frc.robot.commands.lift.LiftManualControlCmd;
import frc.robot.commands.lift.Row1Cmd;
import frc.robot.commands.lift.Row2Cmd;
import frc.robot.commands.lift.Row3Cmd;
import frc.robot.commands.lift.ShelfPickupCmd;
import frc.robot.commands.lights.TogglePartyModeCmd;
import frc.robot.commands.lights.ToggleWeeWooModeCmd;
import frc.robot.commands.vision.RestartLimelightCmd;
import frc.robot.subsystems.ClawSys;
import frc.robot.subsystems.CompressorSys;
import frc.robot.subsystems.IntakeSys;
import frc.robot.subsystems.LiftSys;
import frc.robot.subsystems.LightsSys;
import frc.robot.subsystems.SwerveSys;
import frc.robot.subsystems.VisionSys;

public class RobotContainer {
    
    // Initialize subsystems.
    private final SwerveSys swerveSys = new SwerveSys();
    private final LiftSys liftSys = new LiftSys();
    private final ClawSys clawSys = new ClawSys();
    private final IntakeSys intakeSys = new IntakeSys(() -> swerveSys.getForwardVelocityMetersPerSecond());
    private final VisionSys visionSys = new VisionSys();  
    private final CompressorSys compressorSys = new CompressorSys();  
    private final LightsSys lightsSys = new LightsSys();

    // Initialize joysticks.
    private final XboxController driverController = new XboxController(ControllerConstants.driverGamepadPort);

    private final Joystick driverLeftJoystick = new Joystick(ControllerConstants.driverLeftJoystickPort);
    private final Joystick driverRightJoystick = new Joystick(ControllerConstants.driverRightJoystickPort);

    private final XboxController operatorController = new XboxController(ControllerConstants.operatorGamepadPort);

    private final XboxController hybridController = new XboxController(ControllerConstants.hybridControllerPort);

    // Initialize controller buttons.
    private final JoystickButton driverRightJoystickTriggerBtn = new JoystickButton(driverRightJoystick, 1);
    private final JoystickButton driverRightJoystickThumbBtn = new JoystickButton(driverRightJoystick, 2);

    private final JoystickButton driverABtn = new JoystickButton(driverController, 1);
    // private final JoystickButton driverBBtn = new JoystickButton(driverController, 2);
    private final JoystickButton driverXBtn = new JoystickButton(driverController, 3);
    private final JoystickButton driverYBtn = new JoystickButton(driverController, 4);
    // private final JoystickButton driverLeftBumper = new JoystickButton(driverController, 5);
    // private final JoystickButton driverRightBumper = new JoystickButton(driverController, 6);
    private final JoystickButton driverMenuBtn = new JoystickButton(driverController, 8);
    private final Trigger driverRightTriggerBtn =
        new Trigger(() -> driverController.getRightTriggerAxis() > ControllerConstants.triggerPressedDeadband);
    private final Trigger driverLeftTriggerBtn =
        new Trigger(() -> driverController.getLeftTriggerAxis() > ControllerConstants.triggerPressedDeadband);

    private final JoystickButton operatorABtn = new JoystickButton(operatorController, 1);
    private final JoystickButton operatorBBtn = new JoystickButton(operatorController, 2);
    private final JoystickButton operatorXBtn = new JoystickButton(operatorController, 3);
    private final JoystickButton operatorYBtn = new JoystickButton(operatorController, 4);
    private final JoystickButton operatorLeftBumper = new JoystickButton(operatorController, 5);
    private final JoystickButton operatorRightBumper = new JoystickButton(operatorController, 6);
    private final JoystickButton operatorWindowBtn = new JoystickButton(operatorController, 7);
    private final JoystickButton operatorMenuBtn = new JoystickButton(operatorController, 8);
    private final POVButton operatorUpBtn = new POVButton(operatorController, 0);
    private final POVButton operatorRightBtn = new POVButton(operatorController, 90);
    private final POVButton operatorDownBtn = new POVButton(operatorController, 180);
    private final POVButton operatorLeftBtn = new POVButton(operatorController, 270);

    private final JoystickButton hybridABtn = new JoystickButton(hybridController, 1);
    private final JoystickButton hybridBBtn = new JoystickButton(hybridController, 2);
    private final JoystickButton hybridXBtn = new JoystickButton(hybridController, 3);
    private final JoystickButton hybridYBtn = new JoystickButton(hybridController, 4);
    private final JoystickButton hybridLeftBumper = new JoystickButton(hybridController, 5);
    private final JoystickButton hybridRightBumper = new JoystickButton(hybridController, 6);
    private final JoystickButton hybridWindowBtn = new JoystickButton(hybridController, 7);
    private final JoystickButton hybridMenuBtn = new JoystickButton(driverController, 8);
    private final JoystickButton hybridRightJoystickPressBtn = new JoystickButton(hybridController, 10);

    // Instantiate controller rumble.
    private Rumble matchTimeRumble;
    private Rumble brownOutRumble;
    private Rumble countdown10Rumble;
    private Rumble countdown5Rumble;
    private Rumble targetAlignedRumble;

    // Initialize auto selector.
    SendableChooser<Command> autoSelector = new SendableChooser<Command>();

    public RobotContainer() {
        SmartDashboard.putData("auto selector", autoSelector);

        RestartLimelightCmd restartLimelight = new RestartLimelightCmd(visionSys);
        restartLimelight.setName("RESTART LIMELIGHT");
        SmartDashboard.putData(restartLimelight);

        ParallelRaceGroup runCompressor = new RunCommand(() -> compressorSys.run(), compressorSys).until(() -> !compressorSys.isRunning() || !compressorSys.isEnabled());
        runCompressor.setName("run compressor");
        SmartDashboard.putData(runCompressor);

        ParallelRaceGroup disableCompressor = new RunCommand(() -> compressorSys.disable(), compressorSys).until(() -> !compressorSys.isEnabled());
        disableCompressor.setName("DISABLE COMPRESSOR");
        SmartDashboard.putData(disableCompressor);

        RobotController.setBrownoutVoltage(7.5);

        autoSelector.addOption("Cone", new Cone(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("Cube", new Cube(swerveSys, liftSys, clawSys, intakeSys));
        autoSelector.addOption("CenterConeDock", new CenterConeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("CenterCubeDock", new CenterCubeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("CenterConeMobilityDock", new CenterConeMobilityDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("CenterCubeMobilityDock", new CenterCubeMobilityDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("CenterConeGrabCubeDock", new CenterConeGrabCubeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        // autoSelector.addOption("LeftConeGrabCube", new LeftConeGrabCube(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("LeftConeGrabCubeDock", new LeftConeGrabCubeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("LeftConeScoreCube", new LeftConeScoreCube(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        // autoSelector.addOption("LeftConeScoreCubeDock", new LeftConeScoreCubeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("LeftConeScoreCubeGrabCube", new LeftConeScoreCubeGrabCube(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        // autoSelector.addOption("LeftConeScoreCubeScoreCubeMid", new LeftConeScoreCubeScoreCubeMid(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        // autoSelector.addOption("RightConeGrabCube", new RightConeGrabCube(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("RightConeGrabCubeDock", new RightConeGrabCubeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("RightConeScoreCube", new RightConeScoreCube(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        // autoSelector.addOption("RightConeScoreCubeDock", new RightConeScoreCubeDock(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.addOption("RightConeScoreCubeGrabCube", new RightConeScoreCubeGrabCube(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        // autoSelector.addOption("RightConeScoreCubeScoreCubeMid", new RightConeScoreCubeScoreCubeMid(swerveSys, liftSys, clawSys, intakeSys, lightsSys));
        autoSelector.setDefaultOption("DoNothing", new SetHeadingCmd(new Rotation2d(Math.PI), swerveSys));
    }

    public void configBindings() {
        lightsSys.cancelAnimations();

        brownOutRumble = new Rumble(RumbleType.kLeftRumble, 1.0, driverController);
        matchTimeRumble = new Rumble(RumbleType.kRightRumble, 1.0, driverController);
        countdown10Rumble = new Rumble(RumbleType.kRightRumble, 1.0, driverController);
        countdown5Rumble = new Rumble(RumbleType.kRightRumble, 1.0, driverController);
        targetAlignedRumble = new Rumble(RumbleType.kLeftRumble, 1.0, driverController);

        if(DriverStation.isJoystickConnected(ControllerConstants.hybridControllerPort)) {
            configHybridBindings();
            // SmartDashboard.putString("control type", "hybrid");
        }
        else {
            if(DriverStation.getJoystickIsXbox(ControllerConstants.driverGamepadPort)) {
                configDriverBindings(ControllerType.kGamepad);
                // SmartDashboard.putString("control type", "gamepad");
            }
            else if(DriverStation.isJoystickConnected(ControllerConstants.driverRightJoystickPort)) {
                configDriverBindings(ControllerType.kJoystick);
                // SmartDashboard.putString("control type", "joysticks");
            }
            else {
                // SmartDashboard.putString("control type", "only operator");
                brownOutRumble = new Rumble(RumbleType.kRightRumble, 1.0);
            }

            configOperatorBindings();
        }

        brownOutRumble.rumbleWhen(() -> RobotController.isBrownedOut(), 2.0);
        
        matchTimeRumble.pulseWhen(() -> DriverStation.getMatchTime() <= 60.0 && DriverStation.isTeleop(), 3);
        matchTimeRumble.pulseWhen(() -> DriverStation.getMatchTime() <= 30.0 && DriverStation.isTeleop(), 2);
        matchTimeRumble.pulseWhen(() -> DriverStation.getMatchTime() <= 15.0 && DriverStation.isTeleop(), 1);

        countdown10Rumble.setPulseTime(1.0);
        countdown10Rumble.pulseWhen(() -> DriverStation.getMatchTime() <= 10.0 && DriverStation.isTeleop(), 5);

        countdown5Rumble.setPulseTime(1.0);
        countdown5Rumble.setPulseLength(0.25);
        countdown5Rumble.pulseWhen(() -> DriverStation.getMatchTime() <= 5.0 && DriverStation.isTeleop(), 5);

        targetAlignedRumble.pulseWhen(() -> visionSys.targetIsXAligned() && visionSys.hasTarget() && swerveSys.isTracking());
    }

    public void configDriverBindings(ControllerType driverControllerType) {
        if(driverControllerType.equals(ControllerType.kGamepad)) {
            swerveSys.setDefaultCommand(
                new SwerveDriveCmd(
                    () -> deadband(driverController.getLeftY(), driverControllerType),
                    () -> deadband(driverController.getLeftX(), driverControllerType),
                    () -> deadband(driverController.getRightX(), driverControllerType),
                    true,
                    swerveSys
                )
            );

            driverABtn.onTrue(new ToggleWeeWooModeCmd(lightsSys));
            driverXBtn.whileTrue(new AutoAlignCmd(
                () -> deadband(driverController.getLeftY(), driverControllerType),
                () -> deadband(driverController.getLeftX(), driverControllerType),
                visionSys, swerveSys, liftSys)
            );
            driverYBtn.onTrue(new TogglePartyModeCmd(lightsSys));
            
            driverMenuBtn.onTrue(new ResetHeadingCmd(swerveSys));

            driverRightTriggerBtn
                .onTrue(new OutCmd(intakeSys, lightsSys))
                .whileTrue(new RepeatCommand(new SetRelativeSpeedCmd(intakeSys, lightsSys)))
                .onFalse(new InCmd(intakeSys))
                .onFalse(new StopRollersCmd(intakeSys, lightsSys));
            driverLeftTriggerBtn.whileTrue(new LockCmd(swerveSys));

            brownOutRumble.addControllers(driverController);
            matchTimeRumble.addControllers(driverController);
            countdown10Rumble.addControllers(driverController);
            countdown5Rumble.addControllers(driverController);
            targetAlignedRumble.addControllers(driverController);
        }
        else {
            swerveSys.setDefaultCommand(
                new SwerveDriveCmd(
                    () -> deadband(driverLeftJoystick.getY(), driverControllerType),
                    () -> deadband(driverLeftJoystick.getX(), driverControllerType),
                    () -> deadband(driverRightJoystick.getX(), driverControllerType),
                    true,
                    swerveSys
                )
            );

            driverRightJoystickTriggerBtn
                .onTrue(new OutCmd(intakeSys, lightsSys))
                .whileTrue(new SetRelativeSpeedCmd(intakeSys, lightsSys))
                .onFalse(new InCmd(intakeSys))
                .onFalse(new StopRollersCmd(intakeSys, lightsSys));

            driverRightJoystickThumbBtn.onTrue(new ResetHeadingCmd(swerveSys));
        }
    }

    public void configOperatorBindings() {
        liftSys.setDefaultCommand(
            new LiftManualControlCmd(
                () -> deadband(operatorController.getRightY(), ControllerType.kGamepad),
                liftSys
            )
        );

        intakeSys.setDefaultCommand(
            new IntakeManualControlCmd(
                () -> deadband(operatorController.getLeftY(), ControllerType.kGamepad),
                () -> deadband(operatorController.getRightTriggerAxis(), ControllerType.kGamepad),
                () -> deadband(operatorController.getLeftTriggerAxis(), ControllerType.kGamepad),
                intakeSys
            )
        );

        operatorABtn.onTrue(new Row1Cmd(true, liftSys));
        operatorBBtn.onTrue(new Row2Cmd(lightsSys, true, liftSys));
        operatorXBtn.onTrue(new DownCmd(true, liftSys));
        operatorYBtn.onTrue(new Row3Cmd(lightsSys, true, liftSys));

        operatorWindowBtn.onTrue(new SetElementStatusCmd(GameElement.kCube, liftSys, intakeSys, visionSys, lightsSys));
        operatorMenuBtn.onTrue(new SetElementStatusCmd(GameElement.kCone, liftSys, intakeSys, visionSys, lightsSys));

        operatorWindowBtn.and(operatorMenuBtn).onTrue(new SetElementStatusCmd(GameElement.kNone, liftSys, intakeSys, visionSys, lightsSys));
        
        operatorLeftBumper.onTrue(new OpenCmd(clawSys));
        operatorRightBumper.onTrue(new CloseCmd(clawSys));

        operatorUpBtn.onTrue(new ShelfPickupCmd(true, liftSys));
        operatorRightBtn.onTrue(new YEETCmd(liftSys, clawSys));
        operatorDownBtn.onTrue(new HoverCmd(true, liftSys));
        operatorLeftBtn.onTrue(new HybridYeetCmd(liftSys, clawSys));

        matchTimeRumble.addControllers(operatorController);
        countdown10Rumble.addControllers(operatorController);
        countdown5Rumble.addControllers(operatorController);
        targetAlignedRumble.addControllers(operatorController);
    }

    public void configHybridBindings() {
        swerveSys.setDefaultCommand(
            new SwerveDriveCmd(
                () -> deadband(hybridController.getLeftY(), ControllerType.kGamepad),
                () -> deadband(hybridController.getLeftX(), ControllerType.kGamepad),
                () -> deadband(hybridController.getRightX(), ControllerType.kGamepad),
                true,
                swerveSys
            )
        );

        hybridABtn.onTrue(new Row1Cmd(true, liftSys));
        hybridBBtn.onTrue(new Row2Cmd(lightsSys, true, liftSys));
        hybridXBtn.onTrue(new DownCmd(true, liftSys));
        hybridYBtn.onTrue(new Row3Cmd(lightsSys, true, liftSys));

        hybridWindowBtn.onTrue(new SetElementStatusCmd(GameElement.kCube, liftSys, intakeSys, visionSys, lightsSys));
        hybridMenuBtn.onTrue(new SetElementStatusCmd(GameElement.kCone, liftSys, intakeSys, visionSys, lightsSys));

        hybridWindowBtn.and(operatorMenuBtn).onTrue(new SetElementStatusCmd(GameElement.kNone, liftSys, intakeSys, visionSys, lightsSys));
        
        hybridLeftBumper.onTrue(new OpenCmd(clawSys));
        hybridRightBumper.onTrue(new CloseCmd(clawSys));

        hybridRightJoystickPressBtn.onTrue(new ResetHeadingCmd(swerveSys));

        brownOutRumble.addControllers(hybridController);
        matchTimeRumble.addControllers(hybridController);
        countdown10Rumble.addControllers(hybridController);
        countdown5Rumble.addControllers(hybridController);
        targetAlignedRumble.addControllers(hybridController);
    }

    public Command getAutonomousCommand() {
        return autoSelector.getSelected();
    }

    /**
     * Deadbands inputs to eliminate tiny unwanted values from the joysticks or gamepad sticks.
     * <p>If the distance between the input and zero is less than the deadband amount, the output will be zero.
     * Otherwise, the value will not change.
     * 
     * @param input The controller value to deadband.
     * @param controllerType The type of controller, since joysticks, gamepad sticks, and gamepad triggers can
     * have different deadbands.
     * @return The deadbanded controller value.
     */
    public double deadband(double value, ControllerType controllerType) {

        if (Math.abs(value) < (controllerType.equals(ControllerType.kGamepad) ?
                ControllerConstants.gamepadDeadband :
                ControllerConstants.joystickDeadband
            )
        )
            return 0.0;
        
        return value;
    }

    public void updateInterface() {
        // BATTERY
        SmartDashboard.putNumber("battery voltage", RobotController.getBatteryVoltage());

        // SWERVE
        double headingDisplay = swerveSys.getHeading().getDegrees() % 360;
        if(headingDisplay < 0) {
            headingDisplay += 360;
        }

        SmartDashboard.putNumber("heading", headingDisplay);

        SmartDashboard.putNumber("speed m/s", swerveSys.getAverageDriveVelocityMetersPerSecond());
        // SmartDashboard.putNumber("speed mph", swerveSys.getAverageDriveVelocityMetersPerSecond() * 2.23694);

        // COMPRESSOR
        SmartDashboard.putNumber("pressure PSI", compressorSys.getPressurePSI());

        SmartDashboard.putString("compressor status", (compressorSys.isEnabled() ? (compressorSys.isRunning() ? "PRESSURIZING" : "OFF") : "DISABLED"));
        SmartDashboard.putNumber("compressor elapsed", compressorSys.getRunTimeSeconds());
        SmartDashboard.putNumber("compressor turn on", compressorSys.getTurnOnCount());

        // INTAKE
        SmartDashboard.putNumber("intake inches", intakeSys.getCurrentPosition());
        SmartDashboard.putNumber("roller rpm", intakeSys.getCurrentSpeedRPM());

        // LIFT
        SmartDashboard.putNumber("lift inches", liftSys.getCurrentPosition());
        SmartDashboard.putString("lift articulation", (liftSys.isArticulationOverride() ? (liftSys.isArticulatedDown() ? "DOWN" : "UP") : "UP - OVERRIDE"));
        SmartDashboard.putNumber("lift target", liftSys.getTargetInches());

        // CLAW
        SmartDashboard.putString("claw status", (clawSys.isOpen() ? "OPEN" : "CLOSED"));
    }
}

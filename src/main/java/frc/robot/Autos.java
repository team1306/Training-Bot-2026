package frc.robot;

import static edu.wpi.first.units.Units.Seconds;

import badgerutils.networktables.LoggedNetworkTablesBuilder;
import badgerutils.triggers.AllianceTriggers;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.FlippingUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.controls.Controls;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeConstants;
import java.util.List;
import java.util.Optional;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

public class Autos {
  private final Drive drive;

  // Declare commands here

  // Prefer to construct autos lazily to save limited memory. Required with many auto files
  private final LoggedDashboardChooser<Auto> autoChooser;

  private final LoggedNetworkNumber autoWaitTime =
      new LoggedNetworkNumber("Autos/Auto Wait Seconds");

  private static final List<String> autoNames = AutoBuilder.getAllAutoNames();

  public Autos(Drive drive, Intake intake) {
    this.drive = drive;

    // Initialize commands here

    autoWaitTime.set(0);

    autoChooser = new LoggedDashboardChooser<>("Autos/Auto Chooser");

    autoChooser.addDefaultOption("None", new Auto("Empty", new InstantCommand()));

    for (String auto : autoNames) {
      autoChooser.addOption(auto, new Auto(auto, auto));
    }

    autoChooser.addOption(
        "Deploy",
        new Auto(
            "Deploy", intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime)));
    autoChooser.addOption(
        "Deploy & Outtake",
        new Auto(
            "Deploy & Outtake",
            new SequentialCommandGroup(
                intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime),
                Commands.waitSeconds(2),
                intake.speedCommand(() -> -1.).withTimeout(8))));
    autoChooser.addOption(
        "Deploy, Outtake, Drive",
        new Auto(
            "Deploy, Outtake, Drive",
            new SequentialCommandGroup(
                intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime),
                Commands.waitSeconds(2),
                intake.speedCommand(() -> -1.0).withTimeout(8),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> -.3, () -> 0, () -> 1, () -> 1)
                    .withTimeout(5))));
    autoChooser.addOption(
        "Center & Face Forward (spin right)",
        new Auto(
            "Center & Face Forward (spin right)",
            new SequentialCommandGroup(
                intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime),
                Commands.waitSeconds(2),
                intake.speedCommand(() -> -1.0).withTimeout(8),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> -.3, () -> 0, () -> 1, () -> 1)
                    .withTimeout(5),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> 0, () -> .3, () -> 1, () -> 1)
                    .withTimeout(4))));
    autoChooser.addOption(
        "Center & Face Forward (spin left)",
        new Auto(
            "Center & Face Forward (spin left)",
            new SequentialCommandGroup(
                intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime),
                Commands.waitSeconds(2),
                intake.speedCommand(() -> -1.0).withTimeout(8),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> -.3, () -> 0, () -> 1, () -> 1)
                    .withTimeout(5),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> 0, () -> -.3, () -> 1, () -> 1)
                    .withTimeout(4))));

    SlewRateLimiter limiter = new SlewRateLimiter(1, 0, 0.3);
    autoChooser.addOption(
        "Disruption (spin right)",
        new Auto(
            "Disruption (spin right)",
            new SequentialCommandGroup(
                new ParallelCommandGroup(
                        intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime),
                        DriveCommands.joystickDriveCommand(
                            drive, () -> 0, () -> limiter.calculate(1), () -> 0, () -> 1, () -> 1))
                    .withTimeout(.7),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> 1, () -> 0, () -> 1, () -> 1)
                    .withTimeout(.5),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> 0, () -> 1, () -> 1, () -> 1)
                    .withTimeout(4))));
    autoChooser.addOption(
        "Disruption (spin left)",
        new Auto(
            "Disruption (spin left)",
            new SequentialCommandGroup(
                new ParallelCommandGroup(
                        intake.deployIntakeCommand().withTimeout(IntakeConstants.intakeDeployTime),
                        DriveCommands.joystickDriveCommand(
                            drive, () -> 0, () -> limiter.calculate(1), () -> 0, () -> 1, () -> 1))
                    .withTimeout(0.7),
                DriveCommands.joystickDriveCommand(
                        drive, () -> 0, () -> 1, () -> 0, () -> 1, () -> 1)
                    .withTimeout(0.5),
                DriveCommands.joystickDriveCommand(
                    drive, () -> 0, () -> 0, () -> -1, () -> 1, () -> 1))));

    Controls.addPersistentTrigger(
        () ->
            LoggedNetworkTablesBuilder.createLoggedAutoResettingButton("Autos/Reset Odometry")
                .onTrue(new InstantCommand(this::resetAutoOdometry).ignoringDisable(true)));

    bindNamedCommands();
    bindEventMarkers();
  }

  public Command createCommandFromSelectedAuto() {
    Auto auto = autoChooser.get();
    return new WaitCommand(autoWaitTime.get())
        .andThen(
            auto.getCommand()
                .alongWith(
                    Commands.waitTime(Seconds.of(3))
                        .andThen(Commands.runOnce(() -> drive.setLowCurrentLimits()))))
        .andThen(Commands.print("Auto Complete"))
        .withName(auto.getName());
  }

  private void resetAutoOdometry() {
    Optional<PathPlannerAuto> autoOptional = autoChooser.get().getAuto();

    if (!DriverStation.isDisabled() || autoOptional.isEmpty()) return;

    Pose2d startingPosition = autoOptional.get().getStartingPose();

    drive.setPose(
        AllianceTriggers.isBlueAlliance()
            ? startingPosition
            : FlippingUtil.flipFieldPose(startingPosition));
  }

  private void bindNamedCommands() {}

  private void bindEventMarkers() {}

  public static final class Auto {
    private final String autoName;
    private final String name;
    private final Command command;

    public Auto(String name, String autoName) {
      this.command = new InstantCommand();
      this.name = name;
      this.autoName = autoName;
    }

    public Auto(String name, Command command) {
      this.command = command;
      this.name = name;
      this.autoName = null;
    }

    public String getName() {
      return name;
    }

    public Optional<PathPlannerAuto> getAuto() {
      if (autoName == null) return Optional.empty();
      if (!autoNames.contains(autoName)) return Optional.empty();

      return Optional.of(new PathPlannerAuto(autoName));
    }

    public Command getCommand() {
      if (autoName != null) {
        return new PathPlannerAuto(autoName);
      } else if (command != null) {
        return command;
      }
      return new InstantCommand();
    }
  }
}

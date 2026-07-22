package frc.robot.controls;

import badgerutils.commands.CommandUtils;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;

public class CompetitionControllerMapping extends ControllerMapping {

  private final Drive drive;
  private Intake intake;

  public CompetitionControllerMapping(
      CommandXboxController driverController,
      CommandXboxController operatorController,
      Drive drive,
      Intake intake) {
    super(driverController, operatorController);
    this.drive = drive;
    this.intake = intake;
  }

  @Override
  public void bind() {
    /* ---Default Commands--- */

    // Drive with stick
    drive.setDefaultCommand(
        DriveCommands.joystickDriveCommand(
            drive,
            () -> -driverController.getLeftY(),
            () -> -driverController.getLeftX(),
            () -> -driverController.getRightX(),
            () -> 1,
            () -> 1));

    /* ---P1--- */

    // Reset Odometry
    driverController
        .start()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), Rotation2d.kZero)),
                    drive)
                .ignoringDisable(true));

    /* ---P2--- */
    operatorController
        .a()
        .whileTrue(
            Commands.startEnd(
                () -> intake.speedCommand(() -> 0.8), () -> intake.stopMotorCommand(), intake));
    operatorController
        .y()
        .whileTrue(
            Commands.startEnd(
                () -> intake.speedCommand(() -> -0.8), () -> intake.stopMotorCommand(), intake));
    operatorController.leftBumper().whileTrue(intake.deployIntakeCommand());
    operatorController.rightBumper().whileTrue(intake.retractCommand());
  }

  @Override
  public void clear() {
    super.clear();
    CommandUtils.removeAndCancelDefaultCommand(drive);
  }
}

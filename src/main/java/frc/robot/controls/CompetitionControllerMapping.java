package frc.robot.controls;

import badgerutils.commands.CommandUtils;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeConstants;

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

  static double intakeSpeedAdjustment = 0;
  static double outtakeSpeedAdjustment = -0.2;

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
        .whileTrue(intake.speedCommand(() -> IntakeConstants.intakeSpeed + intakeSpeedAdjustment));
    operatorController
        .y()
        .whileTrue(
            intake.speedCommand(
                () ->
                    -IntakeConstants.intakeSpeed - intakeSpeedAdjustment - outtakeSpeedAdjustment));
    operatorController.leftBumper().whileTrue(intake.deployIntakeCommand());
    operatorController.rightBumper().whileTrue(intake.retractCommand());

    operatorController.povUp().onTrue(new InstantCommand(() -> intakeSpeedAdjustment += 0.1));
    operatorController.povDown().onTrue(new InstantCommand(() -> intakeSpeedAdjustment -= 0.1));
    operatorController.povRight().onTrue(new InstantCommand(() -> outtakeSpeedAdjustment += 0.1));
    operatorController.povLeft().onTrue(new InstantCommand(() -> outtakeSpeedAdjustment -= 0.1));
  }

  @Override
  public void clear() {
    super.clear();
    CommandUtils.removeAndCancelDefaultCommand(drive);
  }
}

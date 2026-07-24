package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
  IntakeIO intakeIO;
  IntakeIOReal intakeIOReal;
  IntakeIOInputsAutoLogged intakeIOInputs = new IntakeIOInputsAutoLogged();

  public Intake(IntakeIO intakeIO) {
    this.intakeIO = intakeIO;
  }

  public void spinUp(double speedSupplier) {
    intakeIO.setDutyCycle(speedSupplier);
    Logger.recordOutput("intake/speed", speedSupplier);
  }

  private void stopMotor() {
    intakeIO.stopMotor();
    Logger.recordOutput("intake/speed", 0);
  }

  private void retract() {
    intakeIO.retract();
  }

  private void stopDeployMotor() {
    intakeIO.stopDeploy();
  }

  public Command speedCommand(Supplier<Double> supplier) {
    return Commands.runEnd(() -> spinUp(supplier.get()), () -> stopMotor(), this);
  }

  public Command stopMotorCommand() {
    return new InstantCommand(() -> this.stopMotor(), this);
  }

  public Command deployIntakeCommand() {
    return Commands.startEnd(() -> spinUp(1), () -> stopDeployMotor(), this);
  }

  public Command retractCommand() {
    return Commands.startEnd(() -> retract(), () -> stopDeployMotor(), this);
  }
}

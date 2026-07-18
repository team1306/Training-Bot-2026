package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
  IntakeIO intakeIO;
  IntakeIOInputsAutoLogged intakeIOInputs = new IntakeIOInputsAutoLogged();

  public void spinUp(double speedSupplier) {
    intakeIO.setDutyCycle(speedSupplier);
    Logger.recordOutput("intake/speed", speedSupplier);
  }

  public void stopMotor() {
    intakeIO.stopMotor();
  }

  public void deploy() {
    intakeIO.deploy();
  }

  public void retract() {
    intakeIO.retract();
  }

  public Command speedCommand(Supplier<Double> supplier) {
    return Commands.runEnd(() -> spinUp(supplier.get()), () -> stopMotor(), this);
  }

  public Command deployCommand() {
    return Commands.runOnce(() -> deploy(), this);
  }

  public Command retractCommand() {
    return Commands.runOnce(() -> retract(), this);
  }
}

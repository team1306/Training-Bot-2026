package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Supplier;

public class Intake extends SubsystemBase {

  IntakeIO intakeIO;
  IntakeIOInputsAutoLogged intakeIOInputs = new IntakeIOInputsAutoLogged();

  public void spinUp(Supplier<Double> speedSupplier) {
    intakeIO.setDutyCycle(speedSupplier.get());
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
}

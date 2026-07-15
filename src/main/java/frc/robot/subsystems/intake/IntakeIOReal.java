package frc.robot.subsystems.intake;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import java.util.function.Supplier;

public class IntakeIOReal implements IntakeIO {

  TalonFX upperIntakeX60 = new TalonFX(IntakeConstants.upperX60id);
  TalonFX upperIntakeX44 = new TalonFX(IntakeConstants.upperX44id);

  TalonFX lowerIntakeX60 = new TalonFX(IntakeConstants.lowerX60id);
  TalonFX lowerIntakeX44 = new TalonFX(IntakeConstants.lowerX44id);

  TalonFX deployX60 = new TalonFX(IntakeConstants.deployID);

  public IntakeIOReal() {

    upperIntakeX60.getConfigurator().apply(IntakeConstants.configX60);
    lowerIntakeX60.getConfigurator().apply(IntakeConstants.configX60);
    upperIntakeX44.getConfigurator().apply(IntakeConstants.configX44);
    lowerIntakeX44.getConfigurator().apply(IntakeConstants.configX44);

    deployX60.getConfigurator().apply(IntakeConstants.configDeployX60);
  }

  @Override
  public void setDutyCycle(double dutyCycle) {
    spinUp(() -> dutyCycle);
  }

  public void spinUp(Supplier<Double> speedSupplier) {
    // this.intakeIO.setDutyCycle(speedSupplier.get());
    upperIntakeX60.setControl(new DutyCycleOut(speedSupplier.get()));
    lowerIntakeX60.setControl(new DutyCycleOut(speedSupplier.get()));
    upperIntakeX44.setControl(new DutyCycleOut(speedSupplier.get()));
    lowerIntakeX44.setControl(new DutyCycleOut(speedSupplier.get()));
  }

  @Override
  public void stopMotor() {
    upperIntakeX60.setControl(new NeutralOut());
    lowerIntakeX60.setControl(new NeutralOut());
    upperIntakeX44.setControl(new NeutralOut());
    lowerIntakeX44.setControl(new NeutralOut());
  }

  @Override
  public void deploy() {
    deployX60.setControl(new PositionVoltage(0.25));
  }

  @Override
  public void retract() {
    deployX60.setControl(new PositionVoltage(-0.25));
  }
}

package frc.robot.subsystems.intake;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeIOReal implements IntakeIO {

  TalonFX upperIntakeX60 = new TalonFX(IntakeConstants.upperX60id);
  TalonFX upperIntakeX44 = new TalonFX(IntakeConstants.upperX44id);

  TalonFX lowerIntakeX60 = new TalonFX(IntakeConstants.lowerX60id);
  TalonFX lowerIntakeX44 = new TalonFX(IntakeConstants.lowerX44id);

  TalonFX deployX60 = new TalonFX(IntakeConstants.deployID);

  public IntakeIOReal() {

    upperIntakeX60.getConfigurator().apply(IntakeConstants.config);
    lowerIntakeX60.getConfigurator().apply(IntakeConstants.configInverted);
    upperIntakeX44.getConfigurator().apply(IntakeConstants.config);
    lowerIntakeX44.getConfigurator().apply(IntakeConstants.configInverted);

    deployX60.getConfigurator().apply(IntakeConstants.configDeploy);
  }

  @Override
  public void setDutyCycle(double dutyCycle) {
    upperIntakeX60.setControl(new DutyCycleOut(dutyCycle));
    lowerIntakeX60.setControl(new DutyCycleOut(dutyCycle));
    upperIntakeX44.setControl(new DutyCycleOut(dutyCycle));
    lowerIntakeX44.setControl(new DutyCycleOut(dutyCycle));
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
    deployX60.setControl(new PositionTorqueCurrentFOC(0.25));
  }

  @Override
  public void retract() {
    deployX60.setControl(new PositionTorqueCurrentFOC(-0.25));
  }
}

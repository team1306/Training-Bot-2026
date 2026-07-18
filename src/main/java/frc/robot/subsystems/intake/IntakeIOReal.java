package frc.robot.subsystems.intake;

import badgerutils.advantagekit.PIDTunable;
import badgerutils.advantagekit.talonfx.TalonFXSignals;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeIOReal implements IntakeIO {

  TalonFX leftIntakeX60 = new TalonFX(IntakeConstants.leftX60id);
  TalonFX leftIntakeX44 = new TalonFX(IntakeConstants.leftX44id);

  TalonFX rightIntakeX60 = new TalonFX(IntakeConstants.rightX60id);
  TalonFX rightIntakeX44 = new TalonFX(IntakeConstants.rightX44id);

  TalonFX deployX60 = new TalonFX(IntakeConstants.deployID);

  private boolean deployed = false;

  private final TalonFXSignals leftX60MotorSignal;
  private final TalonFXSignals leftX44MotorSignal;
  private final TalonFXSignals rightX60MotorSignal;
  private final TalonFXSignals rightX44MotorSignal;
  private final TalonFXSignals deployMotorSignal;

  private final PIDTunable pidTunable;

  public IntakeIOReal() {

    leftIntakeX60.getConfigurator().apply(IntakeConstants.config);
    rightIntakeX60.getConfigurator().apply(IntakeConstants.configInverted);
    leftIntakeX44.getConfigurator().apply(IntakeConstants.config);
    rightIntakeX44.getConfigurator().apply(IntakeConstants.configInverted);

    deployX60.getConfigurator().apply(IntakeConstants.configDeploy);

    rightX60MotorSignal = new TalonFXSignals(rightIntakeX60);
    rightX44MotorSignal = new TalonFXSignals(rightIntakeX44);
    leftX60MotorSignal = new TalonFXSignals(leftIntakeX60);
    leftX44MotorSignal = new TalonFXSignals(leftIntakeX44);
    deployMotorSignal = new TalonFXSignals(deployX60);

    pidTunable =
        new PIDTunable("intake", SlotConfigs.from(IntakeConstants.configDeploy.Slot0), deployX60);
  }

  @Override
  public void setDutyCycle(double dutyCycle) {
    leftIntakeX60.setControl(new DutyCycleOut(dutyCycle));
    rightIntakeX60.setControl(new DutyCycleOut(dutyCycle));
    leftIntakeX44.setControl(new DutyCycleOut(dutyCycle));
    rightIntakeX44.setControl(new DutyCycleOut(dutyCycle));
  }

  @Override
  public void stopMotor() {
    leftIntakeX60.setControl(new NeutralOut());
    rightIntakeX60.setControl(new NeutralOut());
    leftIntakeX44.setControl(new NeutralOut());
    rightIntakeX44.setControl(new NeutralOut());
  }

  @Override
  public void deploy() {
    if (deployed == true) return;

    deployed = true;
    deployX60.setControl(new PositionTorqueCurrentFOC(IntakeConstants.deployAngleChange));
  }

  @Override
  public void retract() {
    if (deployed == false) return;

    deployed = false;
    deployX60.setControl(new PositionTorqueCurrentFOC(-IntakeConstants.deployAngleChange));
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.deployX60 = deployMotorSignal.createLoggedTalonFX();
    inputs.leftX60 = leftX60MotorSignal.createLoggedTalonFX();
    inputs.rightX60 = rightX60MotorSignal.createLoggedTalonFX();
    inputs.leftX44 = leftX44MotorSignal.createLoggedTalonFX();
    inputs.rightX44 = rightX44MotorSignal.createLoggedTalonFX();
  }
}

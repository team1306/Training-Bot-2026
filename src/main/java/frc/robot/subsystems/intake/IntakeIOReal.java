package frc.robot.subsystems.intake;

import badgerutils.advantagekit.PIDTunable;
import badgerutils.advantagekit.talonfx.TalonFXSignals;
import com.ctre.phoenix6.configs.SlotConfigs;
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

  private boolean deployed = false;

  private final TalonFXSignals upperLeftMotorSignal;
  private final TalonFXSignals upperRightMotorSignal;
  private final TalonFXSignals lowerLeftMotorSignal;
  private final TalonFXSignals lowerRightMotorSignal;
  private final TalonFXSignals deployMotorSignal;

  private final PIDTunable pidTunable;

  public IntakeIOReal() {

    upperIntakeX60.getConfigurator().apply(IntakeConstants.config);
    lowerIntakeX60.getConfigurator().apply(IntakeConstants.configInverted);
    upperIntakeX44.getConfigurator().apply(IntakeConstants.config);
    lowerIntakeX44.getConfigurator().apply(IntakeConstants.configInverted);

    deployX60.getConfigurator().apply(IntakeConstants.configDeploy);

    lowerLeftMotorSignal = new TalonFXSignals(lowerIntakeX60);
    lowerRightMotorSignal = new TalonFXSignals(lowerIntakeX44);
    upperLeftMotorSignal = new TalonFXSignals(upperIntakeX60);
    upperRightMotorSignal = new TalonFXSignals(upperIntakeX44);
    deployMotorSignal = new TalonFXSignals(deployX60);

    pidTunable =
        new PIDTunable("intake", SlotConfigs.from(IntakeConstants.configDeploy.Slot0), deployX60);
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
    if (deployed == true) return;

    deployed = true;
    deployX60.setControl(new PositionTorqueCurrentFOC(0.33333));
  }

  @Override
  public void retract() {
    if (deployed == false) return;

    deployed = false;
    deployX60.setControl(new PositionTorqueCurrentFOC(-0.33333));
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.deployX60 = deployMotorSignal.createLoggedTalonFX();
    inputs.upperLeftX60 = upperLeftMotorSignal.createLoggedTalonFX();
    inputs.upperRightX60 = upperRightMotorSignal.createLoggedTalonFX();
    inputs.lowerLeftX60 = lowerLeftMotorSignal.createLoggedTalonFX();
    inputs.lowerRightX60 = lowerRightMotorSignal.createLoggedTalonFX();
  }
}

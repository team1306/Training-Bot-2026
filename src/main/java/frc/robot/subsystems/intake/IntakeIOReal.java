package frc.robot.subsystems.intake;

import badgerutils.advantagekit.PIDTunable;
import badgerutils.advantagekit.talonfx.TalonFXSignals;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class IntakeIOReal implements IntakeIO {

  TalonFX leftIntakeX60 = new TalonFX(IntakeConstants.leftX60id);
  TalonFX leftIntakeX44 = new TalonFX(IntakeConstants.leftX44id);

  TalonFX rightIntakeX60 = new TalonFX(IntakeConstants.rightX60id);
  TalonFX rightIntakeX44 = new TalonFX(IntakeConstants.rightX44id);

  TalonFX deployX60 = new TalonFX(IntakeConstants.deployID);

  private final TalonFXSignals leftX60MotorSignal;
  private final TalonFXSignals leftX44MotorSignal;
  private final TalonFXSignals rightX60MotorSignal;
  private final TalonFXSignals rightX44MotorSignal;
  private final TalonFXSignals deployMotorSignal;

  private final PIDTunable pidTunable;

  private IntakeIO intakeIO;

  public IntakeIOReal() {

    leftIntakeX60.getConfigurator().apply(IntakeConstants.configInverted);
    rightIntakeX60.getConfigurator().apply(IntakeConstants.config);
    leftIntakeX44.getConfigurator().apply(IntakeConstants.configInverted);
    rightIntakeX44.getConfigurator().apply(IntakeConstants.config);

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
  public void setDutyCycle(double speed) {
    leftIntakeX60.setControl(new DutyCycleOut(speed));
    rightIntakeX60.setControl(new DutyCycleOut(speed));
    leftIntakeX44.setControl(new DutyCycleOut(speed));
    rightIntakeX44.setControl(new DutyCycleOut(speed));
    System.out.println("3");
  }

  @Override
  public void stopMotor() {
    leftIntakeX60.setControl(new NeutralOut());
    rightIntakeX60.setControl(new NeutralOut());
    leftIntakeX44.setControl(new NeutralOut());
    rightIntakeX44.setControl(new NeutralOut());
    System.out.println("3321");
  }

  @Override
  public void deploy() {
    deployX60.setControl(new DutyCycleOut(1));
    System.out.println("5");
  }

  @Override
  public void stopDeploy() {
    deployX60.setControl(new DutyCycleOut(0));
    System.out.println("6");
  }

  @Override
  public void retract() {
    deployX60.setControl(new DutyCycleOut(-0.2));
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

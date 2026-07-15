package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Amp;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IntakeConstants {
  public static final int upperX60id = 1;
  public static final int upperX44id = 2;
  public static final int lowerX60id = 3;
  public static final int lowerX44id = 4;
  public static final int deployID = 5;
  public static TalonFXConfiguration configX60;
  public static TalonFXConfiguration configX44;
  public static TalonFXConfiguration configDeployX60;

  public IntakeConstants() {
    configX60 = new TalonFXConfiguration();
    configX60
        .MotorOutput
        .withNeutralMode(NeutralModeValue.Coast)
        .withInverted(InvertedValue.Clockwise_Positive);
    configX60.CurrentLimits.withSupplyCurrentLimitEnable(true).withSupplyCurrentLimit(Amp.of(60));
    configX60.Slot0.withKP(10);

    configDeployX60 = new TalonFXConfiguration();
    configX60
        .MotorOutput
        .withNeutralMode(NeutralModeValue.Coast)
        .withInverted(InvertedValue.Clockwise_Positive);
    configX60.CurrentLimits.withSupplyCurrentLimitEnable(true).withSupplyCurrentLimit(Amp.of(50));
    configX60.Slot0.withKP(10);

    configX44 = new TalonFXConfiguration();
    configX44
        .MotorOutput
        .withNeutralMode(NeutralModeValue.Coast)
        .withInverted(InvertedValue.Clockwise_Positive);
    configX44.CurrentLimits.withSupplyCurrentLimitEnable(true).withSupplyCurrentLimit(Amp.of(60));
    configX44.Slot0.withKP(10);
  }
}

package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Amp;
import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IntakeConstants {
  public static final int leftX60id = 22;
  public static final int leftX44id = 21;
  public static final int rightX60id = 24;
  public static final int rightX44id = 23;
  public static final int deployID = 25;
  public static final double KP = 1;
  public static final double KD = 0;
  public static final double KS = 0;
  public static final double deployAngleChange = 105.0 / 360.0;
  public static TalonFXConfiguration config =
      new TalonFXConfiguration()
          .withMotorOutput(
              new MotorOutputConfigs()
                  .withNeutralMode(NeutralModeValue.Brake)
                  .withInverted(InvertedValue.Clockwise_Positive))
          .withCurrentLimits(
              new CurrentLimitsConfigs()
                  .withSupplyCurrentLimitEnable(true)
                  .withSupplyCurrentLimit(Amp.of(70))
                  .withStatorCurrentLimit(Amps.of(50))
                  .withStatorCurrentLimitEnable(true));

  public static TalonFXConfiguration configInverted =
      config
          .clone()
          .withMotorOutput(
              new MotorOutputConfigs().withInverted(InvertedValue.CounterClockwise_Positive));

  public static TalonFXConfiguration configDeploy =
      config.clone().withSlot0(new Slot0Configs().withKP(KP).withKD(KD).withKS(KS));
}

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
  @AutoLog
  public static class IntakeIOInputs {
    public boolean upperX60Connected = false;
    public boolean upperX44Connected = false;
    public boolean lowerX60Connected = false;
    public boolean lowerX44Connected = false;
    public boolean deployX60Connected = false;

    public AngularVelocity upperX60Speed = RadiansPerSecond.of(0);
    public AngularVelocity lowerX60Speed = RadiansPerSecond.of(0);
    public AngularVelocity upperX44Speed = RadiansPerSecond.of(0);
    public AngularVelocity lowerX44Speed = RadiansPerSecond.of(0);

    public Current upperX60Current = Amps.of(0);
    public Current lowerX60Current = Amps.of(0);
    public Current upperX44Current = Amps.of(0);
    public Current lowerX44Current = Amps.of(0);

    public Voltage upperX60Voltage = Volts.of(0);
    public Voltage lowerX60Voltage = Volts.of(0);
    public Voltage upperX44Voltage = Volts.of(0);
    public Voltage lowerX44Voltage = Volts.of(0);

    public Temperature upperX60Temp = Celsius.of(0);
    public Temperature lowerX60Temp = Celsius.of(0);
    public Temperature upperX44Temp = Celsius.of(0);
    public Temperature lowerX44Temp = Celsius.of(0);
  }

  public default void updateInputs(IntakeIOInputs inputs) {}

  public default void setDutyCycle(double speed) {}

  public default void setDeployAngle(Angle angle) {}

  public default void stopMotor() {}

  public default void deploy() {}

  public default void retract() {}
}

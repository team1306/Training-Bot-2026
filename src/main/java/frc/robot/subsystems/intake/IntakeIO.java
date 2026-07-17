package frc.robot.subsystems.intake;

import badgerutils.advantagekit.talonfx.LoggedTalonFX;
import edu.wpi.first.units.measure.Angle;
import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
  @AutoLog
  public static class IntakeIOInputs {
    public LoggedTalonFX upperRightX60;
    public LoggedTalonFX upperLeftX60;
    public LoggedTalonFX lowerRightX60;
    public LoggedTalonFX lowerLeftX60;
    public LoggedTalonFX deployX60;
  }

  public default void updateInputs(IntakeIOInputs inputs) {}

  public default void setDutyCycle(double speed) {}

  public default void setDeployAngle(Angle angle) {}

  public default void stopMotor() {}

  public default void deploy() {}

  public default void retract() {}
}

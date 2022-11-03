// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.MKBABY;
import frc.robot.Constants.MKINTERPOLERPO;
import frc.robot.Constants.MKLIME;
import frc.robot.interpolerpo.InterpolatingDouble;

/** Add your docs here. */
public class Limelight {
    private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private final NetworkTableEntry tx = table.getEntry("tx");
    private final NetworkTableEntry ty = table.getEntry("ty");
    private final NetworkTableEntry led = table.getEntry("ledMode");
    private final NetworkTableEntry tv = table.getEntry("tv");
    private final NetworkTableEntry pipeline = table.getEntry("pipeline");
    private boolean hasTarget;
    private double distance, visionYaw, visionPitch, RPM, hoodPos;


private Limelight() {
    table.getEntry("pipeline").setValue(MKLIME.pipeline);
  }

  public static Limelight getInstance() {
    return InstanceHolder.mInstance;
  }

  public void updateSensors() {
    visionYaw = tx.getDouble(0.0);
    visionPitch = ty.getDouble(0.0);
    distance = calcDistance(visionPitch);
    hasTarget = tv.getDouble(0.0) != 0.0f; //If tv returns 0, no valid target
  }

  public void setShooterFinal()
  {
    RPM = MKINTERPOLERPO.kRPMMap.getInterpolated(new InterpolatingDouble(distance)).value;
    hoodPos = MKINTERPOLERPO.kHoodMap.getInterpolated(new InterpolatingDouble(distance)).value;
    //Shooter.getInstance().setHoodPositionPercentFF(hoodPos);
    Shooter.getInstance().setShooterCalc(RPM);
  }

  public double etherLimeRCWValue()
  {
    return (visionYaw/MKLIME.maxTX) / MKBABY.rcwBABY;
  }

  public boolean inRange() {
    return hasTarget && Math.abs(visionYaw) < MKLIME.shootTolerance;
  }

  public double calcDistance(double pitch)
  {
    return (MKLIME.goalHeightInches - MKLIME.limeHeightInches) / (Math.tan(Math.toRadians(MKLIME.limeAngle + pitch)));
  }

  public void limeSmartDashboard()
  {
    SmartDashboard.putNumber("distance to gowl", distance);
  }


  private static class InstanceHolder {

    private static final Limelight mInstance = new Limelight();
  }
}


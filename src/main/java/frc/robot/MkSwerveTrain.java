// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.CANID;
import frc.robot.Constants.MKCANCODER;
import frc.robot.Constants.MKTRAIN;
import frc.robot.Constants.MKTURN;

/** Add your docs here. */
public class MkSwerveTrain 
{
    public variables vars;/*
    //TODO how to simplify this without slowing down everything
    private MkSwerveModule topLeftModule = new MkSwerveModule(CANID.topLeftCANID, MKCANCODER.offset[0], MKDRIVE.pidf, MKTURN.pidf);
    private MkSwerveModule topRightModule = new MkSwerveModule(CANID.topRightCANID, MKCANCODER.offset[1], MKDRIVE.pidf, MKTURN.pidf);
    private MkSwerveModule bottomLeftModule = new MkSwerveModule(CANID.bottomLeftCANID, MKCANCODER.offset[2], MKDRIVE.pidf, MKTURN.pidf);
    private MkSwerveModule bottomRightModule = new MkSwerveModule(CANID.bottomRightCANID, MKCANCODER.offset[3], MKDRIVE.pidf, MKTURN.pidf);
*/
private TalonFX topTurnLeft;
private TalonFX topTurnRight;
private TalonFX bottomTurnLeft;
private TalonFX bottomTurnRight;

private TalonFX topDriveLeft;
private TalonFX topDriveRight;
private TalonFX bottomDriveLeft;
private TalonFX bottomDriveRight;

private CANCoder topLeftCoder;
private CANCoder topRightCoder;
private CANCoder bottomLeftCoder;
private CANCoder bottomRightCoder;

private PIDController turn;
//private TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(2,1);

private Motor mMotor = Motor.getInstance();


    private MkSwerveTrain()
    {
        vars = new variables();
        vars.hIntegral = 0;
        vars.mod1 = new double[2];
        vars.mod2 = new double[2];
        vars.mod3 = new double[2];
        vars.mod4 = new double[2];

        turn = new PIDController(vars.hP, vars.hI, vars.hD);
        turn.enableContinuousInput(0, 360);

        topTurnLeft = mMotor.turnMotor(CANID.topTurnLeftCANID);
        topTurnRight = mMotor.turnMotor(CANID.topTurnRightCANID);
        bottomTurnLeft = mMotor.turnMotor(CANID.bottomTurnLeftCANID);
        bottomTurnRight = mMotor.turnMotor(CANID.bottomTurnRightCANID);

        topDriveLeft = mMotor.driveMotor(CANID.topDriveLeftCANID);
        topDriveRight = mMotor.driveMotor(CANID.topDriveRightCANID);
        bottomDriveLeft = mMotor.driveMotor(CANID.bottomDriveLeftCANID);
        bottomDriveRight = mMotor.driveMotor(CANID.bottomDriveRightCANID);

        topLeftCoder = mMotor.cancoder(CANID.topTurnLeftCANCoderCANID, MKCANCODER.topLeftOffset);
        topRightCoder = mMotor.cancoder(CANID.topTurnRightCANCoderCANID, MKCANCODER.topRightOffset);
        bottomLeftCoder = mMotor.cancoder(CANID.bottomTurnLeftCANCoderCANID, MKCANCODER.bottomLeftOffset);
        bottomRightCoder = mMotor.cancoder(CANID.bottomTurnRightCANCoderCANID, MKCANCODER.bottomRightOffset);
    }

    public static MkSwerveTrain getInstance()
    {
        return InstanceHolder.mInstance;
    }

    public void startTrain()
    {
        vars.hIntegral = 0;
        startDrive();
        startTurn();
    }

    public void startTurn()
    {
        topTurnLeft.setSelectedSensorPosition(MathFormulas.degreesToNative(topLeftCoder.getAbsolutePosition(), MKTURN.greerRatio));
        topTurnRight.setSelectedSensorPosition(MathFormulas.degreesToNative(topRightCoder.getAbsolutePosition(), MKTURN.greerRatio));
        bottomTurnLeft.setSelectedSensorPosition(MathFormulas.degreesToNative(bottomLeftCoder.getAbsolutePosition(), MKTURN.greerRatio));
        bottomTurnRight.setSelectedSensorPosition(MathFormulas.degreesToNative(bottomRightCoder.getAbsolutePosition(), MKTURN.greerRatio));
    }

    public void zeroTurn()
    {
        topTurnLeft.setSelectedSensorPosition(0);
        topTurnRight.setSelectedSensorPosition(0);
        bottomTurnLeft.setSelectedSensorPosition(0);
        bottomTurnRight.setSelectedSensorPosition(0);
    }

    public void startDrive()
    {
        topDriveLeft.setSelectedSensorPosition(0);
        topDriveRight.setSelectedSensorPosition(0);
        bottomDriveLeft.setSelectedSensorPosition(0);
        bottomDriveRight.setSelectedSensorPosition(0);
    }

    public double tlDeg()
    {
        return MathFormulas.nativeToDegrees(topTurnLeft.getSelectedSensorPosition(), MKTURN.greerRatio);
    }
    public double trDeg()
    {
        return MathFormulas.nativeToDegrees(topTurnRight.getSelectedSensorPosition(), MKTURN.greerRatio);
    }
    public double blDeg()
    {
        return MathFormulas.nativeToDegrees(bottomTurnLeft.getSelectedSensorPosition(), MKTURN.greerRatio);
    }
    public double brDeg()
    {
        return MathFormulas.nativeToDegrees(bottomTurnRight.getSelectedSensorPosition(), MKTURN.greerRatio);
    }



    public double tlCoder()
    {
        return topLeftCoder.getAbsolutePosition();
    }
    public double trCoder()
    {
        return topRightCoder.getAbsolutePosition();
    }
    public double blCoder()
    {
        return bottomLeftCoder.getAbsolutePosition();
    }
    public double brCoder()
    {
        return bottomRightCoder.getAbsolutePosition();
    }

    public void updateSwerve()
    {
        //SmartDashboard.putNumber("anglet", vars.deg[0]);
        //SmartDashboard.putNumber("distancet", vars.posInch[0]);
        /*
        SmartDashboard.putNumber("degtopleft", vars.degTL);
        SmartDashboard.putNumber("distancetopright", vars.posInchTR);
        SmartDashboard.putNumber("distancetbotleft", vars.posInchBL);
        SmartDashboard.putNumber("distancetbotright", vars.posInchBR);*/
        SmartDashboard.putNumber("topdriveleft", MathFormulas.nativeToInches(topDriveLeft.getSelectedSensorPosition()));
        SmartDashboard.putNumber("topdriveright", MathFormulas.nativeToInches(topDriveRight.getSelectedSensorPosition()));
        SmartDashboard.putNumber("botdriveleft", MathFormulas.nativeToInches(bottomDriveLeft.getSelectedSensorPosition()));
        SmartDashboard.putNumber("botdriveright", MathFormulas.nativeToInches(bottomDriveRight.getSelectedSensorPosition()));
        SmartDashboard.putNumber("avgdistinches", vars.avgDistInches);
        SmartDashboard.putNumber("navx", vars.yaw);

        /*SmartDashboard.putNumber("TopLeft", tlCoder());
        SmartDashboard.putNumber("TopRight", trCoder());
        SmartDashboard.putNumber("BottomLeft", blCoder());
        SmartDashboard.putNumber("BottomRight", brCoder());

        SmartDashboard.putNumber("tldeg", tlDeg());
        SmartDashboard.putNumber("trdeg", trDeg());
        SmartDashboard.putNumber("bldeg", blDeg());
        SmartDashboard.putNumber("brdeg", brDeg());*/

        vars.posInchTL = MathFormulas.nativeToInches(topDriveLeft.getSelectedSensorPosition());
        vars.posInchTR = MathFormulas.nativeToInches(topDriveRight.getSelectedSensorPosition());
        vars.posInchBL = MathFormulas.nativeToInches(bottomDriveLeft.getSelectedSensorPosition());
        vars.posInchBR = MathFormulas.nativeToInches(bottomDriveRight.getSelectedSensorPosition());

        vars.degTL = tlDeg();
        vars.degTR = trDeg();
        vars.degBL = blDeg();
        vars.degBR = brDeg();

        vars.avgDistInches = (Math.abs(vars.posInchTL) + Math.abs(vars.posInchTR) + Math.abs(vars.posInchBL) + Math.abs(vars.posInchBR)) /4.0;
     //vars.avgVelInches = (vars.velInch[0] + vars.velInch[1] + vars.velInch[2] + vars.velInch[3]) / 4.0;
       // vars.avgVelNative = (vars.velNative[0] + vars.velNative[1] + vars.velNative[2] + vars.velNative[3]) / 4.0;
        vars.avgDeg = (vars.degTL + vars.degTR + vars.degBL + vars.degBR) / 4.0;
    }

    public void setModuleTurn(double tl, double tr, double bl, double br)
    {
        topTurnLeft.set(ControlMode.Position, MathFormulas.degreesToNative(tl, MKTURN.greerRatio));
        topTurnRight.set(ControlMode.Position, MathFormulas.degreesToNative(tr, MKTURN.greerRatio));
        bottomTurnLeft.set(ControlMode.Position, MathFormulas.degreesToNative(bl, MKTURN.greerRatio));
        bottomTurnRight.set(ControlMode.Position, MathFormulas.degreesToNative(br, MKTURN.greerRatio));
    }

    public void setModuleDrive(ControlMode mode, double tl, double tr, double bl, double br)
    {
        topDriveLeft.set(mode, tl);
        topDriveRight.set(mode, tr);
        bottomDriveLeft.set(mode, bl);//Ben
        bottomDriveRight.set(mode, br);
    }

    public void stopEverything()
    {
        topTurnLeft.set(ControlMode.PercentOutput, 0);
        topTurnRight.set(ControlMode.PercentOutput, 0);
        bottomTurnLeft.set(ControlMode.PercentOutput, 0);
        bottomTurnRight.set(ControlMode.PercentOutput, 0);

        topDriveLeft.set(ControlMode.PercentOutput, 0);
        topDriveRight.set(ControlMode.PercentOutput, 0);
        bottomDriveLeft.set(ControlMode.PercentOutput, 0);
        bottomDriveRight.set(ControlMode.PercentOutput, 0);
    }

     /**
     * See <a href="https://www.chiefdelphi.com/t/paper-4-wheel-independent-drive-independent-steering-swerve/107383">this thread</a>
     * for more information.
     * <p>
     * Note - this function uses 180 minus yaw due to the positioning of our navx.
     * @param FWD Forward axis of controller
     * @param STR Strafe axis of controller
     * @param RCW Rotational axis of controller
     * @author Ether
     */
    public void etherSwerve(double FWD, double STR, double RCW, ControlMode mode)
    {
       /* if(RCW == 0 && (FWD != 0 || STR != 0))
        {
            RCW = headerStraighter(Math.toDegrees(Math.atan2(FWD, STR)));
        }*/
        vars.yaw = navx.getInstance().getNavxYaw();
        vars.temp = FWD * Math.cos(Math.toRadians(vars.yaw)) + STR * Math.sin(Math.toRadians(vars.yaw));
        STR = -FWD * Math.sin(Math.toRadians(vars.yaw)) + STR * Math.cos(Math.toRadians(vars.yaw));
        FWD = vars.temp;
        //SmartDashboard.putNumber("header pid", (Math.toDegrees(Math.atan2(FWD, STR))));
        //RCW = moveToAngy((((((( Math.toDegrees(Math.atan(RCWY/RCWX))+360 ))+ (MathFormulas.signumV4(RCWX)))%360) - MathFormulas.signumAngleEdition(RCWX,RCWY))+360)%360);
        SmartDashboard.putNumber("rcw", RCW);

        //SmartDashboard.putNumber("frd", FWD);
        //SmartDashboard.putNumber("str", STR);

        vars.A = STR - RCW*(MKTRAIN.L/MKTRAIN.R);
        vars.B = STR + RCW*(MKTRAIN.L/MKTRAIN.R);
        vars.C = FWD - RCW*(MKTRAIN.W/MKTRAIN.R);
        vars.D = FWD + RCW*(MKTRAIN.W/MKTRAIN.R);

        vars.mod2[1] = Math.atan2(vars.B,vars.C)*180/Constants.kPi;
        vars.mod1[1] = Math.atan2(vars.B,vars.D)*180/Constants.kPi;
        vars.mod3[1] = Math.atan2(vars.A,vars.D)*180/Constants.kPi;
        vars.mod4[1] = Math.atan2(vars.A,vars.C)*180/Constants.kPi; 

        vars.mod2[0] = Math.sqrt((Math.pow(vars.B, 2)) + (Math.pow(vars.C, 2)));      
        vars.mod1[0] = Math.sqrt((Math.pow(vars.B, 2)) + (Math.pow(vars.D, 2))); 
        vars.mod3[0] = Math.sqrt((Math.pow(vars.A, 2)) + (Math.pow(vars.D, 2)));           
        vars.mod4[0] = Math.sqrt((Math.pow(vars.A, 2)) + (Math.pow(vars.C, 2)));
        
            vars.max=vars.mod1[0]; if(vars.mod2[0]>vars.max)vars.max=vars.mod2[0]; if(vars.mod3[0]>vars.max)vars.max=vars.mod3[0]; if(vars.mod4[0]>vars.max)vars.max=vars.mod4[0];
            if(vars.max>1){vars.mod1[0]/=vars.max; vars.mod2[0]/=vars.max; vars.mod3[0]/=vars.max; vars.mod4[0]/=vars.max;}

            /*vars.mod1 = MathFormulas.optimize(topLeftModule.getTurnDeg(), vars.mod1);
            vars.mod2 = MathFormulas.optimize(topRightModule.getTurnDeg(), vars.mod2);
            vars.mod3 = MathFormulas.optimize(bottomLeftModule.getTurnDeg(), vars.mod3);
            vars.mod4 = MathFormulas.optimize(bottomRightModule.getTurnDeg(), vars.mod4);*/
    
            vars.mod1 = setDirection(tlDeg(), vars.mod1);
            vars.mod2 = setDirection(trDeg(), vars.mod2);
            vars.mod3 = setDirection(blDeg(), vars.mod3);
            vars.mod4 = setDirection(brDeg(), vars.mod4);
            
            if(mode == ControlMode.MotionMagic)
            {
                vars.mod1[0] = Math.signum(vars.mod1[0]) * vars.autoDist;
                vars.mod2[0] = Math.signum(vars.mod2[0]) * vars.autoDist;
                vars.mod3[0] = Math.signum(vars.mod3[0]) * vars.autoDist;
                vars.mod4[0] = Math.signum(vars.mod4[0]) * vars.autoDist;
            }
          /*  vars.mod1[1] = MathFormulas.setAutoDirection(topLeftModule.getTurnDeg(), vars.mod1[1]);
            vars.mod2[1] = MathFormulas.setAutoDirection(topRightModule.getTurnDeg(), vars.mod2[1]);
            vars.mod3[1] = MathFormulas.setAutoDirection(bottomLeftModule.getTurnDeg(), vars.mod3[1]);
            vars.mod4[1] = MathFormulas.setAutoDirection(bottomRightModule.getTurnDeg(), vars.mod4[1]);
            */

          /*  vars.mod1[0] = vars.autoDist;
            vars.mod2[0] = vars.autoDist;
            vars.mod3[0] = vars.autoDist;
            vars.mod4[0] = vars.autoDist;*/
        

      /*if(mode == ControlMode.MotionMagic)
        {
            vars.mod1[0] = Math.abs(vars.mod1[0]);
            vars.mod2[0] = Math.abs(vars.mod2[0]);
            vars.mod3[0] = Math.abs(vars.mod3[0]);
            vars.mod4[0] = Math.abs(vars.mod4[0]);
        }*/
        SmartDashboard.putNumber("wa1", vars.mod1[1]);
        setModuleDrive(mode, vars.mod1[0], vars.mod2[0], vars.mod3[0], vars.mod4[0]);
        setModuleTurn(vars.mod1[1], vars.mod2[1], vars.mod3[1], vars.mod4[1]);
    }











    public void etherAutoSwerve(double FWD, double STR, double RCW, ControlMode mode)
    {
       /* if(RCW == 0 && (FWD != 0 || STR != 0))
        {
            RCW = headerStraighter(Math.toDegrees(Math.atan2(FWD, STR)));
        }*/
        vars.yaw = 0;
        vars.temp = FWD * Math.cos(Math.toRadians(vars.yaw)) + STR * Math.sin(Math.toRadians(vars.yaw));
        STR = -FWD * Math.sin(Math.toRadians(vars.yaw)) + STR * Math.cos(Math.toRadians(vars.yaw));
        FWD = vars.temp;
        //SmartDashboard.putNumber("header pid", (Math.toDegrees(Math.atan2(FWD, STR))));
        //RCW = moveToAngy((((((( Math.toDegrees(Math.atan(RCWY/RCWX))+360 ))+ (MathFormulas.signumV4(RCWX)))%360) - MathFormulas.signumAngleEdition(RCWX,RCWY))+360)%360);
        SmartDashboard.putNumber("rcw", RCW);

        //SmartDashboard.putNumber("frd", FWD);
        //SmartDashboard.putNumber("str", STR);

        vars.A = STR - RCW*(MKTRAIN.L/MKTRAIN.R);
        vars.B = STR + RCW*(MKTRAIN.L/MKTRAIN.R);
        vars.C = FWD - RCW*(MKTRAIN.W/MKTRAIN.R);
        vars.D = FWD + RCW*(MKTRAIN.W/MKTRAIN.R);

        vars.mod2[1] = Math.atan2(vars.B,vars.C)*180/Constants.kPi;
        vars.mod1[1] = Math.atan2(vars.B,vars.D)*180/Constants.kPi;
        vars.mod3[1] = Math.atan2(vars.A,vars.D)*180/Constants.kPi;
        vars.mod4[1] = Math.atan2(vars.A,vars.C)*180/Constants.kPi; 

        vars.mod2[0] = Math.sqrt((Math.pow(vars.B, 2)) + (Math.pow(vars.C, 2)));      
        vars.mod1[0] = Math.sqrt((Math.pow(vars.B, 2)) + (Math.pow(vars.D, 2))); 
        vars.mod3[0] = Math.sqrt((Math.pow(vars.A, 2)) + (Math.pow(vars.D, 2)));           
        vars.mod4[0] = Math.sqrt((Math.pow(vars.A, 2)) + (Math.pow(vars.C, 2)));
        
            vars.max=vars.mod1[0]; if(vars.mod2[0]>vars.max)vars.max=vars.mod2[0]; if(vars.mod3[0]>vars.max)vars.max=vars.mod3[0]; if(vars.mod4[0]>vars.max)vars.max=vars.mod4[0];
            if(vars.max>1){vars.mod1[0]/=vars.max; vars.mod2[0]/=vars.max; vars.mod3[0]/=vars.max; vars.mod4[0]/=vars.max;}

            /*vars.mod1 = MathFormulas.optimize(topLeftModule.getTurnDeg(), vars.mod1);
            vars.mod2 = MathFormulas.optimize(topRightModule.getTurnDeg(), vars.mod2);
            vars.mod3 = MathFormulas.optimize(bottomLeftModule.getTurnDeg(), vars.mod3);
            vars.mod4 = MathFormulas.optimize(bottomRightModule.getTurnDeg(), vars.mod4);*/
    /* 
            vars.mod1 = setDirection(tlDeg(), vars.mod1);
            vars.mod2 = setDirection(trDeg(), vars.mod2);
            vars.mod3 = setDirection(blDeg(), vars.mod3);
            vars.mod4 = setDirection(brDeg(), vars.mod4);
    */        
            if(mode == ControlMode.MotionMagic)
            {
                vars.mod1[0] = Math.signum(vars.mod1[0]) * vars.autoDist;
                vars.mod2[0] = Math.signum(vars.mod2[0]) * vars.autoDist;
                vars.mod3[0] = Math.signum(vars.mod3[0]) * vars.autoDist;
                vars.mod4[0] = Math.signum(vars.mod4[0]) * vars.autoDist;
            }
          /*  vars.mod1[1] = MathFormulas.setAutoDirection(topLeftModule.getTurnDeg(), vars.mod1[1]);
            vars.mod2[1] = MathFormulas.setAutoDirection(topRightModule.getTurnDeg(), vars.mod2[1]);
            vars.mod3[1] = MathFormulas.setAutoDirection(bottomLeftModule.getTurnDeg(), vars.mod3[1]);
            vars.mod4[1] = MathFormulas.setAutoDirection(bottomRightModule.getTurnDeg(), vars.mod4[1]);
            */

          /*  vars.mod1[0] = vars.autoDist;
            vars.mod2[0] = vars.autoDist;
            vars.mod3[0] = vars.autoDist;
            vars.mod4[0] = vars.autoDist;*/
        

      /*if(mode == ControlMode.MotionMagic)
        {
            vars.mod1[0] = Math.abs(vars.mod1[0]);
            vars.mod2[0] = Math.abs(vars.mod2[0]);
            vars.mod3[0] = Math.abs(vars.mod3[0]);
            vars.mod4[0] = Math.abs(vars.mod4[0]);
        }*/
        //SmartDashboard.putNumber("wa1", vars.mod1[1]);
        setModuleDrive(mode, vars.mod1[0], vars.mod2[0], vars.mod3[0], vars.mod4[0]);
        setModuleTurn(vars.mod1[1], vars.mod2[1], vars.mod3[1], vars.mod4[1]);
    }














    public double moveToAngy(double setpoint)
    {        
        vars.yaw = navx.getInstance().getNavxYaw();
        //TrapezoidProfile.State setpointState = new TrapezoidProfile.State();
                //SmartDashboard.putNumber("angleclosest", setDirectionAuto(vars.yaw, setpoint));
        //TrapezoidProfile turnProfile = new TrapezoidProfile(constraints, new TrapezoidProfile.State(vars.yaw,0));
        //setpointState = turnProfile.calculate(0.02);
        setpoint = turn.calculate(Math.abs(vars.yaw), Math.abs(setpoint));
        //SmartDashboard.putNumber("setpointbefore", setpoint);

        // double feedforward = ((1.0) / (VISION.kMaxAimAngularVel)) * trap.calculate(Constants.kDt).velocity;
   //   SmartDashboard.putNumber("feedforward", feedforward); 
        setpoint = MathFormulas.limitAbsolute(setpoint, 0.3);
        //SmartDashboard.putNumber("turnvelpcityprofile", setpointState.velocity);
        //SmartDashboard.putNumber("turnpositionprofile", setpointState.position);
return setpoint;
        //etherSwerve(0, 0, setpoint, ControlMode.PercentOutput);
    }



   /**
     * decides whether a driving motor should flip based on where the angular motor's setpoint is.
     * @param position position of the motor
     * @param setpoint setpoint for the motor
     * @return returns best angle of travel for the angular motor, as well as the flip value for the driving motor (as an array so it can return two things in one instead of two seperatly)
     * @author team 6624
     */
    public static double[] setDirection(double pos, double[] mod)
    {
        double currentAngle = pos;
        // find closest angle to setpoint
        double setpointAngle = MathFormulas.closestAngle(currentAngle, mod[1]);
        // find closest angle to setpoint + 180
        double setpointAngleFlipped = MathFormulas.closestAngle(currentAngle, mod[1] + 180.0);
        // if the closest angle to setpoint is shorter
        if (Math.abs(setpointAngle) <= Math.abs(setpointAngleFlipped))
        {
            // unflip the motor direction use the setpoint
            //an = currentAngle + setpointAngle;

            return new double[] {mod[0],(currentAngle + setpointAngle)};
        }
        // if the closest angle to setpoint + 180 is shorter
        else
        {
            // flip the motor direction and use the setpoint + 180
            //di = Math.abs(di) * -1.0; 
            //an = currentAngle + setpointAngleFlipped;
            return new double[] {Math.abs(mod[0]) * -1,(currentAngle + setpointAngleFlipped)};
        }
    }












    /**
     * decides whether a driving motor should flip based on where the angular motor's setpoint is.
     * @param position position of the motor
     * @param setpoint setpoint for the motor
     * @return returns best angle of travel for the angular motor, as well as the flip value for the driving motor (as an array so it can return two things in one instead of two seperatly)
     * @author team 6624
     */
    public static double setDirectionAuto(double pos, double setpoint)
    {
        // find closest angle to setpoint
        if (MathFormulas.closestAngle(pos, setpoint) < 90)
        {
            // unflip the motor direction use the setpoint
            //an = currentAngle + setpointAngle;
            return setpoint;
        }
        // if the closest angle to setpoint + 180 is shorter
        else
        {
            // flip the motor direction and use the setpoint + 180
            //di = Math.abs(di) * -1.0; 
            //an = currentAngle + setpointAngleFlipped;
            return -(setpoint);
        }
   
    }











    //programming done right
    public double headerStraighter(double hSetpoint)
    {
            vars.hError = hSetpoint -  navx.getInstance().getNavxYaw();// Error = Target - Actual
            vars.hIntegral += (vars.hError*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
            vars.hDerivative = (vars.hError - vars.hPreviousError) / .02;
            return vars.hP*vars.hError + vars.hI*vars.hIntegral + vars.hD*vars.hDerivative;
    }

    //turn distance is degrees

    public void setEtherAuto(double totalDistance, double distanceA)
    {
        //startDrive();
        vars.autoDist = MathFormulas.inchesToNative(totalDistance);
        vars.totalDistance = totalDistance;
        vars.avgDistInches = 0;
        vars.distanceA = distanceA;
    }

    /**
     * Using the {@link #swerveAutonomousEther} and motion magic, an autonomous angled path of motion can be achieved
     * @param totalDistance Length of curved path
     * @param thetaTurn Angle of curved path
     * @param RCWauto [-1, 1] For spinny, 0 for no spinny
     * @param mode Curve or Straight
     * @param turny Specific or Infinite
     * @param turnyAuto (if using specific for turny) angle that robot tries to keep when moving
    */
    /*public void etherAutoUpdateOld(double thetaTurn, double RCWauto, ETHERAUTO mode, ETHERRCW turny, double turnyAuto, double heading, int side)
    {
                                            //numbers fall short of high by 3ish inches and short of length by 4ish inches
        double RCWtemp = RCWauto; //50,10 = 15 ... 40,10 = 10 ... 30,10 = 5 ... 20,10 = 0 <-- (even if just circle, 4 inches from height but hits target)
                                                                            //minus subtracotr
        double calcangle = ((heading) + (side * ((thetaTurn/2)+((vars.avgDistInches/(vars.distanceA))*(thetaTurn)))));
        if(mode == ETHERAUTO.Curve)
        {
            vars.FWDauto = Math.cos(calcangle* (Constants.kPi/180));//(90-(thetaTurn/2))+((vars.avgDistInches/vars.totalDistance)*(thetaTurn)) * (Constants.kPi/180));//(((-1 * thetaTurn) + (2 * ((vars.avgDistInches/vars.totalDistance)*thetaTurn))) * Constants.kPi / 180);
            vars.STRauto = Math.sin(calcangle* (Constants.kPi/180));//(90-(thetaTurn/2))+((vars.avgDistInches/vars.totalDistance)*(thetaTurn)) * (Constants.kPi/180));//(((-1 * thetaTurn) + (2 * ((vars.avgDistInches/vars.totalDistance)*thetaTurn))) * Constants.kPi / 180);
        }
        else if(mode == ETHERAUTO.Straight)
        {
            vars.FWDauto = Math.cos(thetaTurn* (Constants.kPi/180));
            vars.STRauto = Math.sin(thetaTurn* (Constants.kPi/180));
        }
        if(turny == ETHERRCW.Specific)
        {
            RCWtemp = headerStraighter(turnyAuto);
        }
        etherSwerve(vars.FWDauto, -vars.STRauto, RCWtemp, ControlMode.MotionMagic);
    }*/


    public void etherAutoUpdate(double thetaTurn, double heading, int side)
    {
                                            //numbers fall short of high by 3ish inches and short of length by 4ish inches
        double RCWtemp = 0; //50,10 = 15 ... 40,10 = 10 ... 30,10 = 5 ... 20,10 = 0 <-- (even if just circle, 4 inches from height but hits target)
                                                                            //minus subtracotr
        double calcangle = ((heading) + (side * ((thetaTurn/2)+((vars.avgDistInches/(vars.distanceA))*(thetaTurn)))));
        vars.FWDauto = Math.cos(calcangle* (Constants.kPi/180));//(90-(thetaTurn/2))+((vars.avgDistInches/vars.totalDistance)*(thetaTurn)) * (Constants.kPi/180));//(((-1 * thetaTurn) + (2 * ((vars.avgDistInches/vars.totalDistance)*thetaTurn))) * Constants.kPi / 180);
        vars.STRauto = Math.sin(calcangle* (Constants.kPi/180));//(90-(thetaTurn/2))+((vars.avgDistInches/vars.totalDistance)*(thetaTurn)) * (Constants.kPi/180));//(((-1 * thetaTurn) + (2 * ((vars.avgDistInches/vars.totalDistance)*thetaTurn))) * Constants.kPi / 180);
        etherAutoSwerve(vars.FWDauto, -vars.STRauto, RCWtemp, ControlMode.MotionMagic);
        /*SmartDashboard.putNumber("heading", heading);
        SmartDashboard.putNumber("side", side);
        SmartDashboard.putNumber("thetaTurn", thetaTurn);
        SmartDashboard.putNumber("avgDist", vars.avgDistInches);
        SmartDashboard.putNumber("distA", vars.distanceA);
        SmartDashboard.putNumber("calcangle", calcangle);
        SmartDashboard.putNumber("FWDauto", vars.FWDauto);
        SmartDashboard.putNumber("STRauto", vars.STRauto);*/
        SmartDashboard.putNumber("calcangle", calcangle);

        if(heading > 0 && side > 0)
        {
            SmartDashboard.putBoolean("1", true);
            SmartDashboard.putBoolean("2", false);
            SmartDashboard.putBoolean("3", false);
            SmartDashboard.putBoolean("4", false);
        }

        else if(heading < 0 && side > 0)
        {
            SmartDashboard.putBoolean("1", false);
            SmartDashboard.putBoolean("2", true);
            SmartDashboard.putBoolean("3", false);
            SmartDashboard.putBoolean("4", false);
        }

        else if(heading > 0 && side < 0)
        {
            SmartDashboard.putBoolean("1", false);
            SmartDashboard.putBoolean("2", false);
            SmartDashboard.putBoolean("3", true);
            SmartDashboard.putBoolean("4", false);
        }

        else
        {
            SmartDashboard.putBoolean("1", false);
            SmartDashboard.putBoolean("2", false);
            SmartDashboard.putBoolean("3", false);
            SmartDashboard.putBoolean("4", true);
        }
    }

    public boolean isFinished()
    {
        return Math.abs(vars.avgDistInches) >= Math.abs(vars.totalDistance) - 0.1;
    }

    /**Mode of the ether auto's path*/
    public enum ETHERAUTO
    {
        Straight, Curve
    }

    /**Mode of the ether auto's turn */
    public enum ETHERRCW
    {
        Specific, Forever
    }

    public enum MODE
    {
        auto, tele;
    }

    private static class InstanceHolder
    {
        private static final MkSwerveTrain mInstance = new MkSwerveTrain();
    } 

    public static class variables
    {

        public double heading;
        public int side;
        public double distanceA;
        public double STRauto;
        public double FWDauto;
    public double totalDistance;

        public double temp;
        public double yaw;
        public double A;
        public double B;
        public double C;
        public double D;
        public double[] mod1;
        public double[] mod2;
        public double[] mod3;
        public double[] mod4;
        public double max;

        /**Distance variable for driving in autonomous*/
        public double straightDistance;

    /**Position of the driving motor in native units*/
    public double posNativeTL, posNativeTR, posNativeBL, posNativeBR;
       
    /**Position of the driving motor in inches*/
    public double posInchTL, posInchTR, posInchBL, posInchBR;
   
    /**Position of the driving motor in meters*/
    public double posMetersTL, posMetersTR, posMetersBL, posMetersBR;
       
    /**Velocity of the driving motor in inches*/
    public double velInchTL, velInchTR, velInchBL, velInchBR;
   
    /**Velocity of the driving motor in native units*/
    public double velNativeTL, velNativeTR, velNativeBL, velNativeBR;
       
    /**Velocity of the driving motor in meters*/
    public double velMetersTL, velMetersTR, velMetersBL, velMetersBR;
   
    /**Position of the turning motor in degrees*/
    public double degTL, degTR, degBL, degBR;
   
    /**Driving motor values for autonomous*/
    //public double[] output;
   
    /**Average velocity of driving motors in inches*/
    public double avgVelInches;
   
    /**Average velocity of driving motors in native units*/
    public double avgVelNative;
   
    /**Average distance of driving motors in inches*/
    public double avgDistInches;

    public double avgDeg;

    public variables var;
    //three degrees error babeeeee!!!!
    public double hP = 0.017, hI = hP * 0, hD = hP * 0.10; //0.03i, 0.01d
                   //  0.015
    public double hIntegral, hDerivative, hPreviousError, hError;
//code
    public double autoDist;

    public double autoDirectionTL;
    public double autoDirectionTR;
    public double autoDirectionBL;
    public double autoDirectionBR;

    public double yawTemp;

    }
}

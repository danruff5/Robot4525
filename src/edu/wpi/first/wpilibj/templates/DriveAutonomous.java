package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

public class DriveAutonomous {
     public static double delay = 0.01;
     
     public static void Stright(int driveStrightTime, boolean moveLift, boolean directionReverse) {
            for (double k = 0.0; k < driveStrightTime + delay; k += delay) {
                if (directionReverse) {
                    Robot.DriveLeft.set(-1.0);
                    Robot.DriveRight.set(-1.0);
                } else {
                    Robot.DriveLeft.set(1.0);
                    Robot.DriveRight.set(1.0);
                }
                if (moveLift) {
                    if (Robot.switchDown.get()) {
                        Robot.Lift.set(-1.0);
                    } else {
                        Robot.Lift.set(0.0);
                    }
                } else {
                    Robot.Lift.set(0.0);
                }
                Timer.delay(delay);
            }
            
     }
     
     public static void Turn (int turnTime, boolean moveLift, boolean turnRight) {
          for (double k = 0.0; k < turnTime + delay; k += delay) {
              if (turnRight) {
                  Robot.DriveLeft.set(1.0);
                  Robot.DriveRight.set(-1.0);
              } else {
                  Robot.DriveLeft.set(-1.0);
                  Robot.DriveRight.set(1.0);
              }
              
              if (moveLift) {
                    if (Robot.switchDown.get()) {
                        Robot.Lift.set(-1.0);
                    } else {
                        Robot.Lift.set(0.0);
                    }
                } else {
                    Robot.Lift.set(0.0);
                }
                Timer.delay(delay); 
          }
     }
     
     public static void Shoot (double warmUp, double betweenShoot, int numberShoot) {
         Robot.Shooter1.set(-1.0);
         Robot.Shooter2.set(-1.0);
         Timer.delay(warmUp - betweenShoot);
         for (int shootTimes = numberShoot; shootTimes != 0; shootTimes --) {
            Timer.delay(betweenShoot);
            Robot.Pin.set(Relay.Value.kReverse);
            Timer.delay(0.05);
            Robot.Pin.set(Relay.Value.kOff);
            Timer.delay(0.5);
            Robot.Pin.set(Relay.Value.kForward);
            Timer.delay(0.05);
            Robot.Pin.set(Relay.Value.kOff);
         }
         Timer.delay(delay);
         Robot.Shooter1.set(0.0);
         Robot.Shooter2.set(0.0);
     }
     
     public static void moveLift(boolean liftUP) {
         if (liftUP) {
             while (Robot.switchUp.get()) {
                 Robot.Lift.set(1.0);
                 Timer.delay(delay);
             }
         } else {
             while (Robot.switchDown.get()) {
                 Robot.Lift.set(-1.0);
                 Timer.delay(delay);
             }
         }
     }
}

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Robot extends SimpleRobot {
    
    public static Victor DriveLeft = new Victor(1);
    public static Victor DriveRight = new Victor(2);
    public static Victor Shooter1 = new Victor(3);
    public static Victor Shooter2 = new Victor(4);
    public static Victor Lift = new Victor(5);
    public static Victor Belt = new Victor(6);
    public static Jaguar Wheel = new Jaguar(7);
    
    public static Joystick drivestick = new Joystick(1);
    public static Joystick shootstick = new Joystick(2);
    
    public static DigitalInput switchDown = new DigitalInput(12);
    public static DigitalInput switchUp = new DigitalInput(14);
    
    public static Relay Pin = new Relay(1);
    
    public static boolean PinToggle = false;
    public static boolean beltToggle = false;
    public static boolean firstTick = false;
    
    public void autonomous() {
        int StartPos = StartPosition.FRONT_LEFT; // position relative to the target
        
        if (StartPos == StartPosition.FRONT_LEFT) { // drive to dump and dump while lowering lift
            DriveAutonomous.Stright(2, true, false);
            DriveAutonomous.Turn(2, true, true);
            DriveAutonomous.Stright(5, true, true);
            DriveAutonomous.Turn(1, true, false);
            Belt.set(0.5);
            Wheel.set(-1.0); 
        } else if (StartPos == StartPosition.FRONT_RIGHT) { // drive to dump and dump while lowering lift
            DriveAutonomous.Stright(1, true, false);
            DriveAutonomous.Turn(1, true, false);
            DriveAutonomous.Stright(2, true, false);
            Belt.set(0.5);
            Wheel.set(-1.0);
        } else if (StartPos == StartPosition.REAR_LEFT) { // stop to lower lift then shoot then drive under tower and pick up
            DriveAutonomous.moveLift(false);
            DriveAutonomous.Shoot(2.0, 1.5, 3);
            DriveAutonomous.Stright(1, false, false);
            DriveAutonomous.Turn(2, false, true);
            DriveAutonomous.Stright(5, false, false);
            DriveAutonomous.Turn(2, false, true);
        } else if (StartPos == StartPosition.REAR_RIGHT) { // stop to lower lift then shoot then drive under tower and pick up
           DriveAutonomous.moveLift(false);
           DriveAutonomous.Shoot(2.0, 1.5, 3);
           DriveAutonomous.Stright(1, false, false);
           DriveAutonomous.Turn(2, false, false);
           DriveAutonomous.Stright(5, false, false);
           DriveAutonomous.Turn(2, false, true);
        }
    }

    public void operatorControl() {
        int togglePin = ToggleStatePin.UP;
        int toggleBelt = ToggleStateBelt.UP;
        
        while (isOperatorControl() && isEnabled()) {
            if (!firstTick) {
                firstTick = true;
                Pin.set(Relay.Value.kForward);
                Timer.delay(0.05);
                Pin.set(Relay.Value.kOff);
            }
            
            double X = drivestick.getX();
            double Y = drivestick.getY();
            DriveLeft.set((Y - X) * -0.99); 
            DriveRight.set((Y + X) * 0.99);
            
            if (shootstick.getRawButton(3)) {
                Shooter1.set(-1.0);
                Shooter2.set(-1.0);
            } else {
                Shooter1.set(0.0);
                Shooter2.set(0.0);
            }
            
            if (!beltToggle) {
                if (shootstick.getRawButton(6)) {
                    beltToggle = true;
                    if (toggleBelt == ToggleStateBelt.UP) {
                        toggleBelt = ToggleStateBelt.DOWN;
                        Belt.set(0.5);
                        Wheel.set(-1.0); 
                    } else if (toggleBelt == ToggleStateBelt.DOWN) {
                        toggleBelt = ToggleStateBelt.UP;
                        Belt.set(0.0);
                        Wheel.set(0.0);
                    }
                }
            }
            if (beltToggle) {
                if (!shootstick.getRawButton(6)) {
                    beltToggle = false;
                }
            }
            
            
            if (shootstick.getRawButton(7) && switchUp.get()) {
                Lift.set(1.0);
            } else if (shootstick.getRawButton(10) && switchDown.get()) {
                Lift.set(-1.0);
            } else {
                Lift.set(0.0);
            }
          
           if (!PinToggle)  {
                if (shootstick.getTrigger()) {
                    PinToggle = true;
                    if (togglePin == ToggleStatePin.DOWN) {
                        Pin.set(Relay.Value.kForward);
                        Timer.delay(0.05);
                        Pin.set(Relay.Value.kOff);
                        togglePin = ToggleStatePin.UP;
                    } else if (togglePin == ToggleStatePin.UP) {
                        Pin.set(Relay.Value.kReverse);
                        Timer.delay(0.05);
                        Pin.set(Relay.Value.kOff);
                        togglePin = ToggleStatePin.DOWN;
                        Timer.delay(0.5);
                        Pin.set(Relay.Value.kForward);
                        Timer.delay(0.05);
                        Pin.set(Relay.Value.kOff);
                        togglePin = ToggleStatePin.UP;
                        
                    } else {
                        Pin.set(Relay.Value.kOff);
                    }
                }
           }
           if (PinToggle) {
               if (!shootstick.getTrigger()) {
                   PinToggle = false;
               }
           }
                      
            Timer.delay(0.01); // more for total when pin is being used
        }
    }
    
    public void test() {
    }
    
}
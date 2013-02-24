package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;



public class Robot extends SimpleRobot {
    
    Victor Drive1 = new Victor(1);
    Victor Drive2 = new Victor(2);
    Victor Shooter1 = new Victor(3);
    Victor Shooter2 = new Victor(4);
    Victor Lift = new Victor(5);
    Victor Belt = new Victor(6);
    Jaguar Wheel = new Jaguar(7);
    
    Joystick drivestick = new Joystick(1);
    Joystick shootstick = new Joystick(2);
    
    DigitalInput switchDown = new DigitalInput(12);
    DigitalInput switchUp = new DigitalInput(14);
    
    Relay Pin = new Relay(1);
    
    boolean PinToggle = false;
    boolean beltToggle = false;
    boolean firstTick = false;
    
    public void autonomous() {
        int driveSpeed = 1; // # of seconds for 1 foot movement
        String StartPos = "Left";
        
        if (StartPos.equals("Left")) {
            //dump
            
        } else if (StartPos.equals("Right")) {
            //dump
        } else if (StartPos.equals("Centre")) {
            //shoot
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
            Drive1.set((Y - X) * -0.99); 
            Drive2.set((Y + X) * 0.99);
            
            if (shootstick.getRawButton(3)) {
                Shooter1.set(-1.0);
                Shooter2.set(-1.0);
            } else {
                Shooter1.set(0.0);
                Shooter2.set(0.0);
            }
            
            if (!beltToggle) {
                if (shootstick.getRawButton(6)) {
                    if (toggleBelt == ToggleStateBelt.UP) {
                        beltToggle = true;
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
                      
            Timer.delay(0.01); // 0.03
        }
    }
    
    public void test() {
    }
    
}
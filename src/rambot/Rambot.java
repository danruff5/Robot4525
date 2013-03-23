package rambot;


import edu.wpi.first.wpilibj.*;
//Driverstation

public class Rambot extends SimpleRobot {

    // ------ MOTOR OUTPUTS ----
    private Victor driveLeft;
    private Victor driveRight;
    private Victor shooter1;
    private Victor shooter2;
    private Victor lift;
    private Victor belt;
    private Victor wheel;
    
    private Relay pin;
    
    
    // ------- DRIVER CONTROL ---
    private Joystick driverStick;
    private Joystick shootStick;
    private DriverStation ds;
    
    // ---- SENSORS -------
    private DigitalInput switchDown;
    private DigitalInput switchUp;
    
    // ------- CONTROL ----------
    int pinC; // for the pin movement but allowing program to not stop (pin down time)
    boolean pinStp;
    
    public void robotInit() {
        this.driveLeft = new Victor(1);
        this.driveRight = new Victor(2);
        this.shooter1 = new Victor(3);
        this.shooter2 = new Victor(4);
        this.lift = new Victor(5);
        this.belt = new Victor(6);
        this.wheel = new Victor(7);
        
        this.pin = new Relay(1);
        
        this.switchDown = new DigitalInput(14);
        this.switchUp = new DigitalInput(12);
        
        this.driverStick = new Joystick(1);
        this.shootStick = new Joystick(2);

        this.ds = DriverStation.getInstance();
        
        this.pinC = 0;
        this.pinStp = false;
    }
    
    public void autonomous() { // called once in autonomous period
        /*if (ds.getDigitalIn(1) == false){
            //x code here
            System.out.println("test for 1");
        }
        if (ds.getDigitalIn(2) == false){
            System.out.println("test for 2");
        }else{
            
        }*/
        while (this.switchDown.get()) {
            // lower lift all the way
            this.lift.set(1.0);
        }
        // warm up shooter wheels
        this.shooter1.set(-1.0);
        this.shooter2.set(-1.0);
        Timer.delay(1.5);
        for (int i = 0; i < 3; i ++) {
            // shoot using pin to allow disc to go into wheel
            this.pin.set(Relay.Value.kReverse);
            this.pin.set(Relay.Value.kOff);
            Timer.delay(0.5);
            this.pin.set(Relay.Value.kForward);
            this.pin.set(Relay.Value.kOff);
            Timer.delay(2.0);
        }
        
    }
    
    public void operatorControl() { // called once in teloperated period
        
        
        while(isOperatorControl() && isEnabled()) {
            // ---- DRIVE -----------
            double xVal = this.driverStick.getX();
            double yVal = this.driverStick.getY();
            this.driveLeft.set((yVal - xVal) * -0.99);
            this.driveRight.set((yVal + xVal) * 0.99);
            
            
            // ---- SHOOTER ---------
            if(this.shootStick.getRawButton(3)) {
                // on
                this.shooter1.set(-1.0);
                this.shooter2.set(-1.0);
            } else {
                // off
                this.shooter1.set(0.0);
                this.shooter2.set(0.0);
            }
            
            // ---- INTAKE --------
            if(this.shootStick.getRawButton(6)) {
                // turn it on
                this.belt.set(1.0);
                this.wheel.set(-1.0);
            } else if(this.shootStick.getRawButton(7)) {
                // turn it off
                this.belt.set(0.0);
                this.wheel.set(0.0);
            }
            
            // --- FIRE PIN -----
            if(this.shootStick.getTrigger()) {
                // pin down
                this.pin.set(Relay.Value.kReverse);
                this.pin.set(Relay.Value.kOff);
                this.pinStp = true;
            }
            if (this.pinStp) {
                // counter for time to pin up
                this.pinC += 1;
            }
            if (this.pinC >= 50) {
                // pin back up
                this.pinStp = false;
                this.pinC = 0;
                this.pin.set(Relay.Value.kForward);
                this.pin.set(Relay.Value.kOff);
            }
            
            // --------- SHOOTER --------
            if(this.shootStick.getRawButton(2)) {
                this.pin.set(Relay.Value.kReverse);
                Timer.delay(0.05);
                this.pin.set(Relay.Value.kOff);
            }
            
            // ------- LIFT -------
            if(this.shootStick.getRawButton(11)) { // && this.switchUp.get()) {
                // lift move up
                this.lift.set(1.0);
            } else if(this.shootStick.getRawButton(10) && this.switchDown.get()) {
                // lift move down
                this.lift.set(-1.0);
            } else {
                // lift not move
                this.lift.set(0.0);
            }
            
        }

    }
    
    public void test() { // called once in test period
    
    }
}

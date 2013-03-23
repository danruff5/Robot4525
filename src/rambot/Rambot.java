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
    private DriverStation driverStn;
    
    // ---- SENSORS -------
    private DigitalInput switchDown;
    private DigitalInput switchUp;
    
    // ------- CONTROL ----------
    int pinC; // for the pin movement but allowing program to not stop (pin down time)
    boolean pinStp;
    private double pinDelay; // delay for the actuator to respond
    
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

        this.driverStn = DriverStation.getInstance();
        
        this.pinC = 0;
        this.pinStp = false;
        this.pinDelay = 0.01;
    }
    
    public void autonomous() { // called once in autonomous period
        if (this.driverStn.getDigitalIn(1)){ // switch #1
            
            // --------- LOWER LIFT ---------
            System.out.println("--- Rambot is Lowering Lift ---");
            while (this.switchDown.get()) {
                this.lift.set(-1.0);
                if (!this.driverStn.isAutonomous()) {
                    break;
                }
                
            }
            this.lift.set(0.0);
            
        } else if (this.driverStn.getDigitalIn(2)) { // switch #2
            
            // --------- SHOOT WITH LIFT UP ------
            System.out.println("--- Rambot is Shooting ---");
            // warm up shooter wheels
            this.shooter1.set(-1.0);
            this.shooter2.set(-1.0);
            Timer.delay(1.5);
            for (int i = 0; i < 2; i ++) {
                // shoot using pin to allow disc to go into wheel
                this.pin.set(Relay.Value.kReverse);
                this.pin.set(Relay.Value.kOff);
                Timer.delay(0.5);
                this.pin.set(Relay.Value.kForward);
                this.pin.set(Relay.Value.kOff);
                Timer.delay(2.0);
            }
            
        } else {
            Timer.delay(14.5);
        }
        
        
       
        
    }
    
    public void operatorControl() { // called once in teloperated period
        
        this.pin.set(Relay.Value.kForward);
        Timer.delay(this.pinDelay);        
        this.pin.set(Relay.Value.kOff);
        
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
                Timer.delay(this.pinDelay);
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
                Timer.delay(this.pinDelay);
                this.pin.set(Relay.Value.kOff);
            }
            
            // --------- WALL LOADING --------
            if (this.shootStick.getRawButton(2)) {
                // make pin retract
                this.pin.set(Relay.Value.kReverse);
                Timer.delay(this.pinDelay);
                this.pin.set(Relay.Value.kOff);
                while (this.shootStick.getRawButton(2)) { // no drive control
                    // reverse shoot wheels
                    this.shooter1.set(0.5);
                    this.shooter2.set(0.5);
                    // reverse belt
                    this.belt.set(-1.0);
                }
                // make pin extend
                this.pin.set(Relay.Value.kForward);
                Timer.delay(this.pinDelay);
                this.pin.set(Relay.Value.kOff);
            }
            
            // ------- LIFT -------
            if(this.shootStick.getRawButton(11) && this.switchUp.get()) {
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

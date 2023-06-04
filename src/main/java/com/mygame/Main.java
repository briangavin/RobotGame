package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;

public class Main extends SimpleApplication implements ActionListener {

    private BulletAppState m_bulletAppState;
    private final float accelerationForce = 2000.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    final private Vector3f jumpForce = new Vector3f(0, 3000, 0);
    
    private Robot m_RobotCyber;
    private Robot m_RobotBudget;
    
    private RobotGameGraphics m_GameGraphics;

    public static void main(String[] args) {
        Main app = new Main();
        
        app.setSettings(createGameSettings());
        app.setDisplayStatView(false);
        app.start();
    }
    
    static AppSettings createGameSettings(){
        AppSettings settings = new AppSettings(true);
        settings.setFullscreen(false);
        settings.setSamples(2);
        settings.setWidth(1920);
        settings.setHeight(1080);
        
        return settings;
    }

    @Override
    public void simpleInitApp() {
        m_bulletAppState = new BulletAppState();
       
        stateManager.attach(m_bulletAppState);
       
        // Enable to see all physics boxes
        //m_bulletAppState.setDebugEnabled(true);
        setupKeys();
        
        m_GameGraphics = new RobotGameGraphics();
        m_GameGraphics.createField(this, getPhysicsSpace(),120.0f,80.0f,10.0f);
        m_GameGraphics.createEnviromentLowQuality(this);
        
        m_RobotCyber = new Robot(this, getPhysicsSpace(), new Vector3f(0,0,-30),0.0f, Alliance.BLUE);
        m_RobotBudget = new Robot(this, getPhysicsSpace(), new Vector3f(0,0,30),FastMath.PI, Alliance.RED);
        
        Vector3f loc = new Vector3f(0.0f,10.0f,0.0f);
        Vector3f loc2 = new Vector3f(-50.0f,30.0f,0.0f);
        this.cam.setLocation(loc2);
        cam.lookAt(loc,Vector3f.UNIT_Y);

    }

    private PhysicsSpace getPhysicsSpace(){
        return m_bulletAppState.getPhysicsSpace();
    }

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
    }


    @Override
    public void simpleUpdate(float tpf) {
        if( m_RobotCyber!=null)
            m_RobotCyber.simpleUpdate(tpf);
        
        if( m_RobotBudget!=null)
            m_RobotBudget.simpleUpdate(tpf);

        cam.lookAt(m_RobotCyber.GetVehicleControl().getPhysicsLocation(), Vector3f.UNIT_Y);
        
        m_GameGraphics.simpleUpdate(tpf);
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += .5f;
            } else {
                steeringValue -= .5f;
            }
            m_RobotCyber.GetVehicleControl().steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue -= .5f;
            } else {
                steeringValue += .5f;
            }
           m_RobotCyber.GetVehicleControl().steer(steeringValue);
        } else if (binding.equals("Ups")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            m_RobotCyber.GetVehicleControl().accelerate(0,accelerationValue);
            m_RobotCyber.GetVehicleControl().accelerate(1,accelerationValue);
            m_RobotCyber.GetVehicleControl().accelerate(2,accelerationValue);
            m_RobotCyber.GetVehicleControl().accelerate(3,accelerationValue);

        } else if (binding.equals("Downs")) {
            if (value) {
                accelerationValue -= accelerationForce;
            } else {
                accelerationValue += accelerationForce;
            }
            //vehicle.accelerate(accelerationValue);           
            m_RobotCyber.GetVehicleControl().accelerate(0,accelerationValue);
            m_RobotCyber.GetVehicleControl().accelerate(1,accelerationValue);
            m_RobotCyber.GetVehicleControl().accelerate(2,accelerationValue);
            m_RobotCyber.GetVehicleControl().accelerate(3,accelerationValue);
            

        } else if (binding.equals("Space")) {
            if (value) {
                m_RobotCyber.GetVehicleControl().applyImpulse(jumpForce, Vector3f.ZERO);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                m_RobotCyber.GetVehicleControl().setPhysicsLocation(Vector3f.ZERO);
                m_RobotCyber.GetVehicleControl().setPhysicsRotation(new Matrix3f());
                m_RobotCyber.GetVehicleControl().setLinearVelocity(Vector3f.ZERO);
                m_RobotCyber.GetVehicleControl().setAngularVelocity(Vector3f.ZERO);
                m_RobotCyber.GetVehicleControl().resetSuspension();
            } else {
            }
        }
        
        //Rotate the camNode to look at the target:
       // camNode.lookAt(vehicleNode.getLocalTranslation(), Vector3f.UNIT_Y);

    }
}
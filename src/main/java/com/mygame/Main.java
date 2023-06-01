package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.VehicleWheel;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private VehicleControl vehicleCyber;
    private final float accelerationForce = 2000.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    final private Vector3f jumpForce = new Vector3f(0, 3000, 0);
    private CameraNode camNode;
    private Node vehicleNode;

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
        bulletAppState = new BulletAppState();
       
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);
        setupKeys();
        
        RobotGameGraphics gameGraphics = new RobotGameGraphics();
        gameGraphics.createField(this, getPhysicsSpace(),80.0f,100.0f,10.0f);
        gameGraphics.createEnviroment(this);
        vehicleNode=new Node("vehicleNode");
        vehicleCyber = gameGraphics.createRobot(this, jumpForce, getPhysicsSpace(),vehicleNode);
        
        Vector3f loc = new Vector3f(0.0f,10.0f,0.0f);
        Vector3f loc2 = new Vector3f(-50.0f,30.0f,0.0f);
        this.cam.setLocation(loc2);
        cam.lookAt(loc,Vector3f.UNIT_Y);

    }

    private PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
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
        cam.lookAt(vehicleCyber.getPhysicsLocation(), Vector3f.UNIT_Y);
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += .5f;
            } else {
                steeringValue -= .5f;
            }
            vehicleCyber.steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue -= .5f;
            } else {
                steeringValue += .5f;
            }
            vehicleCyber.steer(steeringValue);
        } else if (binding.equals("Ups")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            vehicleCyber.accelerate(0,accelerationValue);
            vehicleCyber.accelerate(1,accelerationValue);
            vehicleCyber.accelerate(2,accelerationValue);
            vehicleCyber.accelerate(3,accelerationValue);

        } else if (binding.equals("Downs")) {
            if (value) {
                accelerationValue -= accelerationForce;
            } else {
                accelerationValue += accelerationForce;
            }
            //vehicle.accelerate(accelerationValue);           
            vehicleCyber.accelerate(0,accelerationValue);
            vehicleCyber.accelerate(1,accelerationValue);
            vehicleCyber.accelerate(2,accelerationValue);
            vehicleCyber.accelerate(3,accelerationValue);
            

        } else if (binding.equals("Space")) {
            if (value) {
                vehicleCyber.applyImpulse(jumpForce, Vector3f.ZERO);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                vehicleCyber.setPhysicsLocation(Vector3f.ZERO);
                vehicleCyber.setPhysicsRotation(new Matrix3f());
                vehicleCyber.setLinearVelocity(Vector3f.ZERO);
                vehicleCyber.setAngularVelocity(Vector3f.ZERO);
                vehicleCyber.resetSuspension();
            } else {
            }
        }
        
        //Rotate the camNode to look at the target:
       // camNode.lookAt(vehicleNode.getLocalTranslation(), Vector3f.UNIT_Y);

    }
}
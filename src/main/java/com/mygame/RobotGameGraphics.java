/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.VehicleWheel;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.post.ssao.SSAOFilter;

/**
 *
 * @author Gavin
 */
public class RobotGameGraphics {
    
        public VehicleControl createRobot(SimpleApplication app, Vector3f jumpForce, PhysicsSpace physicsSpace, Node vehicleNode ) {

        Material matRobotBody = app.getAssetManager().loadMaterial("Materials/RobotBody.j3m");
        Material matRobotWheels = app.getAssetManager().loadMaterial("Materials/RobotWheels.j3m");
        Material matBumper = app.getAssetManager().loadMaterial("Materials/RedBumperMat.j3m");
        Material matIntake = app.getAssetManager().loadMaterial("Materials/IntakeMaterial.j3m");

        
        float groundClearance = 1.5f;
        //create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
        //this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(3.75f, 1.0f, 3.75f));
        compoundShape.addChildShape(box, new Vector3f(0, groundClearance, 0));

        BoxCollisionShape box2 = new BoxCollisionShape(new Vector3f(2.75f, 4.0f, 2.75f));
        compoundShape.addChildShape(box2, new Vector3f(0, groundClearance+5.0f, 0));


        Cylinder robotIntake = new Cylinder(20,20,0.5f,6.5f,true);
        Geometry intakeGeo = new Geometry("intake",robotIntake);
        intakeGeo.setMaterial(matIntake);
        intakeGeo.setShadowMode(ShadowMode.CastAndReceive);

        intakeGeo.setLocalTranslation(0.0f, 0.75f, 4.5f);
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(FastMath.PI/2,new Vector3f(0,1,0));
        intakeGeo.setLocalRotation(pitch);
        vehicleNode.attachChild(intakeGeo);

        BoxCollisionShape intakeBox = new BoxCollisionShape(new Vector3f(3.5f, 0.5f, 0.5f));
        compoundShape.addChildShape(intakeBox, new Vector3f(0, 0.75f, 4.5f));
        
        
        
        


        //create vehicle node        
        VehicleControl vehicle = new VehicleControl(compoundShape, 400);
        
        CompoundCollisionShape compoundShape2 = new CompoundCollisionShape();
        BoxCollisionShape intakeBox2 = new BoxCollisionShape(new Vector3f(3.5f, 2.0f, 1.0f));
        compoundShape2.addChildShape(intakeBox2, new Vector3f(0, 1.75f, 5.0f));      
        GhostControl ghost = new GhostControl(compoundShape2);
        vehicleNode.addControl(ghost);
        physicsSpace.add(ghost);

        vehicle.applyTorque(jumpForce);
        vehicleNode.addControl(vehicle);
        Box robotBox = new Box(3.75f,1.0f,0.5f);
        Box robotBox2 = new Box(0.5f,1.0f,2.750f);
        
        Box robotBox3 = new Box(2.75f,4.0f,2.75f);
                
        Geometry robotBody = new Geometry("robotbody", robotBox);
        robotBody.setShadowMode(ShadowMode.CastAndReceive);
        robotBody.setLocalTranslation(0.0f, groundClearance, 3.25f);        
        robotBody.setMaterial(matBumper);
        vehicleNode.attachChild(robotBody);

        
        Geometry robotBody2 = new Geometry("robotbody2", robotBox);
        robotBody2.setShadowMode(ShadowMode.CastAndReceive);
        robotBody2.setLocalTranslation(0.0f, groundClearance, -3.25f);
        robotBody2.setMaterial(matBumper);
        vehicleNode.attachChild(robotBody2);

        Geometry robotBody3 = new Geometry("robotbody3", robotBox2);
        robotBody3.setShadowMode(ShadowMode.CastAndReceive);
        robotBody3.setLocalTranslation(3.25f, groundClearance, 0.0f);
        robotBody3.setMaterial(matBumper);
        vehicleNode.attachChild(robotBody3);

        Geometry robotBody4 = new Geometry("robotbody4", robotBox2);
        robotBody4.setShadowMode(ShadowMode.CastAndReceive);
        robotBody4.setLocalTranslation(-3.25f, groundClearance, 0.0f);
        robotBody4.setMaterial(matBumper);
        vehicleNode.attachChild(robotBody4);

        
        Geometry robotBody5 = new Geometry("robotbody5", robotBox3);
        robotBody5.setShadowMode(ShadowMode.CastAndReceive);
        robotBody5.setLocalTranslation(0.0f, groundClearance+5, 0.0f);
        robotBody5.setMaterial(matRobotBody);
        vehicleNode.attachChild(robotBody5);

        //setting suspension values for wheels, this can be a bit tricky
        //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f;
        vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionStiffness(stiffness);
        vehicle.setMaxSuspensionForce(10000.0f);

        //Create four wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
        float radius = 0.5f;
        float restLength = 0.3f;
        float yOff = 0.5f;
        float xOff = 2.0f;
        float zOff = 2.0f;

        Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 1.5f, true);

        Node node1 = new Node("wheel 1 node");
        Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
        wheels1.setShadowMode(ShadowMode.CastAndReceive);
        node1.attachChild(wheels1);
        wheels1.rotate(0, FastMath.HALF_PI, 0);
        wheels1.setMaterial(matRobotWheels);
        vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node2 = new Node("wheel 2 node");
        Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
        wheels2.setShadowMode(ShadowMode.CastAndReceive);

        node2.attachChild(wheels2);
        wheels2.rotate(0, FastMath.HALF_PI, 0);
        wheels2.setMaterial(matRobotWheels);
        vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node3 = new Node("wheel 3 node");
        Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
        wheels3.setShadowMode(ShadowMode.CastAndReceive);

        node3.attachChild(wheels3);
        wheels3.rotate(0, FastMath.HALF_PI, 0);
        wheels3.setMaterial(matRobotWheels);
        vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        Node node4 = new Node("wheel 4 node");
        Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
        wheels4.setShadowMode(ShadowMode.CastAndReceive);

        node4.attachChild(wheels4);
        wheels4.rotate(0, FastMath.HALF_PI, 0);
        wheels4.setMaterial(matRobotWheels);
        vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        vehicleNode.attachChild(node1);
        vehicleNode.attachChild(node2);
        vehicleNode.attachChild(node3);
        vehicleNode.attachChild(node4);
        app.getRootNode().attachChild(vehicleNode);

        physicsSpace.add(vehicle);
        
        RigidBodyControl ball = createBasketBall(app,null,new Vector3f(0.0f,12.0f,0.0f),vehicleNode);
        ball.setEnabled(false);
        vehicleNode.addControl(ball);
        
        
        return vehicle;
    }

    public void createEnviroment(SimpleApplication app) {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        app.getRootNode().addLight(sun);
        
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), 4096, 1);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.8f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        //dlsr.displayDebug();
        app.getViewPort().addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), 4096, 1);
        dlsf.setLight(sun);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.8f);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        dlsf.setEnabled(false);

        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        fpp.addFilter(dlsf);

        app.getViewPort().addProcessor(fpp);

        
        FilterPostProcessor fpp2 = new FilterPostProcessor(app.getAssetManager());
        SSAOFilter ssaoFilter = new SSAOFilter(3.94f, 13.92f, 0.33f, 0.61f);
        fpp2.addFilter(ssaoFilter);
        app.getViewPort().addProcessor(fpp2);
        

    }

    public void createField(SimpleApplication app,  PhysicsSpace space, float width, float length,float height) {
        
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        app.getRootNode().addLight(light);

        Material material = app.getAssetManager().loadMaterial("Materials/ArenaFloor.j3m");
        Material wallMaterial = app.getAssetManager().loadMaterial("Materials/ArenaWall.j3m");

        Box floorBox = new Box(width, 0.25f, length);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -5, 0);
        floorGeometry.setShadowMode(ShadowMode.CastAndReceive);
        RigidBodyControl floor = new RigidBodyControl(0);
        floorGeometry.addControl(floor);
        floor.setFriction(0.2f);
        floor.setRestitution(0.5f);

        app.getRootNode().attachChild(floorGeometry);
        space.add(floorGeometry);
        


        Box wall1Box1 = new Box(width,height, 1.0f);
        Geometry wall1Geometry = new Geometry("Wall1", wall1Box1);
        wall1Geometry.setMaterial(wallMaterial);
        wall1Geometry.setLocalTranslation(0.0f, height-5.0f, -length);
        wall1Geometry.setShadowMode(ShadowMode.CastAndReceive);
        RigidBodyControl wall1 = new RigidBodyControl(0);
        wall1Geometry.addControl(wall1);
        wall1.setFriction(0.5f);
        wall1.setRestitution(0.3f);
        app.getRootNode().attachChild(wall1Geometry);
        space.add(wall1Geometry);
        
        Box wall1Box2 = new Box(width,height, 1.0f);
        Geometry wall2Geometry = new Geometry("Wall2", wall1Box2);
        wall2Geometry.setMaterial(wallMaterial);
        wall2Geometry.setLocalTranslation(0.0f, height-5.0f, length);
        wall2Geometry.setShadowMode(ShadowMode.CastAndReceive);

        RigidBodyControl wall2 = new RigidBodyControl(0);
        wall2Geometry.addControl(wall2);
        wall2.setFriction(0.5f);
        wall2.setRestitution(0.3f);
        app.getRootNode().attachChild(wall2Geometry);
        space.add(wall2Geometry);

        Box wall3Box = new Box(1.0f,height, length);
        Geometry wall3Geometry = new Geometry("Wall3", wall3Box);
        wall3Geometry.setMaterial(wallMaterial);
        wall3Geometry.setLocalTranslation(width, height-5.0f, 0.0f);
        wall3Geometry.setShadowMode(ShadowMode.CastAndReceive);
        
        RigidBodyControl wall3 = new RigidBodyControl(0);
        wall3Geometry.addControl(wall3);
        wall3.setFriction(0.5f);
        wall3.setRestitution(0.3f);
        app.getRootNode().attachChild(wall3Geometry);
        space.add(wall3Geometry);

        Box wall4Box = new Box(1.0f,height, length);
        Geometry wall4Geometry = new Geometry("Wall4", wall4Box);
        wall4Geometry.setMaterial(wallMaterial);
        wall4Geometry.setLocalTranslation(-width, height-5.0f, 0.0f);
        wall4Geometry.setShadowMode(ShadowMode.CastAndReceive);
        

        RigidBodyControl wall4 = new RigidBodyControl(0);
        wall4Geometry.addControl(wall4);
        wall4.setFriction(0.5f);
        wall4.setRestitution(0.3f);
        app.getRootNode().attachChild(wall4Geometry);
        space.add(wall4Geometry);

        createBasketBall(app,space,new Vector3f(20.0f,10.0f,0.0f),app.getRootNode());
        createBasketBall(app,space,new Vector3f(-20.0f,10.0f,0.0f),app.getRootNode());
        createBasketBall(app,space,new Vector3f(20.0f,10.0f,20.0f),app.getRootNode());
        createBasketBall(app,space,new Vector3f(-20.0f,10.0f,20.0f),app.getRootNode());

    }
    
    public RigidBodyControl createBasketBall(SimpleApplication app,  PhysicsSpace space, Vector3f pos, Node parent) {
    Material ballMat = app.getAssetManager().loadMaterial("Materials/BallMaterial.j3m");
    /** Initialize the cannon ball geometry */
    Sphere sphere = new Sphere(32, 32, 1.4f, true, false);
    sphere.setTextureMode(TextureMode.Projected);
    /** Create a cannon ball geometry and attach to scene graph. */
    Geometry ball_geo = new Geometry("cannon ball", sphere);
    ball_geo.setShadowMode(ShadowMode.CastAndReceive);

    ball_geo.setMaterial(ballMat);
    parent.attachChild(ball_geo);
    /** Position the cannon ball  */
    
    ball_geo.setLocalTranslation(pos);
    /* Make the ball physical with a mass > 0.0f */
    RigidBodyControl ball_phy = new RigidBodyControl(1f);
    /** Add physical ball to physics space. */
    ball_geo.addControl(ball_phy);
    ball_phy.setRestitution(0.9f);
    ball_phy.setFriction(0.3f);
    
    
    if( space != null)
        space.add(ball_phy);
    
    return ball_phy;
  }

}

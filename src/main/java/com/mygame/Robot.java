/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import java.util.List;

/**
 *
 * @author Gavin
 */
public class Robot {
    VehicleControl m_vehicleControl;
    Node m_vehicleNode;
    GhostControl m_ghost;
    
    Robot(SimpleApplication app, PhysicsSpace physicsSpace, Vector3f position, double rotation, Alliance teamAlliance){
        createRobot(app, physicsSpace, position, rotation, teamAlliance);
        
    }
        VehicleControl GetVehicleControl(){return m_vehicleControl;}
    
        // Create 3D graphic for robot and setup physics objects
        private void createRobot(SimpleApplication app, PhysicsSpace physicsSpace, Vector3f position, double rotation, Alliance teamAlliance ) {

            
        m_vehicleNode=new Node("vehicleNode");
        m_vehicleNode.setLocalTranslation(position);
        
        Quaternion pitch3 = new Quaternion();
        pitch3.fromAngleAxis((float)rotation,new Vector3f(0,1,0));
        m_vehicleNode.setLocalRotation(pitch3);

        
        Material matRobotBody = app.getAssetManager().loadMaterial("Materials/RobotBody.j3m");
        Material matRobotWheels = app.getAssetManager().loadMaterial("Materials/RobotWheels.j3m");
        Material matBumper;
        if( teamAlliance == Alliance.RED)
            matBumper = app.getAssetManager().loadMaterial("Materials/RedBumperMat.j3m");
        else
            matBumper = app.getAssetManager().loadMaterial("Materials/BlueBumperMat.j3m");
        
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
        intakeGeo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        intakeGeo.setLocalTranslation(0.0f, 0.75f, 4.5f);
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(FastMath.PI/2,new Vector3f(0,1,0));
        intakeGeo.setLocalRotation(pitch);
        m_vehicleNode.attachChild(intakeGeo);

        BoxCollisionShape intakeBox = new BoxCollisionShape(new Vector3f(3.5f, 0.5f, 0.5f));
        compoundShape.addChildShape(intakeBox, new Vector3f(0, 0.75f, 4.5f));
        
        //create vehicle node        
        m_vehicleControl = new VehicleControl(compoundShape, 400);
        
        CompoundCollisionShape compoundShape2 = new CompoundCollisionShape();
        BoxCollisionShape intakeBox2 = new BoxCollisionShape(new Vector3f(3.5f, 2.0f, 1.0f));
        compoundShape2.addChildShape(intakeBox2, new Vector3f(0.0f, 1.75f, 5.0f));      
        m_ghost = new GhostControl(compoundShape2);
        m_vehicleNode.addControl(m_ghost);
        physicsSpace.add(m_ghost);

        m_vehicleNode.addControl(m_vehicleControl);
        Box robotBox = new Box(3.75f,1.0f,0.5f);
        Box robotBox2 = new Box(0.5f,1.0f,2.750f);
        
        Box robotBox3 = new Box(2.75f,4.0f,2.75f);
                
        Geometry robotBody = new Geometry("robotbody", robotBox);
        robotBody.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        robotBody.setLocalTranslation(0.0f, groundClearance, 3.25f);        
        robotBody.setMaterial(matBumper);
        m_vehicleNode.attachChild(robotBody);

        
        Geometry robotBody2 = new Geometry("robotbody2", robotBox);
        robotBody2.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        robotBody2.setLocalTranslation(0.0f, groundClearance, -3.25f);
        robotBody2.setMaterial(matBumper);
        m_vehicleNode.attachChild(robotBody2);

        Geometry robotBody3 = new Geometry("robotbody3", robotBox2);
        robotBody3.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        robotBody3.setLocalTranslation(3.25f, groundClearance, 0.0f);
        robotBody3.setMaterial(matBumper);
        m_vehicleNode.attachChild(robotBody3);

        Geometry robotBody4 = new Geometry("robotbody4", robotBox2);
        robotBody4.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        robotBody4.setLocalTranslation(-3.25f, groundClearance, 0.0f);
        robotBody4.setMaterial(matBumper);
        m_vehicleNode.attachChild(robotBody4);

        
        Geometry robotBody5 = new Geometry("robotbody5", robotBox3);
        robotBody5.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        robotBody5.setLocalTranslation(0.0f, groundClearance+5, 0.0f);
        robotBody5.setMaterial(matRobotBody);
        m_vehicleNode.attachChild(robotBody5);

        //setting suspension values for wheels, this can be a bit tricky
        //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f;
        m_vehicleControl.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        m_vehicleControl.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        m_vehicleControl.setSuspensionStiffness(stiffness);
        m_vehicleControl.setMaxSuspensionForce(10000.0f);

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
        wheels1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        node1.attachChild(wheels1);
        wheels1.rotate(0, FastMath.HALF_PI, 0);
        wheels1.setMaterial(matRobotWheels);
        m_vehicleControl.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node2 = new Node("wheel 2 node");
        Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
        wheels2.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        node2.attachChild(wheels2);
        wheels2.rotate(0, FastMath.HALF_PI, 0);
        wheels2.setMaterial(matRobotWheels);
        m_vehicleControl.addWheel(node2, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node3 = new Node("wheel 3 node");
        Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
        wheels3.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        node3.attachChild(wheels3);
        wheels3.rotate(0, FastMath.HALF_PI, 0);
        wheels3.setMaterial(matRobotWheels);
        m_vehicleControl.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        Node node4 = new Node("wheel 4 node");
        Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
        wheels4.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        node4.attachChild(wheels4);
        wheels4.rotate(0, FastMath.HALF_PI, 0);
        wheels4.setMaterial(matRobotWheels);
        m_vehicleControl.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        m_vehicleNode.attachChild(node1);
        m_vehicleNode.attachChild(node2);
        m_vehicleNode.attachChild(node3);
        m_vehicleNode.attachChild(node4);
        app.getRootNode().attachChild(m_vehicleNode);

        physicsSpace.add(m_vehicleControl);
        
        /*
        RigidBodyControl ball = createBasketBall(app,null,new Vector3f(0.0f,12.0f,0.0f),vehicleNode);
        ball.setEnabled(false);
        vehicleNode.addControl(ball);
        
        return vehicle;
*/
    }        
        
    public void simpleUpdate(float tpf)
    {
        if( m_ghost.getOverlappingCount()>2 )
        {
            List<PhysicsCollisionObject> items = m_ghost.getOverlappingObjects();
            for (PhysicsCollisionObject obj: items)
            {
                if( obj.getCollisionGroup() == 3)
                {
                    System.out.println("Red ball collision");
                }
                else if(obj.getCollisionGroup() == 3)
                {
                    System.out.println("Ball ball collision");
                }
            }

        }
    }

    
}

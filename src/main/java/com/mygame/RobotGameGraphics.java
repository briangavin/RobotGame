/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.post.ssao.SSAOFilter;
import java.util.ArrayList;
import java.util.List;



enum Alliance {
  RED,
  BLUE,
}

/**
 *
 * @author Gavin
 */
public class RobotGameGraphics {
    
    List<rubberBall> m_listFieldBalls;
    RobotGameGraphics(){
        m_listFieldBalls = new ArrayList<rubberBall> ();
    }
    // Add things such as sunlight, shadows and SSAO
    public void createEnviromentLowQuality(SimpleApplication app) {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        app.getRootNode().addLight(sun);
        
        /*
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), 2048, 1);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.8f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        //dlsr.displayDebug();
        app.getViewPort().addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), 2048, 1);
        dlsf.setLight(sun);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.8f);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        dlsf.setEnabled(false);

        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        fpp.addFilter(dlsf);

        app.getViewPort().addProcessor(fpp);
*/
               

    }

    // Add things such as sunlight, shadows and SSAO
    public void createEnviromentHighQuality(SimpleApplication app) {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        app.getRootNode().addLight(sun);
        

        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), 4096*2, 1);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.8f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        //dlsr.displayDebug();
        app.getViewPort().addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), 4096*2, 1);
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
        

        float wallThickness = 2.0f;
        Box wall1Box1 = new Box(width,height, wallThickness);
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
        
        Box wall1Box2 = new Box(width,height, wallThickness);
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

        Box wall3Box = new Box(wallThickness,height, length);
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

        Box wall4Box = new Box(wallThickness,height, length);
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

        m_listFieldBalls.add(createBasketBall(app,space,new Vector3f(20.0f,10.0f,0.0f),app.getRootNode(), Alliance.RED));
        m_listFieldBalls.add(createBasketBall(app,space,new Vector3f(-20.0f,10.0f,0.0f),app.getRootNode(), Alliance.RED));
        m_listFieldBalls.add(createBasketBall(app,space,new Vector3f(20.0f,10.0f,20.0f),app.getRootNode(), Alliance.BLUE));
        m_listFieldBalls.add(createBasketBall(app,space,new Vector3f(-20.0f,10.0f,20.0f),app.getRootNode(), Alliance.BLUE));

    }
    
    private rubberBall createBasketBall(SimpleApplication app, PhysicsSpace space, Vector3f pos, Node parent, Alliance teamAlliance) {
    
        Material ballMat;    
        if( teamAlliance == Alliance.RED)
            ballMat = app.getAssetManager().loadMaterial("Materials/RedBallMaterial.j3m");
        else
            ballMat = app.getAssetManager().loadMaterial("Materials/BlueBallMaterial.j3m");
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
        //RigidBodyControl ball_phy = new RigidBodyControl(1f);
        rubberBall ball_phy = new rubberBall(1f,teamAlliance);
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        ball_phy.setRestitution(0.9f);
        ball_phy.setFriction(0.3f);
        if( teamAlliance == Alliance.RED)
            ball_phy.setCollisionGroup(3);
        else
            ball_phy.setCollisionGroup(4);



        if( space != null)
            space.add(ball_phy);

        return ball_phy;
  }
    
    
  public void simpleUpdate(float tpf){
      //Check if ball are out of range
      
  }

}

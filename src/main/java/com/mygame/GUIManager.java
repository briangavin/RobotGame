/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.app.SimpleApplication;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

/**
 *
 * @author Auction
 */
public class GUIManager {
     GUIManager(SimpleApplication app)    {
         initGUI(app);
     }
     
    private void initGUI(SimpleApplication app)
    {
        // Initialize the globals access so that the defualt
        // components can find what they need.
        GuiGlobals.initialize(app);
            
        // Load the 'glass' style
        BaseStyles.loadGlassStyle();
            
        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
 
    }
    public void makeStartMenu(SimpleApplication app)
    {
        // Create a simple container for our elements
        Container myWindow = new Container();
        app.getGuiNode().attachChild(myWindow);
            
        // Put it somewhere that we will see it
        // Note: Lemur GUI elements grow down from the upper left corner.
        myWindow.setLocalTranslation(300, 50, 0);
    
        // Add some elements
        Button clickMe = myWindow.addChild(new Button("Settings"));
        clickMe.addClickCommands(new Command<Button>() {
                @Override
                public void execute( Button source ) {
                    System.out.println("The world is yours.");
                }
            });            
   
    }

}

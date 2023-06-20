/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ListBox;
import com.simsilica.lemur.Checkbox;

import com.simsilica.lemur.Command;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.core.GuiComponent;
import com.simsilica.lemur.list.SelectionModel;
import com.simsilica.lemur.style.BaseStyles;
import java.util.prefs.BackingStoreException;

/**
 *
 * @author Auction
 */
public class GUIManager {
    
     private Container m_MainMenu;
     private Container m_SettingsMenu;
     private Container m_activeMenu;
     private float m_menuScale;
     
     GUIManager(Main app)    {
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
        m_activeMenu = null;
        m_menuScale = 2.0f;
        makeStartMenu(app);
        makeSettingsMenu(app);       
 
    }
    private void makeStartMenu(SimpleApplication app)
    {
        // Create a simple container for our elements
        m_MainMenu = new Container();
        m_MainMenu.scale(m_menuScale);
            
        // Put it somewhere that we will see it
        // Note: Lemur GUI elements grow down from the upper left corner.
        m_MainMenu.setLocalTranslation(50, 100, 0);
    
        // Add some elements
        Button clickMe = m_MainMenu.addChild(new Button("Settings"));
        clickMe.setFontSize(20);
        clickMe.addClickCommands(new Command<Button>() {
                @Override
                public void execute( Button source ) {
                    //TODO make this nicer
                    app.getGuiNode().attachChild(m_SettingsMenu);
                }
            });            
   
    }
    
    private void makeSettingsMenu(SimpleApplication app)
    {
        // Create a simple container for our elements
        m_SettingsMenu = new Container();

        

        m_SettingsMenu.scale(m_menuScale);
        m_SettingsMenu.setLocalTranslation(300,600,0);
        Label title = m_SettingsMenu.addChild(new Label("Settings"));
        title.setFontSize(20);
        title.setColor(ColorRGBA.Blue);

        Label restitle = m_SettingsMenu.addChild(new Label("Resolution"));
        restitle.setColor(ColorRGBA.Red);
        
        ListBox listBox = m_SettingsMenu.addChild(new ListBox());
        listBox.setVisibleItems(5);
        listBox.getModel().add("1024x768");
        listBox.getModel().add("1280x720");
        listBox.getModel().add("1280x1024");
        listBox.getModel().add("1920x1200");
        listBox.getModel().add("3840x2160");
        
        listBox.getSelectionModel().setSelection(4);

        
        Checkbox checkBox = m_SettingsMenu.addChild(new Checkbox("Antialiasing "));
        checkBox.setColor(ColorRGBA.Red);
        checkBox.setChecked(true);
        
        Checkbox checkBox2 = m_SettingsMenu.addChild(new Checkbox("Fullscreen "));
        checkBox2.setColor(ColorRGBA.Red);
        checkBox2.setChecked(true);

    
        // Add some elements
        Button clickMe = m_SettingsMenu.addChild(new Button("Apply"));
        clickMe.addClickCommands(new Command<Button>() {
                @Override
                public void execute( Button source ) {
                    AppSettings settings = new AppSettings(true);
                    if(checkBox.isChecked())
                        settings.setSamples(4);
                    else
                        settings.setSamples(0);
                    
                    settings.setFullscreen(checkBox2.isChecked());
                    switch(listBox.getSelectionModel().getSelection())
                    {
                        case 0:
                            settings.setWidth(1024);
                            settings.setHeight(768);
                            break;
                        case 1:
                            settings.setWidth(1280);
                            settings.setHeight(720);
                            break;
                        case 2:
                            settings.setWidth(1280);
                            settings.setHeight(1024);
                            break;
                           
                        case 3:
                            settings.setWidth(1920);
                            settings.setHeight(1200);
                            break;
                        case 4:
                            settings.setWidth(3840);
                            settings.setHeight(2160);
                            break;
                   
                    }
                    app.setSettings(settings);
                    try{
                        settings.save("RobotSettings");
                    }
                    catch(BackingStoreException e)
                    {
                        //ignore just use default settings
                    }
                    app.restart();
                     
                }
            });                   
        
                    m_SettingsMenu.addChild(new ActionButton(new CallMethodAction("Close", 
                                                                  m_SettingsMenu, "removeFromParent")));

    }

    public void activateMainMenu(SimpleApplication app)
    {
        if( m_activeMenu != null)
        {
            app.getGuiNode().detachChild(m_activeMenu);
        }
        
        app.getGuiNode().attachChild(m_MainMenu);
        m_activeMenu = m_MainMenu;
    }
    
    public void activateSettingsMenu(SimpleApplication app)
    {
        if( m_activeMenu != null)
        {
            app.getGuiNode().detachChild(m_activeMenu);
        }
        

        app.getGuiNode().attachChild(m_SettingsMenu);
        m_activeMenu = m_SettingsMenu;
    }
    

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mygame;

import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author Gavin
 */
public class rubberBall extends RigidBodyControl {
        private Alliance m_allianceTeam;
        public rubberBall(float mass, Alliance allianceTeam) {
        super(mass);
        m_allianceTeam = allianceTeam;

        }
        
}

package com.star.game.Game;

import java.util.Properties;

public class KeysControl {
    int forward;
    int backward;
    int left;
    int right;
    int fire;
    int[] mass = new int[5];

    public KeysControl(Properties properties, String prefix) {
        forward = Integer.parseInt(properties.getProperty(prefix + "_FORWARD"));
        backward = Integer.parseInt(properties.getProperty(prefix + "_BACKWARD"));
        left = Integer.parseInt(properties.getProperty(prefix + "_LEFT"));
        right = Integer.parseInt(properties.getProperty(prefix + "_RIGHT"));
        fire = Integer.parseInt(properties.getProperty(prefix + "_FIRE"));
        mass[0] = forward;
        mass[1] = left;
        mass[2] = right;
        mass[3] = backward;
        mass[4] = fire;
    }

    public void update(Properties properties, String prefix) {
        forward = Integer.parseInt(properties.getProperty(prefix + "_FORWARD"));
        backward = Integer.parseInt(properties.getProperty(prefix + "_BACKWARD"));
        left = Integer.parseInt(properties.getProperty(prefix + "_LEFT"));
        right = Integer.parseInt(properties.getProperty(prefix + "_RIGHT"));
        fire = Integer.parseInt(properties.getProperty(prefix + "_FIRE"));
        mass[0] = forward;
        mass[1] = left;
        mass[2] = right;
        mass[3] = backward;
        mass[4] = fire;
    }

    public int[] getMass() {
        return mass;
    }
}

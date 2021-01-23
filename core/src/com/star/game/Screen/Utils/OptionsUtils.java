package com.star.game.Screen.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.io.IOException;
import java.util.Properties;

public class OptionsUtils {
    public enum buttonMode {
        FORWARD("FORWARD"),LEFT("LEFT"),RIGHT("RIGHT"),BACKWARD("BACKWARD"),FIRE("FIRE");
        String str;
        buttonMode(String str) {
            this.str = str;
        }
    }

    public static Properties loadProperties() {
        try {
            Properties properties = new Properties();
            properties.load(Gdx.files.local("options.properties").read());
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable to read options.properties");
    }

    public static boolean isOptionsExists() {
        return Gdx.files.local("options.properties").exists();
    }

    public static void createDefaultProperties() {
        try {
            Properties properties = new Properties();
            properties.put("PLAYER1_FORWARD", String.valueOf(Input.Keys.W));
            properties.put("PLAYER1_LEFT", String.valueOf(Input.Keys.A));
            properties.put("PLAYER1_RIGHT", String.valueOf(Input.Keys.D));
            properties.put("PLAYER1_BACKWARD", String.valueOf(Input.Keys.S));
            properties.put("PLAYER1_FIRE", String.valueOf(Input.Keys.SPACE));
            properties.store(Gdx.files.local("options.properties").write(false), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeButton(Properties properties, String prefix, int keyId, int keyCode) {
        try {
            properties.put(prefix+"_"+buttonMode.values()[keyId], String.valueOf(keyCode));
            properties.store(Gdx.files.local("options.properties").write(false), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

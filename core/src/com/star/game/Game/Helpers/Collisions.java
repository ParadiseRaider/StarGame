package com.star.game.Game.Helpers;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public interface Collisions {
    void collision(Collisions obj);
    Circle getHitArea();
    Vector2 getPosition();
    Vector2 getVelocity();
    boolean takeDamage(int amount);
    int getDamage();
}

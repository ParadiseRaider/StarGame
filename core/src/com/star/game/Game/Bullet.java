package com.star.game.Game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Game.Helpers.Poolable;

public class Bullet implements Poolable {
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float scale=0.5f;
    private float angle;
    private int cenX,cenY;
    private boolean isHeroBullet;
    private float dst;

    public boolean isHeroBullet() {
        return isHeroBullet;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Bullet(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.cenX = gc.getBulletController().getCenX();
        this.cenY = gc.getBulletController().getCenY();
        this.active = false;
    }

    public void activate(float x, float y, float vx, float vy, float angle, boolean isHeroBullet, float dst) {
        position.set(x,y);
        velocity.set(vx,vy);
        this.angle = angle;
        this.isHeroBullet = isHeroBullet;
        this.dst = dst;
        active = true;
    }

    public void update(float dt) {
        position.mulAdd(velocity,dt);
        dst-=velocity.len()*dt;
        float bx, by;
        bx = position.x - cenX * scale * MathUtils.cosDeg(angle);
        by = position.y - cenY * scale * MathUtils.sinDeg(angle);
        for (int i = 0; i < 2; i++) {
            gc.getParticleController().setup(
                    bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                    velocity.x * -0.1f + MathUtils.random(-30, 30), velocity.y * -0.1f + MathUtils.random(-30, 30),
                    0.1f,
                    0.5f, 0.2f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 0.0f
            );
        }
        if (dst<=0) {
            deactivate();
        }
    }
}

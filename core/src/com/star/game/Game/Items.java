package com.star.game.Game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Game.Helpers.Poolable;
import com.star.game.Screen.ScreenManager;

public class Items implements Poolable {
    public enum Type {
        AMMO(0), MEDKIT(1), MONEY(2);
        int index;
        Type(int index) { this.index = index; }
    }

    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private Type type;
    private boolean active;
    private float scale;
    private float angle;
    private int itemVal;
    private float time;
    private float blinkTime;

    @Override
    public boolean isActive() {
        return active;
    }

    public int getItemVal() {
        return itemVal;
    }

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public float getScale() {
        return scale;
    }

    public void deactivate() {
        active = false;
    }

    public float getTime() {
        return time;
    }

    public float getBlinkTime() {
        return blinkTime;
    }

    public void refreshBlinkTime() {
        blinkTime=0;
    }

    public Items(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.tmp = new Vector2(0,0);
        this.type = type.values()[MathUtils.random(0,2)];
        this.scale = 0.5f;
        this.active = false;
        switch (type) {
            case AMMO:
                itemVal = MathUtils.random(20,50) * gc.getLevel();
                break;
            case MEDKIT:
                itemVal = MathUtils.random(20,50);
                break;
            case MONEY:
                itemVal = 20 * gc.getLevel();
                break;
        }
    }

    public void activate(float x, float y) {
        position.set(x,y);
        velocity.set(MathUtils.random(-1,1),MathUtils.random(-1,1));
        velocity.nor().scl(50);
        angle = 0;
        time=0;
        blinkTime=0;
        active = true;
    }

    public void update(float dt) {
        if (gc.getHero().getPosition().dst(position)<=200) {
            tmp.set(gc.getHero().getPosition()).sub(position).nor();
            tmp.scl(150f);
            position.mulAdd(tmp,dt);
        } else {
            position.mulAdd(velocity,dt);
        }
        angle+=50*dt;
        time+=dt;
        blinkTime+=dt;
        if (time>=5f) {
            deactivate();
        }
    }
}

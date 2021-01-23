package com.star.game.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Game.Helpers.Collisions;
import com.star.game.Game.Helpers.Poolable;
import com.star.game.Screen.ScreenManager;
import com.star.game.Screen.Utils.Assets;

public class Asteroid implements Poolable, Collisions {
    private TextureRegion asteroid;
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private int cenX;
    private int cenY;
    private int hpMax;
    private int hp;
    private float rotationSpeed;
    private float angle;
    private float scale;
    private boolean active;
    private Circle hitArea;
    private int damage;
    private float timeRadar;

    public Asteroid(GameController gc) {
        this.asteroid = Assets.getInstance().getAtlas().findRegion("asteroid");
        cenX = asteroid.getRegionWidth()/2;
        cenY = asteroid.getRegionHeight()/2;
        this.gc = gc;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.tmp = new Vector2(0,0);
        this.hitArea = new Circle(0,0,0);
        active = false;
    }

    public boolean takeDamage(int amount) {
        hp-=amount;
        if (hp<=0) {
            deactivate();
            if (scale>1f) {
                gc.getAsteroidController().setup(position.x,position.y,MathUtils.random(-150f,150f),
                        MathUtils.random(-150f,150f),scale-0.2f);
                gc.getAsteroidController().setup(position.x,position.y,MathUtils.random(-150f,150f),
                        MathUtils.random(-150f,150f),scale-0.2f);
            } else {
                if (scale > 0.4f) {
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150f, 150f),
                            MathUtils.random(-150f, 150f), scale - 0.2f);
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150f, 150f),
                            MathUtils.random(-150f, 150f), scale - 0.2f);
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150f, 150f),
                            MathUtils.random(-150f, 150f), scale - 0.2f);
                }
            }
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(asteroid,position.x-cenX,position.y-cenY,cenX,cenY,cenX*2,cenY*2,
                scale,scale,angle);
        if(position.x > GameController.SPACE_WIDTH - ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(asteroid, position.x - 128 - GameController.SPACE_WIDTH, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
        }
        if(position.x < ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(asteroid, position.x - 128 + GameController.SPACE_WIDTH, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity,dt);
        angle+=rotationSpeed*dt;
        if (position.x < -cenY * scale) {
            position.x = GameController.SPACE_WIDTH + cenY * scale;
        }
        if (position.x > GameController.SPACE_WIDTH + cenY * scale) {
            position.x = -cenY * scale;
        }
        if (position.y < -cenY * scale) {
            position.y = GameController.SPACE_HEIGHT + cenY * scale;
        }
        if (position.y > GameController.SPACE_HEIGHT + cenY * scale) {
            position.y = -cenY * scale;
        }
        hitArea.setPosition(position);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }


    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x,y);
        this.velocity.set(vx,vy);
        this.hpMax = (int) (10*scale+(5*gc.getLevel()));
        this.damage=5*gc.getLevel();
        this.hp = hpMax;
        this.angle = MathUtils.random(0f,360f);
        this.hitArea.setPosition(position);
        this.rotationSpeed = MathUtils.random(-100f,100f);
        this.active = true;
        this.scale = scale;
        this.hitArea.setRadius(cenY*scale*0.9f);
        this.timeRadar=0;
    }

    public void setRadar(float time) {
        timeRadar=time;
    }

    public float getTimeRadar() {
        return timeRadar;
    }

    public void decRadar(float dt) {
        timeRadar-=dt;
        if (timeRadar<0) timeRadar=0;
    }

    @Override
    public void collision(Collisions obj) {
        if (obj.getHitArea().overlaps(getHitArea())) {
            float dst = obj.getPosition().dst(getPosition());
            float p = (obj.getHitArea().radius + getHitArea().radius - dst) / 2;
            tmp.set(getPosition()).sub(obj.getPosition()).nor();
            getPosition().mulAdd(tmp, p);
            obj.getPosition().mulAdd(tmp, -p);
            float thetta1 = getVelocity().angle();
            float thetta2 = obj.getVelocity().angle();
            float fi1 = tmp.set(obj.getPosition()).sub(getPosition()).angle();
            float fi2 = tmp.set(getPosition()).sub(obj.getPosition()).angle();
            getVelocity().x = gc.vx(getVelocity().len(), obj.getVelocity().len(), getHitArea().radius,
                    obj.getHitArea().radius * 5f, fi1, thetta1, thetta2);
            getVelocity().y = gc.vy(getVelocity().len(), obj.getVelocity().len(), getHitArea().radius,
                    obj.getHitArea().radius * 5f, fi1, thetta1, thetta2);
            obj.getVelocity().x = gc.vx(obj.getVelocity().len(), getVelocity().len(), obj.getHitArea().radius * 5f,
                    getHitArea().radius, fi2, thetta2, thetta1);
            obj.getVelocity().x = gc.vy(obj.getVelocity().len(), getVelocity().len(), obj.getHitArea().radius * 5f,
                    getHitArea().radius, fi2, thetta2, thetta1);
            obj.takeDamage(getDamage());
            takeDamage(obj.getDamage());
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getScale() {
        return scale;
    }

    public float getAngle() {
        return angle;
    }

    public int getDamage() {
        return damage;
    }
}

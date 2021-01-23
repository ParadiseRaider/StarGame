package com.star.game.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.game.Game.Helpers.Collisions;
import com.star.game.Game.Helpers.Poolable;
import com.star.game.Screen.Utils.Assets;

public class Bot implements Poolable, Collisions {
    private TextureRegion bot;
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private Vector2 tmp2;
    private Vector2 dst;
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
    private float enginePower;
    private WeaponBot currentWeapon;
    private float fireTimer;

    @Override
    public boolean isActive() {
        return active;
    }

    public Bot(GameController gc) {
        this.bot = Assets.getInstance().getAtlas().findRegion("bot1");
        cenX = bot.getRegionWidth()/2;
        cenY = bot.getRegionHeight()/2;
        this.gc = gc;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.tmp = new Vector2(0,0);
        this.tmp2 = new Vector2(0,0);
        this.dst = new Vector2(0,0);
        this.hitArea = new Circle(0,0,0);
        active = false;
    }

    public boolean takeDamage(int amount) {
        hp-=amount;
        if (hp<=0) {
            deactivate();
            return true;
        }
        return false;
    }

    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(bot,position.x-cenX,position.y-cenY,cenX,cenY,cenX*2,cenY*2,
                scale,scale,angle);
    }

    public void update(float dt) {
        fireTimer+=dt;
        tmp.set(gc.getHero().getPosition()).sub(position);
        float cx, cy;
        if (gc.getHero().getPosition().x > gc.SPACE_WIDTH / 2) cx = gc.SPACE_WIDTH;
        else cx = -gc.SPACE_WIDTH;
        if (gc.getHero().getPosition().y > gc.SPACE_HEIGHT / 2) cy = gc.SPACE_HEIGHT;
        else cy = -gc.SPACE_HEIGHT;
        tmp2.set(gc.getHero().getPosition()).sub(position.x + cx, position.y);
        if (tmp2.len() < tmp.len()) tmp.set(tmp2);
        tmp2.set(gc.getHero().getPosition()).sub(position.x, position.y + cy);
        if (tmp2.len() < tmp.len()) tmp.set(tmp2);
        tmp2.set(gc.getHero().getPosition()).sub(position.x + cx, position.y + cy);
        if (tmp2.len() < tmp.len()) tmp.set(tmp2);
        if (tmp.len()<=1100) {
            float needAngle = tmp.angle();

            needAngle -= angle;
            if (needAngle < 0) needAngle += 360;
            if (needAngle < 180) {
                angle += rotationSpeed * dt;
                angle %= 360f;
            } else {
                angle -= rotationSpeed * dt;
                if (angle < 0) angle += 360f;
            }

            if (gc.getHero().getPosition().dst(position) >= 600) {
                velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
                velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
            }
            if (gc.getHero().getPosition().dst(position)<=800) {
                tryToFire();
            }
        } else {
            if (position.dst(dst) <= hitArea.radius*2) {
                dst.set(MathUtils.random(0, GameController.SPACE_WIDTH), MathUtils.random(0, GameController.SPACE_HEIGHT));
            } else {
                tmp.set(dst).sub(position);
                float needAngle = tmp.angle();

                needAngle -= angle;
                if (needAngle < 0) needAngle += 360;
                if (needAngle < 180) {
                    angle += rotationSpeed * dt;
                    angle %= 360f;
                } else {
                    angle -= rotationSpeed * dt;
                    if (angle < 0) angle += 360f;
                }
                velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
                velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
            }
        }
        position.mulAdd(velocity,dt);
        float stopKoef = 1.0f - 2.0f * dt;
        if (stopKoef<0.0f) stopKoef=0.0f;
        velocity.scl(stopKoef);
        if (velocity.len() > 50.0f) {
            float bx, by;
            bx = position.x - cenX * scale * MathUtils.cosDeg(angle);
            by = position.y - cenY * scale * MathUtils.sinDeg(angle);
            for (int i = 0; i < 5; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-30, 30), velocity.y * -0.1f + MathUtils.random(-30, 30),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.0f, 0.1f, 1.0f,
                        1.0f, 1.0f, 0.3f, 0.0f
                );
            }
        }
        checkSpaceBorders();
    }

    public void checkSpaceBorders() {
        if (position.x < hitArea.radius) {
            position.x += GameController.SPACE_WIDTH;
        }
        if (position.x > GameController.SPACE_WIDTH - hitArea.radius) {
            position.x -= GameController.SPACE_WIDTH;
        }
        if (position.y < hitArea.radius) {
            position.y = GameController.SPACE_HEIGHT - hitArea.radius - 1;
        }
        if (position.y > GameController.SPACE_HEIGHT - hitArea.radius) {
            position.y = hitArea.radius + 1;
        }
        hitArea.setPosition(position);
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float scale) {
        this.position.set(x,y);
        this.hpMax = 20 + gc.getLevel()*5;
        this.damage=5*gc.getLevel();
        this.hp = hpMax;
        this.angle = MathUtils.random(0f,360f);
        this.hitArea.setPosition(position);
        this.rotationSpeed = 120;
        this.active = true;
        this.scale = scale;
        this.hitArea.setRadius(cenY*scale);
        this.enginePower = 1000f;
        this.fireTimer = 0f;
        this.dst.set(MathUtils.random(0,GameController.SPACE_WIDTH), MathUtils.random(0,GameController.SPACE_HEIGHT));
        this.currentWeapon = new WeaponBot(
                gc, this, "Laser-bot", 0.2f, 1, 600.0f, 300,
                new Vector3[]{
                        new Vector3(cenX*scale-15, 90, 0),
                        new Vector3(cenX*scale-15, -90, 0)
                }
        );
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

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getDamage() {
        return damage;
    }

    public float getAngle() {
        return angle;
    }

    public WeaponBot getCurrentWeapon() {
        return currentWeapon;
    }

    public int getHpMax() {
        return hpMax;
    }
}

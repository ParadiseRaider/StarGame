package com.star.game.Game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Screen.Utils.Assets;

public class GameController {
    public static final int SPACE_WIDTH = 9600;
    public static final int SPACE_HEIGHT = 5400;

    private Music music;
    private int level;
    private float timeSeeLevel;
    private Background background;
    private Hero hero;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private ParticleController particleControllerGUI;
    private ItemsController itemsController;
    private BotController botController;
    private Vector2 tmp;

    public BotController getBotController() {
        return botController;
    }

    public int getLevel() {
        return level;
    }

    public float getTimeSeeLevel() {
        return timeSeeLevel;
    }

    public void decTime(float dt) {
        timeSeeLevel-=dt;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public ParticleController getParticleControllerGUI() {
        return particleControllerGUI;
    }

    public ItemsController getItemsController() {
        return itemsController;
    }

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this, "PLAYER1");
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.particleControllerGUI = new ParticleController();
        this.itemsController = new ItemsController(this);
        this.botController = new BotController(this);
        this.tmp = new Vector2(0f,0f);
        this.level = 1;
        this.timeSeeLevel=2;
        for (int i = 0; i < 10; i++) {
            this.asteroidController.setup(MathUtils.random(0,GameController.SPACE_WIDTH), MathUtils.random(0,GameController.SPACE_HEIGHT),
                    MathUtils.random(-150f,150f),MathUtils.random(-150f,150f),1.1f);
        }
        this.botController.setup(MathUtils.random(0,GameController.SPACE_WIDTH), MathUtils.random(0,GameController.SPACE_HEIGHT),0.5f);
        this.music = Assets.getInstance().getAssetManager().get("audio/Flying.mp3");
        this.music.setLooping(true);
        this.music.play();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        particleControllerGUI.update(dt);
        itemsController.update(dt);
        botController.update(dt);
        checkCollisions();
        endLevel();
    }

    public void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            for (int j = 0; j < bulletController.getActiveList().size(); j++) {
                Bullet b = bulletController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.2f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );
                    b.deactivate();
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        if (b.isHeroBullet()) hero.addScore(a.getHpMax() * 100);
                        int rnd = MathUtils.random(0,100);
                        if (rnd<=30*a.getScale()) {
                            for (int k = 0; k < 3; k++) {
                                itemsController.setup(a.getPosition().x, a.getPosition().y);
                            }
                        }
                    }
                    break;
                }
            }
        }
        collisions();
        checkBulletsCollisions();
        takeItem();
    }

    public void collisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            hero.collision(a);
            for (int j = 0; j < botController.getActiveList().size(); j++) {
                Bot bot = botController.getActiveList().get(j);
                bot.collision(a);
            }
        }
        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            hero.collision(bot);
        }
    }

    public void checkBulletsCollisions() {
        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            for (int j = 0; j < bulletController.getActiveList().size(); j++) {
                Bullet b = bulletController.getActiveList().get(j);
                if (hero.getHitArea().contains(b.getPosition()) && !b.isHeroBullet()) {
                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.2f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );
                    b.deactivate();
                    hero.takeDamage(bot.getCurrentWeapon().getDamage());
                    break;
                }
                if (bot.getHitArea().contains(b.getPosition()) && b.isHeroBullet()) {
                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.2f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );
                    b.deactivate();
                    if (bot.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.addScore(bot.getHpMax() * 1000);
                    }
                    break;
                }
            }
        }
    }

    public void takeItem() {
        for (int i = 0; i < itemsController.getActiveList().size(); i++) {
            Items o = itemsController.getActiveList().get(i);
            if (hero.getHitArea().contains(o.getPosition())) {
                hero.consume(o);
                particleController.getEffectBuilder().takeItemEffect(o.getPosition().x,o.getPosition().y);
                o.deactivate();
            }
        }
    }

    public void endLevel() {
        if (asteroidController.getActiveList().size()==0 && botController.getActiveList().size()==0) {
            level++;
            timeSeeLevel = 2;
            for (int i = 0; i < 10+level; i++) {
                this.asteroidController.setup(MathUtils.random(0,GameController.SPACE_WIDTH), MathUtils.random(0,GameController.SPACE_HEIGHT),
                        MathUtils.random(-150f,150f),MathUtils.random(-150f,150f),1.1f);
            }
            for (int i = 0; i < level ; i++) {
                this.botController.setup(MathUtils.random(0,GameController.SPACE_WIDTH), MathUtils.random(0,GameController.SPACE_HEIGHT),0.5f);
            }
        }
    }

    public float vx(float v1, float v2, float m1, float m2, float fi, float thetta1, float thetta2) {
        float v = ((v1*MathUtils.cosDeg(thetta1-fi)*(m1-m2)+2*m2*v2*MathUtils.cosDeg(thetta2-fi))/(m1+m2))*
                MathUtils.cosDeg(fi)+v1*MathUtils.sinDeg(thetta1-fi)*MathUtils.cosDeg(fi+90);
        return v;
    }

    public float vy(float v1, float v2, float m1, float m2, float fi, float thetta1, float thetta2) {
        float v = ((v1*MathUtils.cosDeg(thetta1-fi)*(m1-m2)+2*m2*v2*MathUtils.cosDeg(thetta2-fi))/(m1+m2))*
                MathUtils.sinDeg(fi)+v1*MathUtils.sinDeg(thetta1-fi)*MathUtils.sinDeg(fi+90);
        return v;
    }

    public void dispose() {
        background.dispose();
    }
}

package com.star.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.game.Game.Helpers.Collisions;
import com.star.game.Screen.ScreenManager;
import com.star.game.Screen.Utils.Assets;
import com.star.game.Screen.Utils.OptionsUtils;

public class Hero implements Collisions {
    private String[] skillsName = {"More health", "More damage weapon"};
    private int[][] skillsCost = {{200, 300, 500, 750, 0}, {1000, 1500, 2000, 2700, 0}};

    public class Skills {
        private int level;
        private int maxLvl;
        private int cost;
        private Shop.Type type;

        public int getMaxLvl() {
            return maxLvl;
        }

        public int getLevel() {
            return level;
        }

        public Shop.Type getType() {
            return type;
        }

        public int getCost() {
            return cost;
        }

        public Skills(Shop.Type type, int maxLvl, int cost) {
            this.level = 1;
            this.type = type;
            this.maxLvl = maxLvl;
            this.cost = cost;
        }

        public void upgrage(Shop.Type type, int amount) {
            switch (type) {
                case MEDKIT:
                    maxHp += amount;
                    cost = skillsCost[0][level - 1];
                    level++;
                    break;
                case WEAPON:
                    currentWeapon.upgradeDamage(1);
                    cost = skillsCost[1][level - 1];
                    level++;
                    break;
            }
        }
    }

    private GameController gc;
    private TextureRegion texture;
    private TextureRegion radarpos;
    private TextureRegion radar;
    private KeysControl keysControl;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private float angle;
    private float enginePower;
    private Circle hitArea;
    private int hp;
    private int maxHp;
    private int money;
    private int cenX;
    private int cenY;
    private float scale;
    private float fireTimer;
    private int score;
    private int scoreView;
    private Shop shop;
    private Skills[] skills;
    private StringBuilder stringBuilder;
    private Weapon currentWeapon;
    private float angleRadar;

    public String[] getSkillsName() {
        return skillsName;
    }

    public int[][] getSkillsCost() {
        return skillsCost;
    }

    public Shop getShop() {
        return shop;
    }

    public Skills[] getSkills() {
        return skills;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public int getHp() {
        return hp;
    }

    public float getAngle() {
        return angle;
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) {
            return false;
        }
        return true;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public int getScore() {
        return score;
    }

    public void firstAid(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }

    public boolean checkCost(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public Hero(GameController gc, String keysControlPrefix) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.radarpos = Assets.getInstance().getAtlas().findRegion("star5");
        this.radar = Assets.getInstance().getAtlas().findRegion("radar");
        this.position = new Vector2(4800, 2700);
        this.velocity = new Vector2(0, 0);
        this.tmp = new Vector2(0, 0);
        this.scale = 0.5f;
        this.angle = 0.0f;
        this.cenX = texture.getRegionWidth() / 2;
        this.cenY = texture.getRegionHeight() / 2;
        this.enginePower = 1100.0f;
        this.maxHp = 100;
        this.hp = maxHp;
        this.money = 0;
        this.hitArea = new Circle(0, 0, cenX * scale);
        this.stringBuilder = new StringBuilder();
        this.keysControl = new KeysControl(OptionsUtils.loadProperties(), keysControlPrefix);
        this.angleRadar = 0;
        this.currentWeapon = new Weapon(
                gc, this, "Laser", 0.2f, 1, 600.0f, 300,
                new Vector3[]{
                        new Vector3(cenX * scale, 0, 0),
                        new Vector3(cenX * scale - 15, 90, 0),
                        new Vector3(cenX * scale - 15, -90, 0)
                }
        );
        this.skills = new Skills[]{
                new Skills(Shop.Type.MEDKIT, 5, 100),
                new Skills(Shop.Type.WEAPON, 5, 500),
        };
        this.shop = new Shop(this);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - cenX, position.y - cenY, cenX, cenY,
                cenX * 2, cenY * 2, scale, scale, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font, float dt) {
        stringBuilder.clear();
        stringBuilder.append("SCORE - ").append(scoreView).append("\n");
        stringBuilder.append("HP - ").append(hp).append(" / ").append(maxHp).append("\n");
        stringBuilder.append("BULLETS - ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        stringBuilder.append("MONEY - ").append(money).append("\n");
        stringBuilder.append("Bots - ").append(gc.getBotController().getActiveList().size()).append("\n");
        font.draw(batch, stringBuilder, 20, 1080);

        int mapX = 1830;
        int mapY = 940;
        int cenX = radarpos.getRegionWidth();
        int cenY = radarpos.getRegionHeight();
        batch.setColor(Color.GREEN);
        batch.draw(radarpos, mapX - cenX / 2, mapY - cenY / 2, cenX, cenY);
        batch.setColor(Color.GREEN);
        for (int i = 0; i < 130; i++) {
            batch.draw(radarpos, mapX + i * MathUtils.cosDeg(angleRadar) - cenX / 2, mapY + i * MathUtils.sinDeg(angleRadar) - cenY / 2);
            float bx, by;
            bx = mapX + i * MathUtils.cosDeg(angleRadar);
            by = mapY + i * MathUtils.sinDeg(angleRadar);
            for (int j = 0; j < 2; j++) {
                gc.getParticleControllerGUI().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        0, 0,
                        0.6f,
                        0.2f, 0.1f,
                        0.5f, 1.0f, 0.5f, 0.3f,
                        0.0f, 0.0f, 0.0f, 0.0f
                );
            }
        }
        for (int i = 0; i < gc.getAsteroidController().getActiveList().size(); i++) {
            Asteroid a = gc.getAsteroidController().getActiveList().get(i);
            float dst = position.dst(a.getPosition());

            if (dst < 2000) {
                tmp.set(a.getPosition());
                tmp.sub(position);
                tmp.scl(150f / 2500f);
                if (angleRadar >= tmp.angle() - 2 && angleRadar <= tmp.angle() + 2) a.setRadar(2f);
                if (a.getTimeRadar() > 0) {
                    if (a.getTimeRadar() < 1f) batch.setColor((a.getTimeRadar() / 2f), 0, 0, 1);
                    else batch.setColor(Color.RED);
                    batch.draw(radarpos, mapX + tmp.x - cenX / 2, mapY + tmp.y - cenY / 2, cenX, cenY);
                    a.decRadar(dt);
                }
            }
        }
        batch.setColor(1, 1, 1, 0.4f);
        batch.draw(radar, mapX - radar.getRegionWidth() / 2, mapY - radar.getRegionHeight() / 2);
        batch.setColor(Color.WHITE);
        angleRadar -= 100 * dt;
        if (angleRadar < 0) angleRadar += 360f;
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);
        if (Gdx.input.isKeyPressed(keysControl.fire)) {
            tryToFire();
        }

        if (Gdx.input.isKeyPressed(keysControl.left)) {
            angle += 180.0f * dt;
            angle %= 360;
        }

        if (Gdx.input.isKeyPressed(keysControl.right)) {
            angle -= 180.0f * dt;
            if (angle < 0) {
                angle += 360;
            }
        }

        if (Gdx.input.isKeyPressed(keysControl.forward)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        }

        if (Gdx.input.isKeyPressed(keysControl.backward)) {
            velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;
        }

        position.mulAdd(velocity, dt);
        float stopKoef = 1.0f - 2.0f * dt;
        if (stopKoef < 0.0f) stopKoef = 0.0f;
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
                        0.1f, 0.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f
                );
            }
        }
        checkSpaceBorders();

        if (hp <= 0) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER);
        }
    }

    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
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

    public void consume(Items items) {
        switch (items.getType()) {
            case AMMO:
                getCurrentWeapon().addAmmo(items.getItemVal());
                break;
            case MEDKIT:
                firstAid(items.getItemVal());
                break;
            case MONEY:
                addMoney(items.getItemVal());
                break;
        }
    }

    public void updateScore(float dt) {
        if (scoreView < score) {
            float scoreSpeed = (score - scoreView) / 2f;
            if (scoreSpeed < 2000) {
                scoreSpeed = 2000f;
            }
            scoreView += scoreSpeed * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
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

    @Override
    public int getDamage() {
        return 5;
    }
}

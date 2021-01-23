package com.star.game.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.game.Game.Helpers.ObjectPool;
import com.star.game.Screen.Utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private GameController gc;
    private TextureRegion bulletTexture;
    private int cenX;
    private int cenY;

    @Override
    protected Bullet newObject() {
        return new Bullet(gc);
    }

    public int getCenX() {
        return cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public BulletController(GameController gc) {
        this.gc = gc;
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet");
        this.cenX = bulletTexture.getRegionWidth()/2;
        this.cenY = bulletTexture.getRegionHeight()/2;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            if (!b.isHeroBullet()) batch.setColor(Color.YELLOW);
            else batch.setColor(1,1,1,1);
            batch.draw(bulletTexture,b.getPosition().x-cenX,b.getPosition().y-cenY, cenX, cenY,
                    cenX*2,cenY*2,b.getScale(),b.getScale(),b.getAngle());
        }
    }

    public void setup(float x, float y, float vx, float vy, float angle, boolean isHeroBullet, float dst) {
        getActiveElement().activate(x, y, vx, vy, angle, isHeroBullet, dst);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}

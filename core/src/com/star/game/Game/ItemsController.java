package com.star.game.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.game.Game.Helpers.ObjectPool;
import com.star.game.Screen.Utils.Assets;

public class ItemsController extends ObjectPool<Items> {
    private GameController gc;
    private TextureRegion[] itemsTexture;
    private int cenX;
    private int cenY;

    @Override
    protected Items newObject() {
        return new Items(gc);
    }

    public ItemsController(GameController gc) {
        this.gc = gc;
        this.itemsTexture = new TextureRegion[]{Assets.getInstance().getAtlas().findRegion("ammo"),
                Assets.getInstance().getAtlas().findRegion("heal"), Assets.getInstance().getAtlas().findRegion("money")};
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Items o = activeList.get(i);
            cenX = itemsTexture[o.getType().index].getRegionWidth()/2;
            cenY = itemsTexture[o.getType().index].getRegionWidth()/2;
            if (o.getTime()<2.5f || o.getBlinkTime()>0.3f) {
                if (o.getTime()<2.5f) o.refreshBlinkTime();
                if (o.getBlinkTime()>0.6f) o.refreshBlinkTime();
                batch.draw(itemsTexture[o.getType().index], o.getPosition().x - cenX, o.getPosition().y - cenY, cenX, cenY,
                        cenX * 2, cenY * 2, o.getScale(), o.getScale(), o.getAngle());
            }
        }
    }

    public void setup(float x, float y) {
        getActiveElement().activate(x,y);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}

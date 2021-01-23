package com.star.game.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.game.Game.Helpers.ObjectPool;

public class BotController extends ObjectPool<Bot> {
    private GameController gc;

    public BotController(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected Bot newObject() {
        return new Bot(gc);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bot bot = activeList.get(i);
            bot.render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void setup(float x, float y, float scale) {
        getActiveElement().activate(x,y,scale);
    }
}

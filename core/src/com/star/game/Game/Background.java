package com.star.game.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Screen.ScreenManager;
import com.star.game.Screen.Utils.Assets;

public class Background {
    private class Star {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;
        private int starType;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH), MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) * 0.9f / 40.0f * 0.7f;
            this.starType = MathUtils.random(0,textureStar.length-1);
        }

        public void update(float dt) {
            if (gc!=null) {
                position.x += (velocity.x - gc.getHero().getVelocity().x / 4) * dt;
                position.y += (velocity.y - gc.getHero().getVelocity().y / 4) * dt;
            } else {
                position.mulAdd(velocity, dt);
            }
            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 20;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                velocity.x = MathUtils.random(-40, -5);
                scale = Math.abs(velocity.x) * 0.9f / 40.0f * 0.7f;
            }
            if (position.x > ScreenManager.SCREEN_WIDTH + 20) {
                position.x = 20;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                velocity.x = MathUtils.random(-40, -5);
                scale = Math.abs(velocity.x) * 0.9f / 40.0f * 0.7f;
            }
            if (position.y > ScreenManager.SCREEN_HEIGHT + 20) {
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                velocity.x = MathUtils.random(-40, -5);
                scale = Math.abs(velocity.x) * 0.9f / 40.0f * 0.7f;
            }
            if (position.y < -200) {
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                velocity.x = MathUtils.random(-40, -5);
                scale = Math.abs(velocity.x) * 0.9f / 40.0f * 0.7f;
            }
        }
    }

    private final int STARS_COUNT = 600;
    private GameController gc;
    private Texture textureCosmos;
    private TextureRegion[] textureStar = {Assets.getInstance().getAtlas().findRegion("star1"),
            Assets.getInstance().getAtlas().findRegion("star2"),
            Assets.getInstance().getAtlas().findRegion("star3"),
            Assets.getInstance().getAtlas().findRegion("star4"),
            Assets.getInstance().getAtlas().findRegion("star5")};
    private Star[] stars;
    private int cenX;
    private int cenY;

    public Background(GameController gc) {
        this.gc = gc;
        this.textureCosmos = new Texture("images/bg.png");
        this.stars = new Star[STARS_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
        for (int i = 0; i < stars.length; i++) {
            this.cenX = textureStar[stars[i].starType].getRegionWidth()/2;
            this.cenY = textureStar[stars[i].starType].getRegionHeight()/2;
            batch.draw(textureStar[stars[i].starType], stars[i].position.x - cenX, stars[i].position.y - cenY, cenX, cenY,
                    cenX*2, cenY*2, stars[i].scale, stars[i].scale, 0);
            if (MathUtils.random(0, 500) < 2) {
                batch.draw(textureStar[stars[i].starType], stars[i].position.x - cenX, stars[i].position.y - cenY, cenX, cenY,
                        cenX*2, cenY*2, stars[i].scale * 2, stars[i].scale * 2,
                        0);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }

    public void dispose() {
        textureCosmos.dispose();
    }
}

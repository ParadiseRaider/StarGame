package com.star.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.star.game.Screen.ScreenManager;
import com.star.game.Screen.Utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font20;
    private BitmapFont font72;

    private Camera camera;
    private FrameBuffer frameBuffer;
    private TextureRegion frameBufferRegion;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.frameBufferRegion.flip(false, true);
        this.camera = ScreenManager.getInstance().getCamera();
    }

    public void render(float dt) {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        gc.getBackground().render(batch);
        batch.end();

        camera.position.set(gc.getHero().getPosition().x,gc.getHero().getPosition().y,0.0f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        gc.getHero().render(batch);
        gc.getBotController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getBulletController().render(batch);
        gc.getParticleController().render(batch);
        gc.getItemsController().render(batch);
        batch.end();
        frameBuffer.end();

        camera.position.set(ScreenManager.HALF_SCREEN_WIDTH, ScreenManager.HALF_SCREEN_HEIGHT, 0.0f);
        camera.update();
        ScreenManager.getInstance().getViewport().apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(frameBufferRegion, 0, 0);
        batch.end();

        batch.begin();
        if (!ScreenManager.PAUSE) {
            gc.getHero().renderGUI(batch,font20,dt);
            gc.getParticleControllerGUI().render(batch);
        }
        if (gc.getTimeSeeLevel()>0) {
            gc.decTime(dt);
            font72.draw(batch, "Level "+gc.getLevel(), 0, 900, ScreenManager.SCREEN_WIDTH, 1, false);
        }
        if (ScreenManager.PAUSE) {
            font72.draw(batch, "PAUSE", 0, 900, ScreenManager.SCREEN_WIDTH, 1, false);
        }
        batch.end();
    }
}

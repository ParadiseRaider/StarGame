package com.star.game.Screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.star.game.Screen.Utils.Assets;
import com.star.game.StarGame;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME, GAMEOVER, OPTIONS
    }
    public static final int SCREEN_WIDTH = 1980;
    public static final int SCREEN_HEIGHT = 1080;
    public static final int HALF_SCREEN_WIDTH = SCREEN_WIDTH / 2;
    public static final int HALF_SCREEN_HEIGHT = SCREEN_HEIGHT / 2;
    public static boolean PAUSE = false;

    private StarGame game;
    private SpriteBatch batch;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private GameOverScreen gameOverScreen;
    private OptionsScreen optionsScreen;
    private Screen targetScreen;
    private Viewport viewport;
    private Camera camera;

    public static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Camera getCamera() {
        return camera;
    }

    private ScreenManager() {
    }

    public void init(StarGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch, gameScreen);
        this.optionsScreen = new OptionsScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void resetCamera() {
        camera.position.set(HALF_SCREEN_WIDTH, HALF_SCREEN_HEIGHT,0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if (screen!=null) {
            screen.dispose();
        }
        resetCamera();
        game.setScreen(loadingScreen);
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAMEOVER:
                targetScreen = gameOverScreen;
                Assets.getInstance().loadAssets(ScreenType.GAMEOVER);
                break;
            case OPTIONS:
                targetScreen = optionsScreen;
                Assets.getInstance().loadAssets(ScreenType.OPTIONS);
                break;
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}

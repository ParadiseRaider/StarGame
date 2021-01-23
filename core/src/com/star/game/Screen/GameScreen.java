package com.star.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.game.Game.GameController;
import com.star.game.Game.WorldRenderer;
import com.star.game.Screen.Utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;
    private Stage stage;
    private BitmapFont font20;
    private BitmapFont font72;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    public GameController getGameController() {
        return gameController;
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gameController = new GameController(batch);
        this.worldRenderer = new WorldRenderer(gameController, batch);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font20;
        skin.add("simpleSkin", textButtonStyle);
        int cenX = Assets.getInstance().getAtlas().findRegion("simpleButton").getRegionWidth()/2;
        int cenY = Assets.getInstance().getAtlas().findRegion("simpleButton").getRegionHeight();
        Button btnGoMenu = new TextButton("Return menu", textButtonStyle);
        btnGoMenu.setPosition(ScreenManager.HALF_SCREEN_WIDTH-cenX, ScreenManager.SCREEN_HEIGHT-500-cenY*3f);
        btnGoMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                ScreenManager.PAUSE=!ScreenManager.PAUSE;
            }
        });
        stage.addActor(btnGoMenu);
        stage.addActor(gameController.getHero().getShop());
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        if (!ScreenManager.PAUSE) gameController.update(delta);
        worldRenderer.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.PAUSE = !ScreenManager.PAUSE;
            gameController.getHero().getShop().setVisible(true);
        }
        if (ScreenManager.PAUSE) {
            stage.draw();
            stage.act(delta);
        }
    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}

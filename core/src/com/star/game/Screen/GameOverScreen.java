package com.star.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.game.Game.Background;
import com.star.game.Screen.Utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private Background background;
    private GameScreen gs;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;

    public GameOverScreen(SpriteBatch batch, GameScreen gs) {
        super(batch);
        this.gs = gs;
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);
        int cenX = Assets.getInstance().getAtlas().findRegion("simpleButton").getRegionWidth()/2;
        int cenY = Assets.getInstance().getAtlas().findRegion("simpleButton").getRegionHeight();
        Button btnGoMenu = new TextButton("Return menu", textButtonStyle);
        btnGoMenu.setPosition(ScreenManager.HALF_SCREEN_WIDTH-cenX, ScreenManager.SCREEN_HEIGHT-500-cenY*3f);
        btnGoMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        stage.addActor(btnGoMenu);
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "Game Over", 0, 900, ScreenManager.SCREEN_WIDTH, 1, false);
        font24.draw(batch, "Score - "+gs.getGameController().getHero().getScore(), 0, 800, ScreenManager.SCREEN_WIDTH, 1, false);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}

package com.star.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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
import com.star.game.Game.KeysControl;
import com.star.game.Screen.Utils.Assets;
import com.star.game.Screen.Utils.OptionsUtils;

public class OptionsScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;
    private boolean change;
    private String[] str;
    private int count;
    private KeysControl keysControl;

    public OptionsScreen(SpriteBatch batch) {
        super(batch);
        this.change = false;
        this.count=0;
        this.str = new String[OptionsUtils.buttonMode.values().length];
        this.keysControl = new KeysControl(OptionsUtils.loadProperties(), "PLAYER1");
        for (int i = 0; i < str.length; i++) {
            str[i] = OptionsUtils.buttonMode.values()[i]+" - "+Input.Keys.toString(keysControl.getMass()[i]);
        }
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(),batch);
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
        Button btnUp = new TextButton("Change", textButtonStyle);
        Button btnGoMenu = new TextButton("Return menu", textButtonStyle);
        btnUp.setPosition(ScreenManager.HALF_SCREEN_WIDTH-cenX, ScreenManager.SCREEN_HEIGHT-500-cenY*1.5f);
        btnGoMenu.setPosition(ScreenManager.HALF_SCREEN_WIDTH-cenX, ScreenManager.SCREEN_HEIGHT-500-cenY*3f);

        btnUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!change) {
                    change = !change;
                }
            }
        });
        btnGoMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        stage.addActor(btnUp);
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
        font72.draw(batch, "Options", 0, 900, ScreenManager.SCREEN_WIDTH, 1, false);
        for (int i = 0; i < str.length; i++) {
            font24.draw(batch, str[i], 0, 800-50*i, ScreenManager.SCREEN_WIDTH, 1, false);
        }
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        for (int i = 0; i < str.length; i++) {
            str[i] = OptionsUtils.buttonMode.values()[i]+" - "+Input.Keys.toString(keysControl.getMass()[i]);
        }
        if (change) {
            for (int i = 0; i < str.length; i++) {
                str[i] = OptionsUtils.buttonMode.values()[i]+" - "+Input.Keys.toString(keysControl.getMass()[i]);
                if (i==count) {
                    str[i] = str[i] = "Press key to " + OptionsUtils.buttonMode.values()[count];
                }
            }
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {
                    OptionsUtils.changeButton(OptionsUtils.loadProperties(),"PLAYER1", count, keycode);
                    count++;
                    if (count >= str.length) {
                        count = 0;
                        change = false;
                        Gdx.input.setInputProcessor(stage);
                    }
                    return true;
                }
            });
        }
        background.update(dt);
        stage.act(dt);
        keysControl.update(OptionsUtils.loadProperties(), "PLAYER1");
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}

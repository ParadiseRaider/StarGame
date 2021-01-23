package com.star.game.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.star.game.Screen.ScreenManager;
import com.star.game.Screen.Utils.Assets;

public class Shop extends Group {
    public enum Type {
        MEDKIT("heal"),WEAPON("ammo");
        private String imageName;

        Type(String imageName) {
            this.imageName = imageName;
        }
    }
    private Hero hero;
    private BitmapFont font20;
    private BitmapFont font12;
    private float scale = 0.3f;

    public Shop(final Hero hero) {
        this.hero = hero;
        this.font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");
        this.font12 = Assets.getInstance().getAssetManager().get("fonts/font12.ttf");

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font20;
        skin.add("simpleSkin", textButtonStyle);

        int imgX = Assets.getInstance().getAtlas().findRegion("ammo").getRegionWidth();
        int imgY = Assets.getInstance().getAtlas().findRegion("ammo").getRegionHeight();

        final Image[][] lamps = new Image[hero.getSkills().length][];
        for (int i = 0; i < lamps.length; i++) {
            lamps[i] = new Image[hero.getSkills()[i].getMaxLvl()];
            for (int j = 0; j < lamps[i].length; j++) {
                Image img = new Image(skin.getDrawable("star5"));
                img.setColor(1, 0, 0, 1);
                img.setPosition(ScreenManager.SCREEN_WIDTH-400+imgX*scale+30+j*10,450-imgY*scale-50-i*50);
                img.setScale(1.0f);
                lamps[i][j] = img;
                this.addActor(img);
            }
        }

        Image[] skills = new Image[hero.getSkills().length];
        Label.LabelStyle labelStyle = new Label.LabelStyle(font12, Color.WHITE);
        final Label[] labels = new Label[hero.getSkills().length*2];
        for (int i = 0; i < skills.length; i++) {
            skills[i] = new Image(skin.getDrawable(hero.getSkills()[i].getType().imageName));
            final int i2=i;
            skills[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (hero.getSkills()[i2].getLevel()<=hero.getSkills()[i2].getMaxLvl()) {
                        if (hero.checkCost(hero.getSkills()[i2].getCost())) {
                            lamps[i2][hero.getSkills()[i2].getLevel() - 1].setColor(0, 1, 0, 1);
                            hero.getSkills()[i2].upgrage(hero.getSkills()[i2].getType(), 10 * hero.getSkills()[i2].getLevel());
                            labels[i2].setText("Price "+hero.getSkills()[i2].getCost());
                        }
                    }
                }
            });
            skills[i].setScale(0.3f);
            skills[i].setPosition(ScreenManager.SCREEN_WIDTH-400+imgX*scale,450-imgY*scale-50*(i+1));
            labels[i] = new Label("Price "+(hero.getSkills()[i].getCost()), labelStyle);
            labels[i].setPosition(ScreenManager.SCREEN_WIDTH-200,450-imgY*scale-50*(i+1));
            labels[hero.getSkills().length*2-1-i] = new Label(hero.getSkillsName()[i], labelStyle);
            labels[hero.getSkills().length*2-1-i].setPosition(ScreenManager.SCREEN_WIDTH-200,450-imgY*scale-50*(i+1)+20);
            this.addActor(skills[i]);
            this.addActor(labels[i]);
            this.addActor(labels[hero.getSkills().length*2-1-i]);
        }

        this.setPosition(20, 300);
        this.setVisible(false);
        skin.dispose();
    }
}

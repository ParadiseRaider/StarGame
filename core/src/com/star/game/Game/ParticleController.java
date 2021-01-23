package com.star.game.Game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.game.Game.Helpers.ObjectPool;
import com.star.game.Screen.Utils.Assets;

public class ParticleController extends ObjectPool<Particle> {
    public class EffectBuilder {
        public void buildMonsterSplash(float x, float y) {
            for (int i = 0; i < 15; i++) {
                float randomAngle = MathUtils.random(0, 6.28f);
                float randomSpeed = MathUtils.random(0, 50.0f);
            }
        }

        public void takeItemEffect(float x, float y) {
            for (int i = 0; i < 16; i++) {
                float angle = 360 / 16 * i;
                setup(x, y, MathUtils.cosDeg(angle) * 100f,
                        MathUtils.sinDeg(angle) * 100f,
                        0.8f, 3f, 2.8f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 0.4f
                );
            }
        }
    }

    private TextureRegion oneParticle;
    private EffectBuilder effectBuilder;
    private int cenX, cenY;

    public EffectBuilder getEffectBuilder() {
        return effectBuilder;
    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    public ParticleController() {
        this.oneParticle = Assets.getInstance().getAtlas().findRegion("star5");
        this.effectBuilder = new EffectBuilder();
        this.cenX = oneParticle.getRegionWidth()/2;
        this.cenY = oneParticle.getRegionHeight()/2;
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < activeList.size(); i++) {
            Particle p = activeList.get(i);
            float t = p.getTime() / p.getTimeMax();
            float scale = lerp(p.getSize1(), p.getSize2(), t);
            batch.setColor(lerp(p.getR1(), p.getR2(), t), lerp(p.getG1(), p.getG2(), t), lerp(p.getB1(), p.getB2(), t), lerp(p.getA1(), p.getA2(), t));
            batch.draw(oneParticle, p.getPosition().x - cenX, p.getPosition().y - cenY, cenX, cenY, cenX * 2, cenY * 2, scale, scale, 0);

        }
        batch.setColor(1f,1f,1f,1f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeList.size(); i++) {
            Particle p = activeList.get(i);
            float t = p.getTime() / p.getTimeMax();
            float scale = lerp(p.getSize1(), p.getSize2(), t);
            if (MathUtils.random(0, 200) < 3) {
                scale *= 5.0f;
            }
            batch.setColor(lerp(p.getR1(), p.getR2(), t), lerp(p.getG1(), p.getG2(), t), lerp(p.getB1(), p.getB2(), t), lerp(p.getA1(), p.getA2(), t));
            batch.draw(oneParticle, p.getPosition().x - cenX, p.getPosition().y - cenY, cenX, cenY, cenX * 2, cenY * 2, scale, scale, 0);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setup(float x, float y, float vx, float vy, float timeMax, float size1,
                      float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        Particle item = getActiveElement();
        item.init(x, y, vx, vy, timeMax, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public float lerp(float value1, float value2, float point) {
        return value1 + (value2-value1)*point;
    }
}

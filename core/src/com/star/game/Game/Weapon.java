package com.star.game.Game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.game.Screen.Utils.Assets;

public class Weapon {
    private GameController gc;
    private Hero hero;
    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    private Vector3[] slots;
    private Sound shootSound;

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public int getDamage() {
        return damage;
    }

    public void addAmmo(int amount) {
        curBullets+=amount;
        if (curBullets>maxBullets) curBullets=maxBullets;
    }

    public void upgradeDamage(int amount) {
        damage+=amount;
    }

    public Weapon(GameController gc, Hero hero, String title, float firePeriod, int damage, float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.hero = hero;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = this.maxBullets;
        this.slots = slots;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/Shoot.mp3");
    }

    public void fire() {
        if (curBullets>0) {
            curBullets--;
            shootSound.play();
            for (int i = 0; i < slots.length; i++) {
                float x,y,vx,vy;
                x = hero.getPosition().x + slots[i].x * MathUtils.cosDeg(hero.getAngle() + slots[i].y);
                y = hero.getPosition().y + slots[i].x * MathUtils.sinDeg(hero.getAngle() + slots[i].y);
                vx = hero.getVelocity().x + bulletSpeed * MathUtils.cosDeg(hero.getAngle() + slots[i].z);
                vy = hero.getVelocity().y + bulletSpeed * MathUtils.sinDeg(hero.getAngle() + slots[i].z);
                gc.getBulletController().setup(x, y, vx, vy, hero.getAngle() + slots[i].z, true, 1000f);
            }
        }
    }
}

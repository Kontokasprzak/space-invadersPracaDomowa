package com.samsung.business.spaceinvaders.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.samsung.business.spaceinvaders.entity.Shoot;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Wqawer on 2018-03-29.
 */

public class Brick {
    int hp;
    private Texture texture;
    TextureRegion[][] textureFrames;
    Rectangle position;
    int x;
    int y;
    public Brick(int x,int y) {
        hp=3;
        position = new Rectangle(x,y,16,16);
        texture=new Texture("Bricks.png");
        textureFrames = TextureRegion.split( texture,16,16);
        this.x=x;
        this.y=y;
        }
    public void render(SpriteBatch batch) {
        TextureRegion drawFrame= textureFrames[0][3-hp];
        batch.draw(drawFrame, x, y);
    }
    public boolean isHit(Shoot shoot){
  boolean isHit = shoot.position().overlaps(this.position);
		if(isHit){
            Gdx.app.log("Brick"+x,"HIT");
            hp--;
            hp();
      //  notifyAllOnSpaceshipHit();
    }
		return isHit;}
    boolean hp(){
        return hp==0;
    }

}








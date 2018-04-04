package com.samsung.business.spaceinvaders.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.samsung.business.spaceinvaders.entity.Shoot;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wqawer on 2018-03-29.
 */

public class Cover {
    private List<Brick> brickList;
    Rectangle position;


    public Cover(int startPositionX, int startPositionY){
        position = new Rectangle(startPositionX,startPositionY,64,32);
        this.brickList = new ArrayList<>();
        brickList.add(new Brick(startPositionX,startPositionY));
        brickList.add(new Brick(startPositionX,startPositionY+16));
        brickList.add(new Brick(startPositionX+16,startPositionY+16));
        brickList.add(new Brick(startPositionX+32,startPositionY+16));
        brickList.add(new Brick(startPositionX+48,startPositionY+16));
        brickList.add(new Brick(startPositionX+48,startPositionY));
    }
    public void render(SpriteBatch batch){
        for (int i = 0; i < brickList.size(); i++){
            brickList.get(i).render(batch);}

    }
    public boolean isHit(Shoot shoot){
        boolean isHit = shoot.position().overlaps(this.position);
        if(isHit){
           // notifyAllOnSpaceshipHit();

            Gdx.app.log("Cover","Overlaps");
            for(Brick x:brickList){
                if(x.isHit(shoot)){
                    for(int i=0;i<brickList.size();i++){if(brickList.get(i).hp()){brickList.remove(i);}}return isHit;}
            }
        }
        return false;

    }

    }







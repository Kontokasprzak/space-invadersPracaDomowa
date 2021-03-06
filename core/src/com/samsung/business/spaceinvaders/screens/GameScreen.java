package com.samsung.business.spaceinvaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.samsung.business.spaceinvaders.Objects.Brick;
import com.samsung.business.spaceinvaders.Objects.Cover;
import com.samsung.business.spaceinvaders.SpaceInvaders;
import com.samsung.business.spaceinvaders.entity.EnemySpaceship;
import com.samsung.business.spaceinvaders.entity.Invasion;
import com.samsung.business.spaceinvaders.entity.PlayerSpaceship;
import com.samsung.business.spaceinvaders.entity.Spaceship;
import com.samsung.business.spaceinvaders.manager.GraphicsManager;
import com.samsung.business.spaceinvaders.manager.ShootManager;
import com.samsung.business.spaceinvaders.ui.DisplayInfo;
import com.samsung.business.spaceinvaders.ui.TouchInput;
import com.samsung.business.spaceinvaders.ui.components.ScoreGuiLabel;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends AbstractScreen {
    private final SpaceInvaders spaceInvaders;

    private OrthographicCamera camera;

    private ShootManager shootManager;
    private GraphicsManager graphicsManager;
    List<Cover> coverList;
    private PlayerSpaceship player;
    private Invasion invasion;
    private Cover cover;

    private ScoreGuiLabel scoreGuiLabel;
    private TouchInput touchInput;

    private float animationTime;

    public GameScreen(SpaceInvaders spaceInvaders) {
        this.spaceInvaders = spaceInvaders;
        scoreGuiLabel = new ScoreGuiLabel();
        create();
    }

    public void create() {

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, DisplayInfo.getWidth(), DisplayInfo.getHeight());

        touchInput = new TouchInput(camera);

        //zaladuj tekstury
        graphicsManager = GraphicsManager.loadGraphics();

        //utworz rakiete gracza
        player = new PlayerSpaceship(graphicsManager.find("rakieta"), camera);
        player.registerOnSpaceshipHit(new Spaceship.OnSpaceshipHit() {
            @Override
            public void onSpaceshipHit() {
                spaceInvaders.gameOver();
                dispose();
            }
        });

        //przygotuj raid wroga
        invasion = Invasion.raid(graphicsManager);

        invasion.listenOnDestroyed(new Invasion.OnEnemyDestroyed() {
            @Override
            public void onEnemyDestroyed(EnemySpaceship enemy) {
                spaceInvaders.getScore().addScore(100);
            }
        });

        invasion.listenOnInvasionDestroyed(new Invasion.OnInvasionDestroyed() {
            @Override
            public void onInvasionDestroyed() {
                spaceInvaders.win();
                dispose();
            }
        });
        coverList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            coverList.add(new Cover((Gdx.graphics.getWidth()/6)*i+10, 100));
        }


        //zaladuj system zarzadzania pociskami
        shootManager = new ShootManager(graphicsManager, player, invasion, coverList);
        shootManager.onMissed(() -> {
            spaceInvaders.getScore().addScore(-10);
        });

        //ustawianie osłon

    }

    private void updatGameState() {
        //zaktualizuj stan i polozenie gracza i wrogow
        player.update(camera, shootManager);
        invasion.update(camera, shootManager);

        //zaktualizuj stan pociskow
        shootManager.updateShots();


        scoreGuiLabel.setScore(spaceInvaders.getScore().getValue());
    }

    @Override
    public void render(float delta) {
        animationTime+= delta;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        spaceInvaders.batch.setProjectionMatrix(camera.combined);

        //renderowanie gry
        spaceInvaders.batch.begin();

        player.render(spaceInvaders.batch, animationTime);
        shootManager.render(spaceInvaders.batch, animationTime);
        invasion.render(spaceInvaders.batch, animationTime);
        scoreGuiLabel.render(spaceInvaders.batch, animationTime);
        for(Cover x: coverList)
        {x.render(spaceInvaders.batch);}
        spaceInvaders.batch.end();

        updatGameState();
        if (touchInput.exit()){
            spaceInvaders.setScreen(new MainMenuScreen(spaceInvaders));
            spaceInvaders.getScore().reset();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.update();
    }

    @Override
    public void dispose() {
    }
}
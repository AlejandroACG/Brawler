package com.svalero.brawler.managers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import static com.svalero.brawler.utils.Constants.ASPECT_RATIO;

public class CameraManager {
    private final OrthographicCamera camera = new OrthographicCamera();
    private final OrthographicCamera uiCamera = new OrthographicCamera();
    private final LevelManager levelManager;

    public CameraManager(LevelManager levelManager) {
        this.levelManager = levelManager;
        camera.update();
        float cameraWidth = levelManager.getMapHeight() * ASPECT_RATIO;
        camera.setToOrtho(false, cameraWidth, levelManager.getMapHeight());

        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        uiCamera.update();
    }

    public void handleCamera() {
        float cameraX = Math.max(camera.viewportWidth / 2f, levelManager.getPlayer().getPosition().x + 30);
        cameraX = Math.min(cameraX, levelManager.getMapWidth() - camera.viewportWidth / 2f);

        camera.position.set(cameraX, levelManager.getMapHeight() / 2, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public OrthographicCamera getUICamera() { return uiCamera; }
}

package com.epicness.controllersdemo;

import static com.epicness.controllersdemo.Constants.SHAPE_SIZE;
import static com.epicness.controllersdemo.Constants.WORLD_HEIGHT;
import static com.epicness.controllersdemo.Constants.WORLD_WIDTH;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class ControllersDemo extends ApplicationAdapter {

    // --- Rendering ---
    private ShapeRenderer shapeRenderer;
    private Vector2 shapePosition;

    private ControllersListener controllersListener;

    @Override
    public void create() {
        Gdx.app.log("App", "Creating application...");

        shapeRenderer = new ShapeRenderer();

        // Center the shape initially
        shapePosition = new Vector2(WORLD_WIDTH / 2f - SHAPE_SIZE / 2f, WORLD_HEIGHT / 2f - SHAPE_SIZE / 2f);

        // --- IMPORTANT: Add this class as a Controller Listener ---
        controllersListener = new ControllersListener(shapePosition);
        Controllers.addListener(controllersListener);
        Gdx.app.log("Controller", "Controller Listener added.");
    }

    @Override
    public void render() {
        // --- Clear Screen ---
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1); // Dark blue background

        // --- Handle Input and Update Logic ---
        controllersListener.handleInput(Gdx.graphics.getDeltaTime());

        // --- Rendering ---
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw the shape
        shapeRenderer.setColor(Color.GREEN); // Green if connected, Red if not
        shapeRenderer.rect(shapePosition.x, shapePosition.y, SHAPE_SIZE, SHAPE_SIZE);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        Gdx.app.log("App", "Disposing resources...");
        shapeRenderer.dispose();
        // --- IMPORTANT: Remove the listener ---
        Controllers.removeListener(controllersListener);
        Gdx.app.log("Controller", "Controller Listener removed.");
    }
}
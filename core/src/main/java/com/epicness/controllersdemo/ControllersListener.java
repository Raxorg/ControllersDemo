package com.epicness.controllersdemo;

import static com.epicness.controllersdemo.Constants.MOVE_SPEED;
import static com.epicness.controllersdemo.Constants.SHAPE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ControllersListener implements ControllerListener {

    // --- Game State ---
    private Controller activeController;
    private final Vector2 shapePosition;
    private final int worldWidth, worldHeight;

    public ControllersListener(Vector2 shapePosition) {
        // Check if any controllers are already connected at startup
        Gdx.app.log("Controller", "Available controllers on startup:");
        for (int i = 0; i < Controllers.getControllers().size; i++) {
            Controller controller = Controllers.getControllers().get(i);
            Gdx.app.log("Controller", "  -> " + controller.getName() + " | ID: " + controller.getUniqueId());
            if (activeController == null) {
                activeController = controller; // Assign the first one found
                Gdx.app.log("Controller", "Setting startup controller as active: " + activeController.getName());
            }
        }
        if (activeController == null) {
            Gdx.app.log("Controller", "No controllers detected at startup.");
        }
        this.shapePosition = shapePosition;
        worldWidth = Gdx.graphics.getWidth();
        worldHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void connected(Controller controller) {
        Gdx.app.log("Controller", "Connected: " + controller.getName() + " | Instance ID: " + controller.getUniqueId());
        // If we don't have an active controller yet, use this one
        if (activeController == null) {
            Gdx.app.log("Controller", "Setting as active controller: " + controller.getName());
            activeController = controller;
        }
        // You could also iterate Controllers.getControllers() to pick a specific one
    }

    @Override
    public void disconnected(Controller controller) {
        Gdx.app.log("Controller", "Disconnected: " + controller.getName() + " | Instance ID: " + controller.getUniqueId());
        // If the disconnected controller was our active one, clear it
        if (activeController != null && activeController.getUniqueId().equals(controller.getUniqueId())) {
            Gdx.app.log("Controller", "Active controller disconnected.");
            activeController = null;
            // Optional: Try to find another connected controller
            if (!Controllers.getControllers().isEmpty()) {
                activeController = Controllers.getControllers().first(); // Get the next available
                Gdx.app.log("Controller", "Switched active controller to: " + activeController.getName());
            }
        }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        Gdx.app.log("ControllerInput", controller.getName() + " | Button Down: " + buttonCode);
        // Return false so other listeners can process the event too if needed
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Gdx.app.log("ControllerInput", controller.getName() + " | Button Up: " + buttonCode);
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        // We handle axis movement directly in render(), but you could log it here too
        // Gdx.app.log("ControllerInput", controller.getName() + " | Axis Moved: " + axisCode + " | Value: " + value);
        return false;
    }

    public void handleInput(float deltaTime) {
        if (activeController == null) return;

        // --- Axis Input for Movement ---
        float xAxis = activeController.getAxis(activeController.getMapping().axisLeftX);
        float yAxis = activeController.getAxis(activeController.getMapping().axisLeftY); // Note: Y axis might be inverted (-1 is up)

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            System.out.println(xAxis);
            System.out.println(yAxis);
        }
        // Apply a deadzone to avoid drift
        float deadzone = 0.2f;
        if (Math.abs(xAxis) < deadzone) xAxis = 0;
        if (Math.abs(yAxis) < deadzone) yAxis = 0;

        // Update position based on axis input
        // Remember yAxis might be inverted: positive value often means "down"
        shapePosition.x += xAxis * MOVE_SPEED * deltaTime;
        shapePosition.y -= yAxis * MOVE_SPEED * deltaTime; // Use minus if Y is inverted

        // --- Boundary Checks ---
        // Keep shape within world bounds
        shapePosition.x = MathUtils.clamp(shapePosition.x, 0f, worldWidth - SHAPE_SIZE);
        shapePosition.y = MathUtils.clamp(shapePosition.y, 0f, worldHeight - SHAPE_SIZE);
    }
}
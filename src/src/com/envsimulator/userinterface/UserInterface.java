package com.envsimulator.userinterface;

import com.envsimulator.simulationengine;

import tv.ouya.console.api.OuyaController;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;


public class UserInterface extends GLSurfaceView {

    private SimulationEngine engine;
    private StartMenu startMenu;
    private GameMenu gameMenu;
    private MainSimulationLoop simLoop;

    private Screen screen;
    private StartOption startOption;
    private int xTile;
    private int yTile;
    private final int xTileMax = 5;
    private final int yTileMax = 5;
    Boolean simMenuOpen;

    Render render;

    public UserInterface(Context context) {
        super(context);

        startMenu = new StartMenu();
        gameMenu = new GameMenu();

        render = new Render();
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // Only update on request
        
        // State of the user Interface
        screen = Screen.START_MENU;
        startOption = StartOption.NEW_SIM;
        simMenuOpen = false;
        xTile = 1;
        yTile = 1;
    }

    enum Screen {
        START_MENU,
        GAME_MENU,
        SIMULATION
    };

    enum StartOption {
        NEW_SIM,
        LOAD_SIM
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;

        //Handle the input
        switch(keyCode){
        case OuyaController.BUTTON_O:
            if (this.screen == Screen.SIMULATION) { // Back
                this.render.setScreen(Screen.START_MENU);
                this.render.requestRender();
            }
            else if (this.screen == Screen.GAME_MENU) { // Back
                this.render.setScreen(Screen.SIMULATION);
                this.render.requestRender();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_U:
            if (this.screen == Screen.SIMULATION) { // Game Menu
                this.render.setScreen(Screen.GAME_MENU);
                this.render.requestRender();
            }
            else if (this.screen == Screen.GAME_MENU) { // Exit Menu
                this.render.setScreen(Screen.SIMULATION);
                this.render.requestRender();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_Y:
            if (this.screen == Screen.SIMULATION) { // Step
                StepSimulation();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_A:
            if (this.screen == Screen.START_MENU) { // Select
                if (startOption == StartOption.LOAD_SIM) {
                    LoadSimulation();
                }
                else if (startOption == StartOption.NEW_SIM) {
                    NewSimulation();
                }
            }
            else if (this.screen == Screen.SIMULATION) { // Select
                simMenuOpen = !simMenuOpen;
                this.render.setSimMenu(simMenuOpen);
                this.render.requestRender();
            }
            else if (this.screen == Screen.GAME_MENU) { // Select
                this.render.setScreen(Screen.SIMULATION);
                this.render.requestRender();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_UP:
            if (this.screen == Screen.START_MENU) {
                if (startOption == StartOption.NEW_SIM)
                    startOption = StartOption.LOAD_SIM;
                else
                    startOption = StartOption.NEW_SIM;
            }
            else if (this.screen == Screen.SIMULATION) {
                if (yTile > 1) yTile--;
                this.render.setGridCoord(xTile, yTile);
                this.render.requestRender();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_DOWN:
            if (this.screen == Screen.START_MENU) {
                if (startOption == StartOption.NEW_SIM)
                    startOption = StartOption.LOAD_SIM;
                else
                    startOption = StartOption.NEW_SIM;
            }
            else if (this.screen == Screen.SIMULATION) {
                if (yTile < yTileMax) yTile++;
                this.render.setGridCoord(xTile, yTile);
                this.render.requestRender();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_LEFT:
            if (this.screen == Screen.SIMULATION) {
                if (xTile > 1) xTile--;
                this.render.setGridCoord(xTile, yTile);
                this.render.requestRender();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_RIGHT:
            if (this.screen == Screen.SIMULATION) {
                if (xTile < xTileMax) xTile++;
                this.render.setGridCoord(xTile, yTile);
                this.render.requestRender();
            }
            handled = true;
            break;
        }
        return handled || super.onKeyDown(keyCode, event);
    }

    protected void StepSimulation() {
        engine.step();
    }

    protected void LoadSimulation() {
        engine = startMenu.loadSimulation();
        BeginSimulation();
    }

    protected void SaveSimulation() {
        startMenu.saveSimulation();
    }

    protected void NewSimulation() {
        engine = new SimulationEngine();
        BeginSimulation();
    }
    
    protected void BeginSimulation() {
        simLoop = new MainSimulationLoop(this, engine);
        simLoop.setRunning(true);
        simLoop.start();
    }

    protected void StopSimulation() {
        simLoop.setRunning(false);
        SaveSimulation();
    }
}

package com.envsimulator.game;

import com.envsimulator.userinterface.*;
import com.envsimulator.simulationengine.*;
import com.envsimulator.eventqueue.*;

public class MainSimulationLoop extends Thread {
    public UserInterface userInterface;
    public SimulationEngine engine;
	private SimulationEngine simulationEngine;

    public MainSimulationLoop (UserInterface userInterface, SimulationEngine engine) {
        super();
        this.userInterface = userInterface;
        this.simulationEngine = engine;
    }

    private boolean running;
    
    public void setRunning(boolean running) {
        this.running = running;
    }
    
//    public void run() {
//        while (running) {
//            Event event = eventQueue.getEvent();
//            if (event instanceof SimulationEvent) {
//                if (!simulationEngine.simulateOneEvent((SimulationEvent)event))
//                    eventQueue.popEvent();
//            }
//            // Handle render events
//        }
//    }
}
package com.envsimulator.game;

import com.envsimulator.userinterface.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {
    private UserInterface userInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userInterface = new UserInterface(this);
        setContentView(userInterface);
    }
    
    // Not really sure if these are needed or not
    @Override
    protected void onResume() {
        super.onResume();
        userInterface.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        userInterface.onPause();
    }
}

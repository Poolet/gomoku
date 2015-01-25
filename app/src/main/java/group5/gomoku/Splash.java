package group5.gomoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    // Set the splash screen time shown
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                //Once the splash is done showing, go to the main activity
                Intent i = new Intent(Splash.this, HomeMenu.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
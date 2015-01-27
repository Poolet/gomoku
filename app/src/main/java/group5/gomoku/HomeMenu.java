package group5.gomoku;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;


public class HomeMenu extends ActionBarActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        View btnPlay = (Button) findViewById(R.id.play_button);
        btnPlay.setOnClickListener(this);
        View btnRules = (Button) findViewById(R.id.rules_button);
        btnRules.setOnClickListener(this);
        View btnSettings = (Button) findViewById(R.id.settings_button);
        btnSettings.setOnClickListener(this);
        View btnExit = (Button) findViewById(R.id.exit_button);
        btnExit.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                startActivity(new Intent(this, SelectPlayer.class));
                break;
            case R.id.rules_button:
                startActivity(new Intent(this, rules.class));
                break;
            case R.id.settings_button:
                break;
            case R.id.exit_button:
                finish();
                break;
        }
    }
}


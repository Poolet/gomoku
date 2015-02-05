package group5.gomoku;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;


public class SelectPlayer extends ActionBarActivity implements OnClickListener {

    Intent i;
    Bundle gameMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);

        View btnPlayOnline = (Button) findViewById(R.id.play_online_button);
        btnPlayOnline.setOnClickListener(this);
        View btnOffline = (Button) findViewById(R.id.play_offline_button);
        btnOffline.setOnClickListener(this);
        View btnSinglePlayer = (Button) findViewById(R.id.single_player_button);
        btnSinglePlayer.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_online_button:
                break;
            case R.id.play_offline_button:
                i = new Intent(this, SelectBoard.class);
                gameMode = new Bundle();
                gameMode.putBoolean("AI", false);
                i.putExtras(gameMode);
                startActivity(i);
                break;
            case R.id.single_player_button:
                i = new Intent(this, SelectBoard.class);
                gameMode = new Bundle();
                gameMode.putBoolean("AI", true);
                i.putExtras(gameMode);
                startActivity(i);
                break;
        }
    }
}


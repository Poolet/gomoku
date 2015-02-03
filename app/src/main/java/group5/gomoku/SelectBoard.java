package group5.gomoku;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;


public class SelectBoard extends ActionBarActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_board);

        View btnPlayOnline = (Button) findViewById(R.id.size_10_button);
        btnPlayOnline.setOnClickListener(this);
        View btnOffline = (Button) findViewById(R.id.size_15_button);
        btnOffline.setOnClickListener(this);
        View btnSinglePlayer = (Button) findViewById(R.id.size_20_button);
        btnSinglePlayer.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {
        Intent i;
        Bundle boardSize = new Bundle();
        switch (v.getId()) {
            case R.id.size_10_button:
                i = new Intent(this, Board.class);
                boardSize.putInt("boardSize", 10);
                i.putExtras(boardSize);
                startActivity(i);
                break;
            case R.id.size_15_button:
                i = new Intent(this, Board.class);
                boardSize.putInt("boardSize", 15);
                i.putExtras(boardSize);
                startActivity(i);
                break;
            case R.id.size_20_button:
                i = new Intent(this, Board.class);
                boardSize.putInt("boardSize", 20);
                i.putExtras(boardSize);
                startActivity(i);
                break;
        }
    }
}


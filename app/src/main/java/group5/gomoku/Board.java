package group5.gomoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

/**
 * Created by Maithily on 1/25/2015.
 */
public class Board extends Activity implements View.OnClickListener{

    BoardView grid;
    int score1;
    int score2;
    //Chronometer cr= (Chronometer) findViewById(R.id.chronometer);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Bundle bundle = getIntent().getExtras();
        int boardSize = bundle.getInt("boardSize");
        boolean AI = bundle.getBoolean("AI");
        grid = (BoardView)findViewById(R.id.board_grid);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        grid.setParent(this, chronometer);
        try {
            score1 = bundle.getInt("score1");
            score2 = bundle.getInt("score2");
        }catch(NullPointerException e)
        {
            score1 = 0;
            score2 = 0;
        }

        grid.setScoreValue1(score1);
        grid.setScoreValue2(score2);
        grid.setGridDimension(boardSize);
        grid.setAI(AI);
        grid.Init();
        grid.invalidate();

      //  cr.start();
      //  cr.setOnClickListener(this);

        Button back=(Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        Button quit =(Button) findViewById(R.id.button_quit);
        quit.setOnClickListener(this);
        Button scores =(Button) findViewById(R.id.button_scores);
        scores.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        //Chronometer.setBase(SystemClock.elapsedRealtime());


        switch (v.getId()) {
            case R.id.button_back:
                startActivity(new Intent(this, SelectBoard.class));
                break;
            case R.id.button_quit:
                startActivity(new Intent(this, HomeMenu.class));
                break;
            case R.id.button_scores:
                startActivity(new Intent(this, Scores.class));
                break;
        }
    }
}

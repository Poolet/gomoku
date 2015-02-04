package group5.gomoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Maithily on 1/25/2015.
 */
public class Board extends Activity implements View.OnClickListener{

    BoardView grid;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Bundle bundle = getIntent().getExtras();
        int boardSize = bundle.getInt("boardSize");
        grid = (BoardView)findViewById(R.id.board_grid);
        grid.setParent(this);
        grid.setGridDimension(boardSize);
        grid.Init();
        grid.invalidate();

        Button back=(Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        Button quit =(Button) findViewById(R.id.button_quit);
        quit.setOnClickListener(this);
        Button scores =(Button) findViewById(R.id.button_scores);
        scores.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
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

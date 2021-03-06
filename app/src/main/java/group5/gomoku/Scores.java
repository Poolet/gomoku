package group5.gomoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Maithily on 1/25/2015.
 */
public class Scores extends ActionBarActivity implements View.OnClickListener {

    TextView Scores1TextView;
    TextView Scores2TextView;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        setContentView(R.layout.activity_scores);
        Scores1TextView = (TextView)findViewById(R.id.editText);
        Scores2TextView = (TextView)findViewById(R.id.editText2);
        String sc1,sc2;

        sc1 = "" + bundle.getInt("score1");
        sc2 = "" + bundle.getInt("score2");


        Scores1TextView.setText(sc1);
        Scores2TextView.setText(sc2);

        View btnPlayAgn = (Button) findViewById(R.id.button_playAgain);
        btnPlayAgn.setOnClickListener(this);
        View btnHome = (Button) findViewById(R.id.button_home);
        btnHome.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getIntent().getExtras();
        switch (v.getId()) {
            case R.id.button_playAgain:
                Bundle gameInfo = new Bundle();
                gameInfo.putBoolean("AI", bundle.getBoolean("AI"));
                Intent i=new Intent();
                i.putExtras(gameInfo);
                i.setClass(this, SelectBoard.class);
                startActivity(i);
                break;
            case R.id.button_home:
                startActivity(new Intent(this, HomeMenu.class));
                break;
        }
    }


}

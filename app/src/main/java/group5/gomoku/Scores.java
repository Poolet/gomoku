package group5.gomoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Maithily on 1/25/2015.
 */
public class Scores extends ActionBarActivity implements View.OnClickListener {

    TextView Scores1TextView;
    TextView Scores2TextView;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Scores1TextView = (TextView)findViewById(R.id.editText);
        Scores2TextView = (TextView)findViewById(R.id.editText2);
        String sc1="",sc2="";
        Intent extras = getIntent();

            sc1 = extras.getStringExtra("score1");
            sc2 = extras.getStringExtra("score2");


        Scores1TextView.setText(""+sc1);
        Scores2TextView.setText(""+sc2);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_playAgain:
                startActivity(new Intent(this,Board.class));
                break;
            case R.id.button_home:
                startActivity(new Intent(this, HomeMenu.class));
                break;
        }
    }


}

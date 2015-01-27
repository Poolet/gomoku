package group5.gomoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Maithily on 1/25/2015.
 */
public class Scores extends ActionBarActivity implements View.OnClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);


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

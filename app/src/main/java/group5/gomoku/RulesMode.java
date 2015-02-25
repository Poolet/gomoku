package group5.gomoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by typut_000 on 2/24/2015.
 */
public class RulesMode extends ActionBarActivity implements View.OnClickListener  {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_modes);

        View btnOk = (Button) findViewById(R.id.btnOK);
        btnOk.setOnClickListener(this);

        View btnPrev = (Button) findViewById(R.id.gameModePrevious);
        btnPrev.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                startActivity(new Intent(this, HomeMenu.class));
                break;

            case R.id.gameModePrevious:
                startActivity(new Intent(this, rules.class));
                break;
        }
    }
}

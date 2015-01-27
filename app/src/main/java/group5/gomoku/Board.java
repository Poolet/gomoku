package group5.gomoku;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Maithily on 1/25/2015.
 */
public class Board extends ActionBarActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView imageview = (ImageView)v;
                imageview.setImageResource(R.drawable.board_button_black);
            }
        });
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

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return 100;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(130, 105));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[0]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.board_button
        };
    }
}

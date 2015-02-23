package group5.gomoku;

import com.example.android.bluetoothchat.BluetoothChatService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class SelectPlayer extends ActionBarActivity implements OnClickListener {

    Intent i;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int SUCCESS_CONNECT=0;
    private static final int MESSAGE_READ=1;
    Bundle gameMode;
    Set<BluetoothDevice> pairedDevices;
    Set<BluetoothDevice> availableDevices;
    BluetoothAdapter BA;
    ArrayAdapter<String> listAdapter;
    ArrayAdapter<String> pDevices;
    ArrayAdapter<String> aDevices;
    private BluetoothChatService mChatService = null;




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
            case R.id.single_player_button:
                i = new Intent(this, SelectBoard.class);
                gameMode = new Bundle();
                gameMode.putBoolean("AI", true);
                i.putExtras(gameMode);
                startActivity(i);
                break;

            case R.id.play_offline_button:
                i = new Intent(this, SelectBoard.class);
                gameMode = new Bundle();
                gameMode.putBoolean("AI", false);
                i.putExtras(gameMode);
                startActivity(i);
                break;

            case R.id.play_online_button:
                i = new Intent(this, MultiPlayer.class);
                gameMode = new Bundle();
                gameMode.putBoolean("AI", false);
                i.putExtras(gameMode);
                startActivity(i);

                //mChatService= BluetoothAdapter.getDefaultAdapter();

                //availableDevices = BA.get




                //i = new Intent(this, SelectBoard.class);
                //gameMode = new Bundle();
                //gameMode.putBoolean("AI", false);
                //i.putExtras(gameMode);
                //startActivity(i);
                break;
        }
    }





 //   @Override
    //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


       // Object o[]=pairedDevices.toArray();
      //  BluetoothDevice selectedDevice;
     //   selectedDevice = (BluetoothDevice)o[position];
     //   ConnectThread connectThread = new ConnectThread(selectedDevice);
     //   connectThread.start();
  //  }
}


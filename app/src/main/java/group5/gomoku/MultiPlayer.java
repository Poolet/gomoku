package group5.gomoku;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Maithily on 2/22/2015.
 */
public class MultiPlayer extends Board implements View.OnClickListener {
    private static final int CONNECTION_SUCCESS = 0;
    BoardView gridServer;
    BoardView gridClient;
    View getDevices;
    View hostGame;
    View joinGame;
    private static MediaPlayer newGame;
    Chronometer chronometer;
    int turn;
    int mode = 1;
    private static final int CLIENT_READY = 2;
    RelativeLayout rLayoutBoard;
    RelativeLayout rLayout;
    BluetoothSocket writerCSocket;
    BluetoothSocket writerSSocket;
    ListView LVlistView;
    IntentFilter iFilter;
    BroadcastReceiver bReceiver;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter BA;
    private static final int SUCCESS_CONNECT = 0;
    private static final int MESSAGE_READ = 1;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> listAdapter;
    ArrayList list = new ArrayList();
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECTION_SUCCESS:
                    // DO something
                    //ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    Toast.makeText(getApplicationContext(), "CONNECT", Toast.LENGTH_SHORT).show();
                    MultiPlayer.this.runOnUiThread(new Runnable() {
                        public void run() {
                            rLayoutBoard.setVisibility(View.VISIBLE);
                            rLayout.setVisibility(View.INVISIBLE);
                            gridServer.setVisibility(View.INVISIBLE);

                            chronometer = (Chronometer) findViewById(R.id.chronometer);
                            MediaPlayer mp = MediaPlayer.create(MultiPlayer.this, R.raw.new_game);
                            mp.start();
                            gridClient.setParent(MultiPlayer.this, chronometer);
                            gridClient.setGridDimension(10);
                            gridClient.setOnline(true);
                            gridClient.setTurn(false);
                            gridClient.Init();
                            gridClient.setIsBlack(false);
                            gridClient.setPlayerName("Player 2");
                            gridClient.invalidate();

                            //draw board for client here
                        }
                    });

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String s = new String(readBuf);
                    Toast.makeText(getApplicationContext(), "Your turn!", Toast.LENGTH_SHORT).show();
                    gridClient.setTurn(!gridClient.getTurn());
                    gridServer.setTurn(!gridServer.getTurn());
                    Scanner in = new Scanner(s);
                    int isClient = Integer.parseInt(String.valueOf(in.findInLine(".").charAt(0)));
                    s = in.next();
                    if(isClient == 1) {
                        gridServer.playPiecePlaced();
                        gridServer.resetChronometer();
                        gridServer.decodeGameState(10, s);
                        gridServer.invalidate();
                        if(gridServer.checkSuccess(2, 0))
                        {
                            gridServer.initGameBoard(gridServer.getGridDimension() - 1);
                            gridServer.invalidate();
                        };

                    }
                    else {
                        gridClient.playPiecePlaced();
                        gridClient.resetChronometer();
                        gridClient.decodeGameState(10, s);
                        gridClient.invalidate();
                        if(gridClient.checkSuccess(1, 0))
                        {
                            gridClient.initGameBoard(gridClient.getGridDimension() - 1);
                            gridClient.setTurn(false);
                            gridClient.invalidate();
                        };
                    }

                    break;

                case CLIENT_READY:
                    MultiPlayer.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "In client ready", Toast.LENGTH_SHORT).show();
                            rLayoutBoard.setVisibility(View.VISIBLE);
                            rLayout.setVisibility(View.INVISIBLE);
                            gridClient.setVisibility(View.INVISIBLE);
                            //draw board for server here
                            newGame = MediaPlayer.create(MultiPlayer.this, R.raw.new_game);
                            newGame.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();

                                };
                            });
                            newGame.start();
                            chronometer = (Chronometer) findViewById(R.id.chronometer);
                            gridServer.setParent(MultiPlayer.this, chronometer);
                            gridServer.setGridDimension(10);
                            gridServer.setOnline(true);
                            gridServer.setIsBlack(true);
                            gridServer.setTurn(true);
                            gridServer.Init();
                            gridServer.setPlayerName("Player 1");
                            gridServer.invalidate();
                        }
                    });
                    break;
            }
        }
    };

    protected void swapTurns()
    {
        gridServer.setTurn(!gridServer.getTurn());
        gridClient.setTurn(!gridClient.getTurn());
        String s;
        if(mode==0)
        {
            ConnectedThread con =new ConnectedThread(writerSSocket);
            s = gridServer.packageGameState(gridServer.getGridDimension() - 1, 0);
            if(gridServer.checkSuccess(1, 0)) {
                gridServer.initGameBoard(gridServer.getGridDimension() - 1);
                gridServer.invalidate();
                gridServer.setTurn(true);
            }
            con.write(s.getBytes());
        }
        else
        {
            ConnectedThread con = new ConnectedThread(writerCSocket);
                s = gridClient.packageGameState(gridClient.getGridDimension() - 1, 1);
            if(gridClient.checkSuccess(2, 0)) {
                gridClient.initGameBoard(gridClient.getGridDimension() - 1);
                gridClient.invalidate();
            }
            con.write(s.getBytes());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutliplayer);
        gridServer = (BoardView)findViewById(R.id.board_grid);
        gridClient = (BoardView)findViewById(R.id.board_grid2);
        hostGame = (Button) findViewById(R.id.button);
        joinGame = (Button) findViewById(R.id.button2);
        LVlistView = (ListView) findViewById(R.id.listView2);
        rLayout = (RelativeLayout) findViewById(R.id.relLayout);
        rLayoutBoard = (RelativeLayout) findViewById(R.id.rLayoutBoard);
        hostGame.setOnClickListener(this);
        joinGame.setOnClickListener(this);
        iFilter= new IntentFilter(BluetoothDevice.ACTION_FOUND);
        rLayout.setVisibility(View.VISIBLE);
        rLayoutBoard.setVisibility(View.INVISIBLE);
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();

                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listAdapter.add(device.getName()+"\n"+device.getAddress());
                }
            }
        };

        registerReceiver(bReceiver,iFilter);
        iFilter= new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(bReceiver,iFilter);
        iFilter= new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bReceiver,iFilter);
        iFilter= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bReceiver,iFilter);

        listAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);

        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not enabled on this device"
                    , Toast.LENGTH_LONG).show();
            finish();
        } else if (!BA.isEnabled()) {
            turnOn();
            Toast.makeText(getApplicationContext(), "Turned on"
                    , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on",
                    Toast.LENGTH_LONG).show();
        }

        pairedDevices = BA.getBondedDevices();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                mode = 0;
                AcceptThread at = new AcceptThread();

                at.start();
                break;
            case R.id.button2:
                listAdapter.clear();
                pairedDevices = BA.getBondedDevices();
                for (BluetoothDevice bt : pairedDevices) {
                    listAdapter.add(bt.getName());
                }
                LVlistView.setAdapter(listAdapter);
                LVlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(BluetoothDevice bt : pairedDevices) {
                            if (bt.getName().equals(((TextView) view).getText())) {
                                ConnectThread connectThread = new ConnectThread(bt);
                                connectThread.start();


                            }
                        }
                    }
                });

                break;
        }
    }
    private void forClient(BluetoothSocket ClientSocket)
    {
        writerCSocket = ClientSocket;
    }

    private void forServer(BluetoothSocket ServerSocket)
    {
        writerSSocket = ServerSocket;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(bReceiver);
        } catch(IllegalArgumentException e)
        {

        }
    }
    public void turnOn()
    {
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);
    }

    private void startDiscovery() {
        BA.cancelDiscovery();
        BA.startDiscovery();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = BA.listenUsingRfcommWithServiceRecord("Test", MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    forServer(socket);
                    mHandler.obtainMessage(CLIENT_READY,socket).sendToTarget();
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection

            BA.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                mHandler.obtainMessage(CONNECTION_SUCCESS,mmSocket).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
            forClient(mmSocket);

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket)
    {
        ConnectedThread ct = new ConnectedThread(mmSocket);
        ct.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}
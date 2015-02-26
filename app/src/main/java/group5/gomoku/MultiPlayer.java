package group5.gomoku;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Maithily on 2/22/2015.
 */
public class MultiPlayer extends ActionBarActivity implements View.OnClickListener {
    private static final int CONNECTION_SUCCESS = 0;
    View getDevices ;
    View hostGame ;
    View joinGame ;
    View sendData ;
    int mode = 1;
    BluetoothSocket writerCSocket;
    BluetoothSocket writerSSocket;
    ListView LVlistView;
    IntentFilter iFilter;
    BroadcastReceiver bReceiver;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter BA;
    private static final int SUCCESS_CONNECT=0;
    private static final int MESSAGE_READ=1;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> listAdapter;
    ArrayList list = new ArrayList();
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            super.handleMessage(msg);
            switch(msg.what){
                case CONNECTION_SUCCESS:
                    // DO something
                    ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    Toast.makeText(getApplicationContext(), "CONNECT", Toast.LENGTH_SHORT).show();


                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String string = new String(readBuf);
                    Toast.makeText(getApplicationContext(), string,Toast.LENGTH_SHORT ).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutliplayer);
        getDevices = (Button) findViewById(R.id.button4);
        hostGame = (Button) findViewById(R.id.button);
        joinGame = (Button) findViewById(R.id.button2);
        sendData = (Button) findViewById(R.id.button3);
        LVlistView = (ListView) findViewById(R.id.listView2);
        getDevices.setOnClickListener(this);
        hostGame.setOnClickListener(this);
        joinGame.setOnClickListener(this);
        sendData.setOnClickListener(this);
        iFilter= new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();

                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listAdapter.add(device.getName()+"\n"+device.getAddress());
                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                {

                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {

                }
                else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
                {
                    if (BA.getState() == BA.STATE_OFF)
                    {
                        turnOn();
                    }

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
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                //   if (bt.getName().equals("Galaxy")) {
                //        ConnectThread connectThread = new ConnectThread(bt);
                //       connectThread.start();
                // }
                listAdapter.add(bt.getName());
            }
        }

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:

                // setupChat();
                pairedDevices = BA.getBondedDevices();
                    for (BluetoothDevice bt : pairedDevices) {
                        //if (bt.getName().equals("Galaxy")) {
                          //  ConnectThread connectThread = new ConnectThread(bt);
                           // connectThread.start();
                        //}
                        listAdapter.add(bt.getName());
                   }

                LVlistView.setAdapter(listAdapter);
             //   startDiscovery();
                break;
            case R.id.button:
                mode = 0;
                AcceptThread at = new AcceptThread();

                at.start();
                break;
            case R.id.button2:
                for (BluetoothDevice bt : pairedDevices) {
                    if (bt.getName().equals("XT1064")) {
                        ConnectThread connectThread = new ConnectThread(bt);
                        connectThread.start();
                    }
                }
                break;
            case R.id.button3:
                if(mode==0)
                {

                    ConnectedThread con =new ConnectedThread(writerSSocket);
                    String s1 = "successfully connected Server onClick";
                    con.write(s1.getBytes());
                }
                else
                {
                    ConnectedThread con = new ConnectedThread(writerCSocket);
                    String s2 = "successfully connected Client onClick";
                    con.write(s2.getBytes());
                }
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
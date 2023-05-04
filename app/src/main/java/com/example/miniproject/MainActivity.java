package com.example.miniproject;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    Button connectButton, leftButton, rightButton, forwardButton, backwardButton,stopButton,oneFoot, halfFoot, fourthFoot;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice hc05;
    BluetoothSocket btSocket = null;
    OutputStream outStream;

    private static final UUID HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONNECT_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = (Button) findViewById(R.id.connectToBluetoothBtn);
        leftButton = (Button) findViewById(R.id.leftBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);
        rightButton = (Button) findViewById(R.id.rightBtn);
        forwardButton = (Button) findViewById(R.id.forwardBtn);
        backwardButton = (Button) findViewById(R.id.backBtn);
        oneFoot = (Button) findViewById(R.id.oneFoot);
        halfFoot = (Button) findViewById(R.id.halfFoot);
        fourthFoot = (Button) findViewById(R.id.fourthFoot);



        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Left", Toast.LENGTH_SHORT).show();
                sendData("L");
            }
        });
        oneFoot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "One Foot", Toast.LENGTH_SHORT).show();
                sendData("1");
            }
        });
        halfFoot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Half Foot", Toast.LENGTH_SHORT).show();
                sendData("2");
            }
        });
        fourthFoot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fourth Foot", Toast.LENGTH_SHORT).show();
                sendData("3");
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Forward", Toast.LENGTH_SHORT).show();
                sendData("F");
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT).show();
                sendData("S");
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Backward Button", Toast.LENGTH_SHORT).show();
                sendData("B");
            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Right", Toast.LENGTH_SHORT).show();
                sendData("R");
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectToHC05();
            }
        });
    }
    private void sendData(String message) {
        if (outStream != null) {
            try {
                Toast.makeText(this, "Sending data: " + message, Toast.LENGTH_SHORT).show();
                outStream.write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void connectToHC05() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CONNECT_BT);
            return;
        }

        if (btAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!btAdapter.isEnabled()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);
            return;
        }

        String hc05MacAddress = "00:22:12:01:77:0A"; // Replace with your HC-05's MAC address
        hc05 = btAdapter.getRemoteDevice(hc05MacAddress);

        try {
            btSocket = hc05.createRfcommSocketToServiceRecord(HC05_UUID);
            btSocket.connect();
            outStream = btSocket.getOutputStream();
            Toast.makeText(this, "Connected to HC-05", Toast.LENGTH_SHORT).show();
            connectButton.setText("Disconnect");
        } catch (IOException e) {
            Toast.makeText(this, "Unable to connect to HC-05", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToHC05();
            } else {
                Toast.makeText(this, "Please enable Bluetooth and try again", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CONNECT_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToHC05();
            } else {
                Toast.makeText(this, "Please grant Bluetooth Connect permission and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
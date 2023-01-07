package com.example.pulsar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeviceSelector extends AppCompatActivity {

    Button perms_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_selector);


        //configure btn
        perms_btn = (Button) findViewById(R.id.Permission_Request);
        perms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBluetooth();
            }
        });
    }

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(DeviceSelector.this, "Bluetooth granted!", Toast.LENGTH_SHORT).show();

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(DeviceSelector.this, "Bluetooth not granted", Toast.LENGTH_SHORT).show();

                }
            });

    public void checkBluetooth(){
        if (ContextCompat.checkSelfPermission(
                DeviceSelector.this, Manifest.permission.BLUETOOTH_SCAN) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Toast.makeText(DeviceSelector.this, "Bluetooth granted", Toast.LENGTH_SHORT).show();
        }  else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION

            );
        }


    }
}
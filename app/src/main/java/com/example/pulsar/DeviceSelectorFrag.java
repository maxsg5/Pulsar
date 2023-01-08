package com.example.pulsar;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceSelectorFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceSelectorFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView scan_results;

    public DeviceSelectorFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceSelectorFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceSelectorFrag newInstance(String param1, String param2) {
        DeviceSelectorFrag fragment = new DeviceSelectorFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize UI
        View view = inflater.inflate(R.layout.fragment_device_selector, container, false);
        Button ScanButton = (Button) view.findViewById(R.id.ScanButton);
        Button ScanStopButton = (Button) view.findViewById(R.id.ScanStopButton);
        scan_results = (TextView) view.findViewById(R.id.scan_results);
        scan_results.setMovementMethod(new ScrollingMovementMethod());

        //create onClickListener for ScanButton
        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkBluetooth();
                bluetoothScan();
            }
        });

        //create onClickListener for ScanStopButton
        ScanStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopBluetoothScan();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(getActivity(), "Bluetooth granted!", Toast.LENGTH_SHORT).show();

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getActivity(), "Bluetooth not granted", Toast.LENGTH_SHORT).show();

                }
            });

    public void checkBluetooth(){
        //get android version from device
        int androidVersion = android.os.Build.VERSION.SDK_INT;
        //greater than or equal to android 12
        if (androidVersion >= 31) {
            if (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                Toast.makeText(getActivity(), "Bluetooth granted", Toast.LENGTH_SHORT).show();
            }  else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.BLUETOOTH_SCAN
                );
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                Toast.makeText(getActivity(), "Bluetooth granted", Toast.LENGTH_SHORT).show();
            }  else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                );

            }

        }
    }

    public void bluetoothScan() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        ScanSettings settings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(5000)
                .setUseHardwareBatchingIfSupported(true)
                .build();
        List<ScanFilter> filters = new ArrayList<>();
        //filters.add(new ScanFilter.Builder().setServiceUuid(mUuid).build());
        Toast.makeText(getActivity(), "Scanning!", Toast.LENGTH_SHORT).show();

        scanner.startScan(leScanback);
    }

    public void stopBluetoothScan() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.stopScan(leScanback);
        Toast.makeText(getActivity(), "Stopped scanning!", Toast.LENGTH_SHORT).show();

    }

    public ScanCallback leScanback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            super.onScanResult(callbackType, result);
            String dev_name = result.getDevice().getName();
            if (dev_name != null && dev_name.contains("OxySmart 6511")) {
                stopBluetoothScan();
                scan_results.setText("");
                scan_results.append("Device: >" + dev_name + "<    ADDR: " + result.getDevice().getAddress() + "\n");
            }

        }


    };


}
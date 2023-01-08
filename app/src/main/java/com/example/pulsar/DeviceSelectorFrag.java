package com.example.pulsar;

import static android.content.ContentValues.TAG;
import static android.nfc.NfcAdapter.EXTRA_DATA;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";

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
        //Log.d("LOGSHIT", "onCreate: yooooooooooooo");
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

    public void checkBluetooth() {
        //get android version from device
        int androidVersion = android.os.Build.VERSION.SDK_INT;
        //greater than or equal to android 12
        if (androidVersion >= 31) {
            if (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                Toast.makeText(getActivity(), "Bluetooth scan granted", Toast.LENGTH_SHORT).show();
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.BLUETOOTH_SCAN
                );
            }
            if (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                Toast.makeText(getActivity(), "Bluetooth connect granted", Toast.LENGTH_SHORT).show();
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.BLUETOOTH_CONNECT
                );
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                Toast.makeText(getActivity(), "Bluetooth granted", Toast.LENGTH_SHORT).show();
            } else {
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


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(getActivity(), "perm not granted", Toast.LENGTH_SHORT).show();

                    return;
                }
                result.getDevice().connectGatt(getActivity(), true, bluetoothGattCallback);
                Toast.makeText(getActivity(), "Connecting to gatt", Toast.LENGTH_SHORT).show();

            }

        }
    };

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {


        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d("LOGSHIT", "VALUE changed!!: " + value);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d("LOGSHIT", "VALUE changed!!: " + characteristic.getValue());
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "onConnectionStateChange: " + status);
            Log.d(TAG, "onConnectionStateChange: " + newState);


            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server

                Log.d("LOGSHIT", "onConnectionStateChange: CONNECTED TO GATT");
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                gatt.discoverServices();
                Log.d("LOGSHIT", "onConnectionStateChange: DISCOVERING SERVICES");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server
                scan_results.append("DISCONNECTED FROM GATT\n");

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                Log.d("LOGSHIT", "onServicesDiscovered: SERVICES DISCOVERED");
                List<BluetoothGattService> services = gatt.getServices();
                Log.d("LOGSHIT", "onServicesDiscovered: " + services.size());
                for (BluetoothGattService service : services) {
                    Log.d("LOGSHIT", "onServicesDiscovered: " + service.toString());
                    Log.d("LOGSHIT", "onServicesDiscovered: " + service.getCharacteristics());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        Log.d("LOGSHIT", "Characteristic: " + characteristic.getUuid());
                        Log.d("LOGSHIT", "setCharNoti" + gatt.setCharacteristicNotification(characteristic, true));

                        if (characteristic.getUuid().compareTo(UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")) == 0) {
                            Log.d("LOGSHIT", "FUCK YEA");
                            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                            Log.d("LOGSHIT", "found " + descriptors.size() + " descriptors");
                            for (BluetoothGattDescriptor descriptor : descriptors) {
                                Log.d("LOGSHIT", "Descriptor: " + descriptor.getUuid());
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                gatt.readDescriptor(descriptor);
                            }

                            characteristic.getValue();
                            boolean test = gatt.readCharacteristic(characteristic);
                            Log.d("LOGSHIT", "onServicesDiscovered: READ CHARACTERISTIC" + test);
                        }
                    }
                }
            } else {
                Log.d("LOGSHIT", "onServicesDiscovered received: " + status);
            }
        }

        //onDescriptorRead callback
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d("LOGSHIT", "onDescriptorRead: " + descriptor.getUuid());
            gatt.setCharacteristicNotification(descriptor.getCharacteristic(), true);

            BluetoothGattDescriptor clientConfig = descriptor;
            //clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            // or
            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            boolean suc = gatt.writeDescriptor(clientConfig);
            Log.d("LOGSHIT", "onDescriptorWrite: " + suc);
            byte[] des = descriptor.getValue();
            Log.d("LOGSHIT", "Descriptor val " + des);
        }




        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.d("LOGSHIT", "something ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                Log.d("LOGSHIT", "Read characteristic ");
            }
        }


    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void sendBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }



    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        Log.d("LOGSHIT", "broadcastUpdate: " + characteristic.getUuid().toString());
        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                        stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }







}
package com.example.pulsar;

public class bluetoothModel {
    private int device_id;
    private String device_name;
    private String mac_address;


    public bluetoothModel( int device_id, String device_name, String mac_address ){
        this.device_id=device_id;
        this.device_name=device_name;
        this.mac_address=mac_address;

    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
}




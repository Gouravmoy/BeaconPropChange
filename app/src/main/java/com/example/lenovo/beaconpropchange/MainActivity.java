package com.example.lenovo.beaconpropchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.connection.scanner.ConfigurableDevicesScanner;
import com.estimote.sdk.connection.scanner.DeviceType;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ConfigurableDevicesScanner deviceScanner;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstimoteSDK.initialize(getApplicationContext(), "gouravmoy-mohanty91-gmail--cko", "1ad5c58a21a2c58883b43ad4d06ec832");
        deviceScanner = new ConfigurableDevicesScanner(getApplicationContext());
// Scan for devices own by currently logged user.
        deviceScanner.setOwnDevicesFiltering(true);
// Scan only for Location Beacons. You can set here different types of devices, such as Proximity Beacons or Nearables.
        //deviceScanner.setDeviceTypes(DeviceType.LOCATION_BEACON);
        deviceScanner.setDeviceTypes(DeviceType.PROXIMITY_BEACON);
// Pass callback object and start scanning. If scanner finds something, it will notify your callback.
        deviceScanner.scanForDevices(new ConfigurableDevicesScanner.ScannerCallback() {
            @Override
            public void onDevicesFound(List<ConfigurableDevicesScanner.ScanResultItem> devices) {
                L.m("No of beacons detected = " + devices.size());
                for (ConfigurableDevicesScanner.ScanResultItem item : devices) {
                    L.m("macAddress = " + item.device.macAddress);
                    L.m("txPower = " + item.txPower);
                    L.m("rssi = " + item.rssi);
                    // Do something with your object.
                    // ScanResultItem contains basic info about device discovery - such as RSSI, TX power, or discovery time.
                    // It also contains ConfigurableDevice object. You can easily acquire it via item.configurableDevice
                }
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        deviceScanner.scanForDevices(new ConfigurableDevicesScanner.ScannerCallback() {
            @Override
            public void onDevicesFound(List<ConfigurableDevicesScanner.ScanResultItem> devices) {
                L.m("No of beacons detected = " + devices.size());
                for (ConfigurableDevicesScanner.ScanResultItem item : devices) {
                    L.m("macAddress = " + item.device.macAddress);
                    L.m("txPower = " + item.txPower);
                    L.m("rssi = " + item.rssi);
                    // Do something with your object.
                    // ScanResultItem contains basic info about device discovery - such as RSSI, TX power, or discovery time.
                    // It also contains ConfigurableDevice object. You can easily acquire it via item.configurableDevice
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        deviceScanner.stopScanning();
    }
}

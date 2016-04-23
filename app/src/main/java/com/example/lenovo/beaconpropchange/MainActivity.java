package com.example.lenovo.beaconpropchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.connection.DeviceConnection;
import com.estimote.sdk.connection.DeviceConnectionCallback;
import com.estimote.sdk.connection.DeviceConnectionProvider;
import com.estimote.sdk.connection.exceptions.DeviceConnectionException;
import com.estimote.sdk.connection.scanner.ConfigurableDevice;
import com.estimote.sdk.connection.scanner.ConfigurableDevicesScanner;
import com.estimote.sdk.connection.scanner.DeviceType;
import com.estimote.sdk.connection.settings.SettingCallback;
import com.estimote.sdk.connection.settings.Version;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ConfigurableDevicesScanner deviceScanner;
    DeviceConnectionProvider connectionProvider;
    ConfigurableDevice device;
    DeviceConnection connection;
    int advertisingInterval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstimoteSDK.initialize(getApplicationContext(), "gouravmoy-mohanty91-gmail--cko", "1ad5c58a21a2c58883b43ad4d06ec832");
        connectionProvider = new DeviceConnectionProvider(this);
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
                    device = item.device;
                    connectionProvider.connectToService(new DeviceConnectionProvider.ConnectionProviderCallback() {
                        @Override
                        public void onConnectedToService() {
                            connection = connectionProvider.getConnection(device);
                            connection.connect(new DeviceConnectionCallback() {
                                @Override
                                public void onConnected() {
                                    connection.settings.deviceInfo.firmware().get(new SettingCallback<Version>() {
                                        @Override
                                        public void onSuccess(Version version) {
                                            Log.d("DeviceRead", "Read firmware version:  " + version.toString());
                                        }

                                        @Override
                                        public void onFailure(DeviceConnectionException e) {
                                            Log.d("DeviceRead","Reading firmware version failed.");
                                        }
                                    });
                                    connection.settings.eddystone.tlm.advertisingInterval().set(advertisingInterval, new SettingCallback<Integer>() {
                                        @Override
                                        public void onSuccess(Integer integer) {
                                            Log.d("DeviceWrite","Written new Eddystone interval: " + integer.toString());
                                        }

                                        @Override
                                        public void onFailure(DeviceConnectionException e) {
                                            Log.d("DeviceWrite","Write new Eddystone interval failed.");
                                        }
                                    });
                                    L.m("Device Connected");
                                }

                                @Override
                                public void onDisconnected() {
                                    L.m("Device DisConnected");
                                }

                                @Override
                                public void onConnectionFailed(DeviceConnectionException e) {
                                    L.m("Device Connection Failed");
                                }
                            });
                        }
                    });

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
    protected void onDestroy() {
        connectionProvider.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        deviceScanner.stopScanning();
    }
}

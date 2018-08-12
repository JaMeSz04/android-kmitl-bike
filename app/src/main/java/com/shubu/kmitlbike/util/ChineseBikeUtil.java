package com.shubu.kmitlbike.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.state.BikeState;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;


@Singleton
public class ChineseBikeUtil {
    private final UUID CLIENT_CHARACTER_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private final UUID BLT_SERVER_UUID = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
    private final UUID READDATA_UUID = UUID.fromString("000036f6-0000-1000-8000-00805f9b34fb");
    private final UUID WRITEDATA_UUID = UUID.fromString("000036f5-0000-1000-8000-00805f9b34fb");
    private final UUID LOCKREAD_WRITE_UUID = UUID.fromString("0000feff-0000-1000-8000-00805f9b34fb");
    //private final byte[] key = CommunicationAdapter.intToBytes(32874782547563714880658817994543);
    private final byte[] key = {0x20, 0x57, 0x2F, 0x52, 0x36, 0x4B, 0x3F, 0x47, 0x30, 0x50, 0x41, 0x58, 0x11, 0x63, 0x2D, 0x2B};
    private final byte[] gettoken = {0x06, 0x01, 0x01, 0x01};
    private final byte[] token = new byte[4];
    private Bike bike;
    private RxBleClient client;
    private DisposableContainer disposeBag;
    private RxBleConnection connection;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;


    public class DataClass {
        public BluetoothDevice device = null;
        public String name;
        public Integer rssi = 0;
        public int count = 0;
        public String address;
    }

    private DataClass m_myData = new DataClass();

    BluetoothGattCharacteristic writeCharacteristic;
    BluetoothGattCharacteristic readCharacteristic;
    BluetoothGatt mBluetoothGatt;

    public ChineseBikeUtil(Bike target){
        this.client = KMITLBikeApplication.getBluetooth();
        this.client.observeStateChanges().subscribe(state -> {Timber.e("state changed to : " + state);});
        this.bike = target;
        bluetoothManager = KMITLBikeApplication.getBluetoothManager();
        bluetoothAdapter = bluetoothManager.getAdapter();


        bluetoothAdapter.startLeScan(mLeScanCallback);

    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void SendData(byte[] data) {
        byte miwen[] = CommunicationAdapter.Encrypt(data, key);
        if (miwen != null) {
            try {
                if (mBluetoothGatt != null) {
                    BluetoothGattService vClickService = mBluetoothGatt
                            .getService(BLT_SERVER_UUID);
                    BluetoothGattCharacteristic vClickCharacteristic = vClickService
                            .getCharacteristic(WRITEDATA_UUID);
                    vClickCharacteristic.setValue(miwen);
                    mBluetoothGatt.writeCharacteristic(vClickCharacteristic);
                    String hexString = CommunicationAdapter.bytesToHexString(data);
                    Message m_hex = m_myHandler.obtainMessage(9, 1, 1,
                            hexString);
                    m_myHandler.sendMessage(m_hex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void connect() {
        bluetoothAdapter.stopLeScan(mLeScanCallback);

        if (mBluetoothGatt == null) {
            mBluetoothGatt = m_myData.device.connectGatt(KMITLBikeApplication.getAppContext(),
                    false, mGattCallback);
        }

    }


    public void disconnect(){

        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            m_myHandler.sendEmptyMessage(6);
        }
    }


   public void borrowDu(){
        bluetoothAdapter.stopLeScan(mLeScanCallback);
        if (mBluetoothGatt.getDevice() != null && mBluetoothGatt.getDevice().getAddress()
                .equals(m_myData.address)) {
            Log.e("Tag", "mBluetoothGatt");
            byte[] downLock = {0x05, 0x01, 0x06, 0x30, 0x30, 0x30, 0x30,
                    0x30, 0x30, token[0], token[1], token[2], token[3],
                    0x00, 0x00, 0x00};
            SendData(downLock);
        }
    }

    Handler m_myHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message mes) {
            return false;
        }
    });

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            try {
//                [The real address is the address in the broadcast]
                // 真实地址是广播播里面的地址
                String record = CommunicationAdapter.bytesToHex(scanRecord);
                String address = record.substring(record.indexOf("00000000") - 12, record.indexOf("00000000"));
                String mDeviceAddress = "";
                for (int i = 0; i < address.length(); i += 2) {
                    mDeviceAddress += address.substring(i, i + 2);
                    if (i == 10) {
                        break;
                    } else {
                        mDeviceAddress += ":";
                    }
                }
                if (mDeviceAddress.equals(bike.getMacAddress())) {
                    //Scan to specified device
                    Log.e("Tag","Scan to specified device:" + mDeviceAddress);
                    String nowAddress = mDeviceAddress;

                    m_myData.device = device;
                    m_myData.name = device.getName();
                    m_myData.address = nowAddress;
                    m_myData.rssi = rssi;
                    m_myData.count = 0;
                    m_myHandler.sendEmptyMessage(3);
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    connect();
                }else{
                    //Not scanned to specified device
                    Log.e("Tag","Not scanned to specified device:" + mDeviceAddress);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void setCharacteristicNotification(UUID uuid) {
        BluetoothGattService vClickService = mBluetoothGatt
                .getService(BLT_SERVER_UUID);
        if (vClickService == null) {
            return;
        }
        BluetoothGattCharacteristic vClickCharacteristic = vClickService
                .getCharacteristic(uuid);
        if (vClickCharacteristic == null) {
            return;
        }
        mBluetoothGatt
                .setCharacteristicNotification(vClickCharacteristic, true);
        BluetoothGattDescriptor vDescriptor = vClickCharacteristic
                .getDescriptor(CLIENT_CHARACTER_CONFIG);
        vDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(vDescriptor);

    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            Log.e("Tag", "onConnectionStateChange " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
                m_myHandler.sendEmptyMessage(5);


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
                m_myHandler.sendEmptyMessage(6);
                m_myData.count = 0;
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("Tag", "onCharacteristicRead GATT_SUCCESS");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                mBluetoothGatt = gatt;
                Log.e("Tag", "onServicesDiscovered GATT_SUCCESS");
                try {
                    setCharacteristicNotification(READDATA_UUID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setCharacteristicNotification(LOCKREAD_WRITE_UUID);
                borrowDu();

                Log.e("Tag", " GATT_SUCCESS END");
            }

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e("Tag", "onDescriptorWrite");
            SendData(gettoken);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] values = characteristic.getValue();
            byte[] x = new byte[16];
            System.arraycopy(values, 0, x, 0, 16);
            byte mingwen[] = CommunicationAdapter.Decrypt(x, key);

            String hexString = CommunicationAdapter.bytesToHexString(mingwen);
            //Characteristic value change
            System.out.println("Tag特征值变化:" + hexString);
            if (null != hexString && hexString.startsWith("050f01")) {
                //Lock
                Log.e("Tag", "关锁:" + hexString);
            }
            Message m_hex = m_myHandler.obtainMessage(8, 1, 1, hexString);
            m_myHandler.sendMessage(m_hex);

            if (mingwen != null && mingwen.length == 16) {
                if (mingwen[0] == 0x06 && mingwen[1] == 0x02) {
                    token[0] = mingwen[3];
                    token[1] = mingwen[4];
                    token[2] = mingwen[5];
                    token[3] = mingwen[6];
                } else if (mingwen[0] == 0x05 && mingwen[1] == 0x02) {
                    if (mingwen[3] == 0x00) { // Unlock success
                        Message msg = m_myHandler.obtainMessage(1, 1, 1, gatt
                                .getDevice().getAddress());
                        m_myHandler.sendMessage(msg);
                    } else {// The lock failure
                        Message msg = m_myHandler.obtainMessage(2, 1, 1,
                                "failure");
                        m_myHandler.sendMessage(msg);
                    }
                } else if (mingwen[0] == 0x05 && mingwen[1] == 0x08) {
                    if (mingwen[3] == 0x00) { // You success
                        Message msg = m_myHandler.obtainMessage(1, 1, 1, gatt
                                .getDevice().getAddress());
                        m_myHandler.sendMessage(msg);
                    } else { // You failed
                        Message msg = m_myHandler.obtainMessage(2, 1, 1,
                                "failure");
                        m_myHandler.sendMessage(msg);
                    }
                }

            }
        }

    };

}

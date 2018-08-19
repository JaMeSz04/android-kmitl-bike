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
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.state.BikeState;

import java.util.UUID;

import architecture.com.jimi.NativeController;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class CopyBikeUtil {

    //mac addr of lock -> "C4:A8:28:08:28:5E"
    private String mac;

    public class DataClass {
        public BluetoothDevice device = null;
        public String name;
        public Integer rssi = 0;
        public int count = 0;
        public String address;
    }

    private DataClass m_myData = new DataClass();

    BluetoothAdapter mBluetoothAdapter;
    BluetoothGattCharacteristic writeCharacteristic;
    BluetoothGattCharacteristic readCharacteristic;
    BluetoothGatt mBluetoothGatt;

    byte[] key;

    public static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID
            .fromString(NativeController.getClientCharacteristicConfig());
    public static final UUID bltServerUUID = UUID
            .fromString(NativeController.getBltServerUUID());
    public static final UUID readDataUUID = UUID
            .fromString(NativeController.getReadDataUUID());
    public static final UUID writeDataUUID = UUID
            .fromString(NativeController.getWriteDataUUID());
    public final static UUID LOCK_READWRITE_UUID = UUID
            .fromString(NativeController.getLockReadwriteUuid());

    byte[] token = new byte[4];

    byte[] gettoken;

    private boolean isUnlocked = false;
    private PublishSubject<BikeState> usageStatus = null;

    public CopyBikeUtil(String mac, PublishSubject<BikeState> usageStatus) {
        this.mac = mac;
        this.usageStatus = usageStatus;
        key = NativeController.getEncyptKey();
        gettoken = NativeController.getTokenCmd();

        BluetoothManager bluetoothManager = KMITLBikeApplication.getBluetoothManager();

        mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothAdapter.startLeScan(mLeScanCallback);

        this.usageStatus.onNext(BikeState.BORROW_COMPLETE);


    }

    public void SendData(byte[] data) {
        byte miwen[] = CommunicationAdapter.Encrypt(data, key);
        if (miwen != null) {
            try {
                if (mBluetoothGatt != null) {
                    BluetoothGattService vClickService = mBluetoothGatt
                            .getService(bltServerUUID);
                    BluetoothGattCharacteristic vClickCharacteristic = vClickService
                            .getCharacteristic(writeDataUUID);
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

    private void dispNotFindDeviceToast() {
        Timber.e("can't find device");

    }


    public void disconnect() {
        if (m_myData.device == null) {
            dispNotFindDeviceToast();
            return;
        }
        if (mBluetoothGatt != null) {
            isUnlocked = false;
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            m_myHandler.sendEmptyMessage(6);
        }
    }

    private void connect(){
        if (m_myData.device == null) {
            dispNotFindDeviceToast();
            return;
        }

        mBluetoothAdapter.stopLeScan(mLeScanCallback);

        if (mBluetoothGatt == null) {
            mBluetoothGatt = m_myData.device.connectGatt(KMITLBikeApplication.getAppContext(),
                    false, mGattCallback);
        }
    }

    private void unlock(){
        if (m_myData.device == null) {
            Log.e("Tag", "null");
            dispNotFindDeviceToast();
            return;
        }

        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        if (mBluetoothGatt.getDevice() != null && mBluetoothGatt.getDevice().getAddress()
                .equals(m_myData.address)) {
            Log.e("Tag", "mBluetoothGatt");
            byte[] downLock = {0x05, 0x01, 0x06, 0x30, 0x30, 0x30, 0x30,
                    0x30, 0x30, token[0], token[1], token[2], token[3],
                    0x00, 0x00, 0x00};
            SendData(downLock);
        } else {
            Timber.i("设备为空");
        }
    }

    Handler m_myHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message mes) {
            switch (mes.what) {
                case 1: {
                    // 开锁成功 [Unlocked successfully]
                    m_myData.count++;
                    usageStatus.onComplete();
                    Timber.e("Equipment name:" + m_myData.name + "\r\n"
                            + "Signal strength:" + String.valueOf(m_myData.rssi)
                            + "\r\n" + "Operation frequency:"
                            + String.valueOf(m_myData.count) + "\r\n"
                            + "Bluetooth address:" + m_myData.address);



                    break;
                }
                case 3: {
                    Timber.e("Equipment name:" + m_myData.name + "\r\n"
                            + "Signal strength:" + String.valueOf(m_myData.rssi)
                            + "\r\n" + "Operation frequency:"
                            + String.valueOf(m_myData.count) + "\r\n"
                            + "Bluetooth address:" + m_myData.address);
                    break;
                }
                case 4: {
                    Timber.e((String) mes.obj);
                    break;
                }

                case 5: {
                    Timber.e("Successfully Connected");

                    break;
                }
                case 6: {
                    Timber.e("Successfully Disconnected");
                    break;
                }
                case 8: {
                    Timber.e((String) mes.obj);
                    if (!isUnlocked) {
                        isUnlocked = true;
                        unlock();
                    }
                    break;
                }
                case 9: {
                    Timber.e((String) mes.obj);
                    break;
                }
                default:
                    break;
            }
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

                if (mDeviceAddress.equals(mac)) {
                    //Scan to specified device
                    Log.e("Tag","Scan to specified device:" + mDeviceAddress);
                    String nowAddress = mDeviceAddress;

                    m_myData.device = device;
                    m_myData.name = device.getName();
                    m_myData.address = nowAddress;
                    m_myData.rssi = rssi;
                    m_myData.count = 0;
                    m_myHandler.sendEmptyMessage(3);

                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
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
                .getService(bltServerUUID);
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
                .getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
        vDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(vDescriptor);

    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            Log.e("Tag", "onConnectionStateChange");

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
                    setCharacteristicNotification(readDataUUID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setCharacteristicNotification(LOCK_READWRITE_UUID);

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
package com.shubu.kmitlbike.util;

import com.annimon.stream.Stream;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.state.BikeState;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;
import rx.SingleSubscriber;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;


@Singleton
public class BluetoothUtil {

    private enum BluetoothTask {
        BORROW, RETURN
    }

    private final byte[] BORROW_COMMAND = "BORROW".getBytes(StandardCharsets.UTF_8);
    private final byte[] RETURN_COMMAND = "RETURN".getBytes(StandardCharsets.UTF_8);
    private final UUID CHARACTERISTIC_ID = UUIDHelper.uuidFromString("FFE1");
    private RxBleClient client;
    private PublishSubject<BikeState> eventbus;
    private Bike target;
    private RxBleDevice device;
    private List<Disposable> tasks;
    private RxBleConnection deviceConnection;
    private BluetoothTask command;
    private SingleSubject<String> onceSubscriber;


    public BluetoothUtil(Bike bike){
        client = KMITLBikeApplication.getBluetooth();
        target = bike;
        onceSubscriber = SingleSubject.create();
        tasks = new ArrayList<>();
    }

    public void initBluetoothService(){
        this.command = BluetoothTask.BORROW;
        tasks.add(this.startDiscover().subscribe( scanResult -> {
            this.device = scanResult.getBleDevice();
            this.connect();
        }));
    }

    public void borrow(String cmd){
        Disposable borrowTask = this.deviceConnection.writeCharacteristic(CHARACTERISTIC_ID, cmd.getBytes(StandardCharsets.UTF_8))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bytes -> onWriteSuccess(bytes),
                        throwable -> Timber.e(throwable)
                );
        tasks.add(borrowTask);
    }


    private Observable<ScanResult> startDiscover(){
        this.updateEvent(BikeState.BORROW_SCAN_START);
        return this.client.scanBleDevices(new ScanSettings.Builder().build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .timeout(15, TimeUnit.SECONDS)
                .filter( scanResult -> scanResult.getBleDevice().getMacAddress().equals( target.getMacAddress()) );
    }

    private void connect(){
        this.updateEvent(BikeState.BORROW_SCAN_FINISH);
        this.dispose();
        tasks.add(this.device.establishConnection(false).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doFinally(this::dispose)
            .subscribe(this::onConnectionReceived, this::onConnectionFailure));
    }

    private void onConnectionReceived(RxBleConnection connection){
        this.deviceConnection = connection;
        this.updateEvent(BikeState.BORROW_START);

        tasks.add(connection.setupNotification(CHARACTERISTIC_ID).flatMap(notificationObservable -> notificationObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNotificationReceive, this::onNotificationFailure));

        tasks.add(connection.writeCharacteristic(CHARACTERISTIC_ID, BORROW_COMMAND).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                bytes -> onWriteSuccess(bytes),
                throwable -> Timber.e(throwable)
            ));
    }

    private void onWriteSuccess(byte[] response){
        Timber.e("write success!!!");
        this.updateEvent(BikeState.BORROW_COMPLETE);
        Timber.e(new String(response, StandardCharsets.UTF_8));
    }

    private void onConnectionFailure(Throwable throwable) {
        Timber.e(throwable);
    }

    private void onNotificationReceive(byte[] bytes){
        Timber.e("Notification!!!");
        String response = new String(bytes, StandardCharsets.UTF_8);
        Timber.e(response);
        String[] command = response.split(",");
        switch (command[0]){
            case "NONCE":
                onceSubscriber.onSuccess(command[1]);
            case "BORROW":
                eventbus.onComplete();

        }

    }

    private void onNotificationFailure(Throwable throwable){
        Timber.e(throwable);
    }

    private void updateEvent(BikeState event){
        if (this.eventbus != null){
            this.eventbus.onNext(event);
        }
    }

    private void dispose(){
        Stream.of(tasks).forEach(Disposable::dispose);
    }

    public void setEventbus(PublishSubject<BikeState> eventbus) {
        this.eventbus = eventbus;
    }

    public Bike getTarget() {
        return target;
    }

    public void setTarget(Bike target) {
        this.target = target;
    }

    public SingleSubject<String> getOnceSubscriber() {
        return onceSubscriber;
    }
}

package com.shubu.kmitlbike.data;

import android.location.Location;

import com.annimon.stream.Stream;
import com.google.zxing.Result;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.data.adapter.LocationAdapter;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.LoginForm;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.NamedResource;
import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowRequest;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowResponse;
import com.shubu.kmitlbike.data.remote.Router;
import com.shubu.kmitlbike.data.remote.Router.PokemonListResponse;
import com.shubu.kmitlbike.data.state.BikeState;
import com.shubu.kmitlbike.ui.common.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

@Singleton
public class DataManager {

    private final Router mRouter;
    private List<Bike> bikeList;
    private Bike usingBike;
    private List<UsagePlan> usagePlans;
    private PublishSubject<BikeState> usageStatus;

    @Inject
    public DataManager(Router router) {
        mRouter = router;
    }

    public Single<List<String>> getPokemonList(int limit) {
        return mRouter.getPokemonList(limit)
                .flatMap(new Func1<Router.PokemonListResponse,
                        Single<? extends List<String>>>() {
                            @Override
                            public Single<? extends List<String>>
                                    call(PokemonListResponse pokemonListResponse) {
                                List<String> pokemonNames = new ArrayList<>();
                                for (NamedResource pokemon : pokemonListResponse.results) {
                                    pokemonNames.add(pokemon.name);
                                }
                                return Single.just(pokemonNames);
                            }
                        });
    }

    public Single<LoginResponse> login(String username, String password){
        Timber.i("gonna login");
        return mRouter.login(new LoginForm(username, password));

    }

    public Single<List<UsagePlan>> getUsagePlan(){
        return mRouter.getUsagePlan();
    }

    public Single<List<Bike>> getBikeList(){
        return mRouter.getBikeList();
    }

    public Single<Response> performReturn(Bike bike, Location location){
        return mRouter.returnBike(bike.getId(), LocationAdapter.makeLocationForm(location));
    }

    public Bike getBikeFromScannerCode(Result code){
        List<Bike> result = Stream.of(this.bikeList).filter( bike -> bike.getBarcode().equals(code.getText())).toList();
        if (result.size() <= 0){ /* TODO: 4/2/2018 raise GUI error : case -> barcode not found!!! */  }
        return result.get(0);
    }

    public Single<Pokemon> getPokemon(String name) {
        return mRouter.getPokemon(name);
    }

    public void setBikeList(List<Bike> bikeList) {
        this.bikeList = bikeList;
    }

    public void setUsagePlans(List<UsagePlan> usagePlans) {
        this.usagePlans = usagePlans;
    }

    public PublishSubject<BikeState> initializeBorrowService(Bike bike){
        this.usingBike = bike;
        this.usageStatus = PublishSubject.create();
        return this.usageStatus;
    }

    public void performBorrow(Bike bike, Location location){
        usageStatus.onNext(BikeState.BORROW_START);
        BikeBorrowRequest request = new BikeBorrowRequest();
        request.setLocation(LocationAdapter.makeLocationForm(location));
        request.setNonce(Math.round(System.nanoTime() / 1000));
        request.setSelectedPlan(CONSTANTS.SELECTED_PLAN);
        mRouter.borrowBike(bike.getId(), request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<BikeBorrowResponse>() {
            @Override
            public void onSuccess(BikeBorrowResponse value) {
                switch (bike.getBikeModel()){
                    case CONSTANTS.GIANT_ESCAPE:
                        // TODO: 4/3/2018 bluetooth service
                        break;
                    case CONSTANTS.LA_GREEN:
                        usageStatus.onCompleted();

                }
            }

            @Override
            public void onError(Throwable error) {
                Timber.e("error woi : " + error.getMessage());
                Timber.e(error);
            }
        });
    }

    public Bike getUsingBike() {
        return usingBike;
    }

    public void setUsingBike(Bike usingBike) {
        this.usingBike = usingBike;
    }
}
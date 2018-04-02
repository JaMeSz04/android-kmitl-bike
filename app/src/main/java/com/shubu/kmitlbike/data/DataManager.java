package com.shubu.kmitlbike.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Stream;
import com.google.zxing.Result;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.data.model.LoginForm;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.NamedResource;
import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.remote.Router;
import com.shubu.kmitlbike.data.remote.Router.PokemonListResponse;
import com.shubu.kmitlbike.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.functions.Func1;
import timber.log.Timber;

@Singleton
public class DataManager {

    private final Router mRouter;
    private List<Bike> bikeList;
    private List<UsagePlan> usagePlans;


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
}
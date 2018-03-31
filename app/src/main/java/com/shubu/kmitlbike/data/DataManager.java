package com.shubu.kmitlbike.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.shubu.kmitlbike.data.model.LoginForm;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.NamedResource;
import com.shubu.kmitlbike.data.model.Pokemon;
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


    public boolean hasToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("token-store", Context.MODE_PRIVATE);
        String token = pref.getString("token","");
        return !token.isEmpty();
    }


    public Single<Pokemon> getPokemon(String name) {
        return mRouter.getPokemon(name);
    }

}
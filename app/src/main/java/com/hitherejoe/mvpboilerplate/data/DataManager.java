package com.hitherejoe.mvpboilerplate.data;

import com.hitherejoe.mvpboilerplate.data.model.LoginForm;
import com.hitherejoe.mvpboilerplate.data.model.LoginResponse;
import com.hitherejoe.mvpboilerplate.data.model.NamedResource;
import com.hitherejoe.mvpboilerplate.data.model.Pokemon;
import com.hitherejoe.mvpboilerplate.data.remote.Router;
import com.hitherejoe.mvpboilerplate.data.remote.Router.PokemonListResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.functions.Func1;

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
        return mRouter.login(new LoginForm(username, password));
    }

    public Single<Pokemon> getPokemon(String name) {
        return mRouter.getPokemon(name);
    }

}
package com.shubu.kmitlbike.data;

import com.shubu.kmitlbike.data.model.NamedResource;
import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.remote.MvpBoilerplateService;
import com.shubu.kmitlbike.data.remote.MvpBoilerplateService.PokemonListResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final MvpBoilerplateService mMvpBoilerplateService;

    @Inject
    public DataManager(MvpBoilerplateService mvpBoilerplateService) {
        mMvpBoilerplateService = mvpBoilerplateService;
    }

    public Single<List<String>> getPokemonList(int limit) {
        return mMvpBoilerplateService.getPokemonList(limit)
                .flatMap(new Func1<MvpBoilerplateService.PokemonListResponse,
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

    public Single<Pokemon> getPokemon(String name) {
        return mMvpBoilerplateService.getPokemon(name);
    }

}
package com.hitherejoe.mvpboilerplate.data.remote;


import com.hitherejoe.mvpboilerplate.data.model.LoginForm;
import com.hitherejoe.mvpboilerplate.data.model.LoginResponse;
import com.hitherejoe.mvpboilerplate.data.model.NamedResource;
import com.hitherejoe.mvpboilerplate.data.model.Pokemon;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

public interface Router {

    @GET("pokemon")
    Single<PokemonListResponse> getPokemonList(@Query("limit") int limit);

    @GET("pokemon/{name}")
    Single<Pokemon> getPokemon(@Path("name") String name);

    @POST("api/v1/accounts/login")
    Single<LoginResponse> login(@Body LoginForm form);

    class PokemonListResponse {
        public List<NamedResource> results;
    }

}

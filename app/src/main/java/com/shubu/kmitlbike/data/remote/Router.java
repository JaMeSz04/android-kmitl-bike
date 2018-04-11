package com.shubu.kmitlbike.data.remote;


import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.data.model.LoginForm;
import com.shubu.kmitlbike.data.model.LoginResponse;
import com.shubu.kmitlbike.data.model.NamedResource;
import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.model.UsagePlan;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowRequest;
import com.shubu.kmitlbike.data.model.bike.BikeBorrowResponse;
import com.shubu.kmitlbike.data.model.bike.BikeReturnForm;
import com.shubu.kmitlbike.data.model.bike.BikeReturnResponse;
import com.shubu.kmitlbike.data.model.bike.Location;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import io.reactivex.Single;

public interface Router {

    
    @POST("api/v1/accounts/login")
    Single<LoginResponse> login(@Body LoginForm form);

    @GET("api/v1/bikes/list")
    Single<List<Bike>> getBikeList();

    @GET("api/v1/bikes/plans/list")
    Single<List<UsagePlan>> getUsagePlan();

    @POST("api/v1/bikes/{id}/borrow")
    Single<BikeBorrowResponse> borrowBike(@Path("id") int id, @Body BikeBorrowRequest request);

    @POST("api/v1/bikes/{id}/return")
    Single<BikeReturnResponse> returnBike(@Path("id") int id, @Body BikeReturnForm request);

    @POST("api/v1/bikes/update")
    Single<Object> updateTrackingLocation(@Body Location location);


}

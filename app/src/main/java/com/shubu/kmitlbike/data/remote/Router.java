package com.shubu.kmitlbike.data.remote;


import com.shubu.kmitlbike.data.model.History;
import com.shubu.kmitlbike.data.model.ProfileHistory;
import com.shubu.kmitlbike.data.model.Token;
import com.shubu.kmitlbike.data.model.UserSession;
import com.shubu.kmitlbike.data.model.VersionForm;
import com.shubu.kmitlbike.data.model.VersionResponse;
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
import com.shubu.kmitlbike.data.model.bike.Session;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import io.reactivex.Single;

public interface Router {

    
    @POST("api/v1/accounts/login")
    Single<LoginResponse> login(@Body LoginForm form);

    @POST("api/v1/accounts/access_token")
    Single<LoginResponse> tokenLogin(@Body Token token);

    @GET("api/v1/bikes/list")
    Single<List<Bike>> getBikeList();

    @GET("api/v1/users/{id}/histories/list")
    Single<List<ProfileHistory>> getHistoriesList(@Path("id") int userId);

    @GET("api/v1/users/{id}/histories/{hId}")
    Single<History> getHistory(@Path("id") int userId, @Path("hId") int historyId);

    @GET("api/v1/bikes/plans/list")
    Single<List<UsagePlan>> getUsagePlan();

    @GET("api/v1/users/{id}/session")
    Single<UserSession> getUserSession(@Path("id") int userid);

    @POST("api/v1/bikes/{id}/borrow")
    Single<BikeBorrowResponse> borrowBike(@Path("id") int id, @Body BikeBorrowRequest request);

    @POST("api/v1/bikes/{id}/return")
    Single<BikeReturnResponse> returnBike(@Path("id") int id, @Body BikeReturnForm request);

    @POST("api/v1/bikes/update")
    Single<Object> updateTrackingLocation(@Body Location location);

    @POST("api/v1/versions/check")
    Single<VersionResponse> getVersion(@Body VersionForm form);




}

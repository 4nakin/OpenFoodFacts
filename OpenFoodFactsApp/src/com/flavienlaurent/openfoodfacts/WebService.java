package com.flavienlaurent.openfoodfacts;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by f.laurent on 31/10/13.
 */
public interface WebService {

    @GET("/product/{ean}.json")
    void getProduct(@Path("ean") String ean, Callback<OFFResponse> callback);
}

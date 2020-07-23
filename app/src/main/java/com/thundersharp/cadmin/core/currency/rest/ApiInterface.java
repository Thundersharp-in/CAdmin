package com.thundersharp.cadmin.core.currency.rest;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("currencymobile")
    Call<JsonObject> getCurrencyMobile();

}

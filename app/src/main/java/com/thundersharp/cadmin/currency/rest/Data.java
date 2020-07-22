package com.thundersharp.cadmin.currency.rest;


import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.thundersharp.cadmin.currency.utils.Globals;
import com.thundersharp.cadmin.currency.utils.Prefs;

import retrofit2.Call;

public class Data {

    public static void getCurrency(final Activity activity, final UpdateCallback updateCallback) {
        GetCurrency getCurrency = new GetCurrency(updateCallback);
        getCurrency.execute(activity);
    }

    public static class GetCurrency extends AsyncTask<Activity, Void, Integer> {

        UpdateCallback updateCallback;
        int error = 0;

        GetCurrency(UpdateCallback updateCallback) {
            this.updateCallback = updateCallback;
        }

        @Override
        protected Integer doInBackground(Activity... params) {
            final Activity activity = params[0];

            ApiInterface apiInterface = new ApiClient().getClient(activity).create(ApiInterface.class);

            final Call<JsonObject> bootupRes = apiInterface.getCurrencyMobile();

            try {
                JsonObject currencyResponse = bootupRes.execute().body();
                if (currencyResponse.get("status").getAsBoolean()) {
                    Prefs.setPrefs("currencyJson", currencyResponse.toString(), activity);
                    error = 0;
                } else {
                    Globals.errorRes = "Error in fetching";
                    error = 1;
                }
            } catch (Exception e) {
                error = 1;
                Globals.errorRes = "No Internet Connection!";
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (error == 0) {
                updateCallback.onUpdate();
            } else {
                updateCallback.onFailure();
            }
        }
    }

    public interface UpdateCallback {
        void onUpdate();

        void onFailure();
    }


}

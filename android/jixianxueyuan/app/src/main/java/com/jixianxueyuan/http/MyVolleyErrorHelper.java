package com.jixianxueyuan.http;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengchao on 9/1/15.
 */
public class MyVolleyErrorHelper {

    public static void showError(Context context, Object error){
        Toast.makeText(context, getMessage(error,context), Toast.LENGTH_SHORT).show();
    }

    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.time_out);
        }
        else if (isServerProblem(error)) {
            return handleServerError(error, context);
        }
        else if (error instanceof NetworkError) {
            return context.getResources().getString(R.string.network_err);
        }
        else if(error instanceof NoConnectionError){
            return context.getResources().getString(R.string.connection_err);
        }
        return context.getResources().getString(R.string.err);
    }
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }
    /**
     * Determines whether the error is related to server
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }
    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());
                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();
                default:
                    return context.getResources().getString(R.string.err);
            }
        }
        return context.getResources().getString(R.string.err);
    }
}

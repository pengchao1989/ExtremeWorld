package com.jixianxueyuan.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.dto.MyResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by pengchao on 5/31/15.
 */
public class MyRequest<T> extends JsonRequest<MyResponse<T>> {

    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Gson gson = new Gson();

    private Class<T> clazz;

    private Response.Listener<MyResponse<T>> mListener;
    private String mRequestBody;
    private Object mRequestBodyObject;

    public MyRequest(String url,Class<T> clazz, Response.Listener<MyResponse<T>> listener,  Response.ErrorListener errorListener) {

        super(Method.GET, url,null , listener, errorListener);
        mListener = listener;
        this.clazz = clazz;
    }

    public MyRequest(int method ,String url, Class<T> clazz, Response.Listener<MyResponse<T>> listener,  Response.ErrorListener errorListener) {

        super(method, url,null , listener, errorListener);
        mListener = listener;
        this.clazz = clazz;
    }

    public MyRequest(int method, String url, Class<T> clazz, Object requestBodyObject, Response.Listener<MyResponse<T>> listener,  Response.ErrorListener errorListener) {

        super(method, url,null , listener, errorListener);

        this.mRequestBodyObject = requestBodyObject;
        mListener = listener;
        this.clazz = clazz;


    }

    @Override
    protected Response<MyResponse<T>> parseNetworkResponse(NetworkResponse response) {

        try
        {
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            MyResponse<T> myResponse = gson.fromJson(jsonStr, new TypeToken<MyResponse<T>>(){}.getType());

            JsonParser parser = new JsonParser();
            JsonObject jsonObject =  parser.parse(jsonStr).getAsJsonObject();
            JsonElement contentObject = jsonObject.getAsJsonObject("content");

            T content = gson.fromJson(contentObject, clazz);

            myResponse.setContent(content);


            return Response.success(myResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(MyResponse<T> response) {
        mListener.onResponse(response);
    }

    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try
        {
            mRequestBody = gson.toJson(mRequestBodyObject);

            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        }
        catch (UnsupportedEncodingException uee)
        {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}

package com.jixianxueyuan.http;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 5/31/15.
 */
public class MyPageRequest<T> extends JsonRequest<MyResponse<MyPage<T>>> {

    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private Class<T> classz;

    private final Gson gson = new Gson();

    private Response.Listener<MyResponse<MyPage<T>>> mListener;
    private String mRequestBody;
    private Object mRequestBodyObject;

    public MyPageRequest(String url, Class<T> classz, Response.Listener<MyResponse<MyPage<T>>> listener,  Response.ErrorListener errorListener) {

        super(Request.Method.GET, url,null , listener, errorListener);
        mListener = listener;
        this.classz = classz;
        MyLog.d("MyRequest", "url=" + url);
    }

    public MyPageRequest(int method ,String url, Class<T> classz, Response.Listener<MyResponse<MyPage<T>>> listener,  Response.ErrorListener errorListener) {

        super(method, url,null , listener, errorListener);
        mListener = listener;
        this.classz = classz;
        MyLog.d("MyRequest", "url=" + url);
    }

    public MyPageRequest(int method, String url, Class<T> classz, Object requestBodyObject, Response.Listener<MyResponse<MyPage<T>>> listener,  Response.ErrorListener errorListener) {

        super(method, url,null , listener, errorListener);

        this.mRequestBodyObject = requestBodyObject;
        mListener = listener;
        this.classz = classz;
        MyLog.d("MyRequest", "url=" + url);
    }

    @Override
    protected Response<MyResponse<MyPage<T>>> parseNetworkResponse(NetworkResponse response) {

        try
        {
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            //头疼的泛型 类型擦除

/*            Class<T> classz = (Class<T>) getClass();
            Type mySuperClass = classz.getGenericSuperclass();
            Type type = ((ParameterizedType)mySuperClass).getActualTypeArguments()[0];*/




            MyResponse<MyPage<T>> myResponse = gson.fromJson(jsonStr, new TypeToken<MyResponse<MyPage<T>>>(){}.getType());

            JsonParser parser = new JsonParser();
            JsonObject jsonObject =  parser.parse(jsonStr).getAsJsonObject();
            JsonObject jsonPage = jsonObject.getAsJsonObject("content");
            JsonArray jsonArray = jsonPage.getAsJsonArray("contents");

            List<T> listItems = new ArrayList<T>();
            for (int i = 0; i != jsonArray.size(); i++)
            {
                JsonElement el = jsonArray.get(i);
                T item = gson.fromJson(el, classz);

                listItems.add(item);
            }

            myResponse.getContent().setContents(listItems);

            return Response.success(myResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(MyResponse<MyPage<T>> response) {
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

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //return super.getHeaders();
        Map<String,String> headers = new HashMap<String, String>();
        String userName = "aaa";
        String password = "aaa";
        byte[] encodedPassword = (userName + ":" + password).getBytes();
        String strBase64 = new String(Base64.encode(encodedPassword, Base64.DEFAULT));
        headers.put("Authorization", "Basic " + strBase64);
        return headers;
    }

}

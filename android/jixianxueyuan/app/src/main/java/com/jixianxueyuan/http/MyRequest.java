package com.jixianxueyuan.http;

import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.util.AesCbcWithIntegrity;
import com.jixianxueyuan.util.Cryptos;
import com.jixianxueyuan.util.MyLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by pengchao on 5/31/15.
 */
public class MyRequest<T> extends JsonRequest<MyResponse<T>> {

    private static final String URL_SECURE_KEYWORD = "secure";

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

        MyLog.d("MyRequest", "url=" + url);
    }

    public MyRequest(int method, String url, Class<T> clazz, Object requestBodyObject, Response.Listener<MyResponse<T>> listener,  Response.ErrorListener errorListener) {

        super(method, url,null , listener, errorListener);

        this.mRequestBodyObject = requestBodyObject;
        mListener = listener;
        this.clazz = clazz;

        MyLog.d("MyRequest", "url=" + url);
    }

    @Override
    protected Response<MyResponse<T>> parseNetworkResponse(NetworkResponse response) {

        try
        {
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            MyLog.d("MyRequest", "json=" + jsonStr);

            MyResponse<T> myResponse = gson.fromJson(jsonStr, new TypeToken<MyResponse<T>>(){}.getType());

            JsonParser parser = new JsonParser();
            JsonObject jsonObject =  parser.parse(jsonStr).getAsJsonObject();

            if(myResponse.getStatus() == MyResponse.status_error){

            }

            if(jsonObject.has("content"))
            {
                if(myResponse.isEncryp()){

                    UserDTO userDTO = MyApplication.getContext().getMine().getUserInfo();


                    JsonElement contentObject = jsonObject.get("content");
                    String base64EncrypText = contentObject.getAsString();
                    String[] encryItems =  base64EncrypText.split(":");
                    byte[] ivBytes = Base64.decode(encryItems[0], Base64.DEFAULT);
                    byte[] secretKey = Base64.decode(userDTO.getToken(), Base64.DEFAULT);
                    byte[] aesEncrypText =  Base64.decode(encryItems[1], Base64.DEFAULT);
                    String contentText = Cryptos.aesDecrypt(aesEncrypText, secretKey, ivBytes);

                    //unzip
/*                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ByteArrayInputStream in = new ByteArrayInputStream(contentText.getBytes());
                    try {
                        GZIPInputStream gunzip = new GZIPInputStream(in);
                        byte[] buffer = new byte[512];
                        int n;
                        while ((n = gunzip.read(buffer))>= 0) {
                            out.write(buffer, 0, n);
                        }

                        contentText = out.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/


                    T content = gson.fromJson(contentText, clazz);
                    myResponse.setContent(content);
                }
                else {
                    JsonElement contentObject = jsonObject.get("content");
                    if(contentObject == null)
                    {
                        myResponse.setContent(null);
                    }
                    else
                    {
                        T content = gson.fromJson(contentObject, clazz);
                        myResponse.setContent(content);
                    }
                }

            }

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
            MyLog.d("MyRequest", "json content=" + mRequestBody);

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
        if(getOriginUrl().contains(URL_SECURE_KEYWORD)){
            UserDTO userDTO = MyApplication.getContext().getMine().getUserInfo();
            Map<String,String> headers = new HashMap<String, String>();
            String userName = userDTO.getLoginName();
            String password = userDTO.getLoginName();
            byte[] encodedPassword = (userName + ":" + password).getBytes();
            String strBase64 = new String(Base64.encode(encodedPassword, Base64.DEFAULT));
            strBase64 = strBase64.replace("\n", "");
            headers.put("Authorization", "Basic " + strBase64);
            return headers;
        }else {
            return super.getHeaders();
        }
    }
}

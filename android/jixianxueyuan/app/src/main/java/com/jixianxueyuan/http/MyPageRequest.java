package com.jixianxueyuan.http;

import android.util.Base64;
import android.util.Log;

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
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.util.Cryptos;
import com.jixianxueyuan.util.MyLog;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by pengchao on 5/31/15.
 */
public class MyPageRequest<T> extends JsonRequest<MyResponse<MyPage<T>>> {

    private static final String URL_SECURE_KEYWORD = "secure";

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


            JsonParser parser = new JsonParser();
            JsonObject jsonObject =  parser.parse(jsonStr).getAsJsonObject();

            JsonElement statusElement = jsonObject.get("status");
            int status = statusElement.getAsInt();
            JsonElement encrypElement = jsonObject.get("encryp");
            boolean encryp = encrypElement.getAsBoolean();


            MyResponse<String> baseResponse = gson.fromJson(jsonStr, new TypeToken<MyResponse<Object>>(){}.getType());

            if(baseResponse.getStatus() == MyResponse.status_error){

            }

            if(baseResponse.isEncryp()){

                UserDTO userDTO = MyApplication.getContext().getMine().getUserInfo();

                JsonElement contentElement = jsonObject.get("content");
                String base64Content = contentElement.getAsString();

                String[] encryItems =  base64Content.split(":");
                byte[] ivBytes = Base64.decode(encryItems[0], Base64.DEFAULT);
                byte[] encrypBytes =  Base64.decode(encryItems[1], Base64.DEFAULT);
                byte[] secretKey = Base64.decode(userDTO.getToken(), Base64.DEFAULT);
                String contentText = Cryptos.aesDecrypt(encrypBytes, secretKey, ivBytes);

                //unzip
/*                ByteArrayOutputStream out = new ByteArrayOutputStream();
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


                JsonObject jsonPage = parser.parse(contentText).getAsJsonObject();

                MyPage<T> myPage = new MyPage<T>();
                myPage.setTotalPages(jsonPage.get("totalPages").getAsInt());
                myPage.setCurPage(jsonPage.get("curPage").getAsInt());
                myPage.setTotalElements(jsonPage.get("totalElements").getAsLong());

                JsonArray jsonArray = jsonPage.getAsJsonArray("contents");
                //List<T> itemList = gson.fromJson(jsonPage.get("contents"), new TypeToken<List<T>>() {}.getType());
                //myPage.setContents(itemList);
                List<T> listItems = new ArrayList<T>();
                for (int i = 0; i != jsonArray.size(); i++)
                {
                    JsonElement el = jsonArray.get(i);
                    T item = gson.fromJson(el, classz);

                    listItems.add(item);
                }
                myPage.setContents(listItems);



                MyResponse<MyPage<T>> resultResponse = new MyResponse<MyPage<T>>();

                resultResponse.setStatus(baseResponse.getStatus());
                resultResponse.setError(baseResponse.getError());
                resultResponse.setContent(myPage);

                return Response.success(resultResponse, HttpHeaderParser.parseCacheHeaders(response));


            }else {

                MyResponse<MyPage<T>> resultResponse = gson.fromJson(jsonStr, new TypeToken<MyResponse<MyPage<T>>>() {}.getType());

                JsonObject jsonPage = jsonObject.getAsJsonObject("content");
                JsonArray jsonArray = jsonPage.getAsJsonArray("contents");

                List<T> listItems = new ArrayList<T>();
                for (int i = 0; i != jsonArray.size(); i++)
                {
                    JsonElement el = jsonArray.get(i);
                    T item = gson.fromJson(el, classz);

                    listItems.add(item);
                }

                resultResponse.getContent().setContents(listItems);
                return Response.success(resultResponse, HttpHeaderParser.parseCacheHeaders(response));
            }



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
        if(getOrc iginUrl().contains(URL_SECURE_KEYWORD)){
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

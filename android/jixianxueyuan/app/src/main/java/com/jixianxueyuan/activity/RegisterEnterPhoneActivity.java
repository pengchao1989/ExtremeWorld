package com.jixianxueyuan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 9/13/15.
 */
public class RegisterEnterPhoneActivity extends Activity{

    @InjectView(R.id.register_enter_phone_number)EditText phoneNumberEditText;
    private String phoneNumber;

    private AlertDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_enter_phone_activity);
        ButterKnife.inject(this);
    }

    private boolean checkPhoneNumber(String phoneNumber){
        if(StringUtils.isBlank(phoneNumber)){
            return false;
        }
        if(phoneNumber.length() != 11){
            return false;
        }

        return true;
    }

    private void requestVerificationCode(){

        progressDialog = new SpotsDialog(this,R.style.ProgressDialogUpdating);
        progressDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.verification_code() + "?phone=" + phoneNumber;

        MyRequest<Boolean> myRequest = new MyRequest<Boolean>(Request.Method.GET, url, Boolean.class,
                new Response.Listener<MyResponse<Boolean>>() {
                    @Override
                    public void onResponse(MyResponse<Boolean> response) {
                        if(response.getStatus() == MyResponse.status_ok){

                            Intent intent = new Intent(RegisterEnterPhoneActivity.this, RegisterActivity.class);
                            intent.putExtra(RegisterActivity.REGISTER_TYPE, RegisterActivity.REGISTER_TYPE_PHONE);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);

                        }else if(response.getStatus() == MyResponse.status_ok){
                            MyErrorHelper.showErrorToast(RegisterEnterPhoneActivity.this, response.getError());
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        MyVolleyErrorHelper.showError(RegisterEnterPhoneActivity.this, error);
                    }
        });

        queue.add(myRequest);
    }


    @OnClick(R.id.register_enter_phone_next)void onNextClick(){

        phoneNumber = phoneNumberEditText.getText().toString();
        boolean checkResult = checkPhoneNumber(phoneNumber);
        if(!checkResult){
            Toast.makeText(RegisterEnterPhoneActivity.this, getString(R.string.err_phone_number),Toast.LENGTH_SHORT).show();
            return;
        }

        requestVerificationCode();

    }

}

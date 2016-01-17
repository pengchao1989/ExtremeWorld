package com.jixianxueyuan.activity.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.BaseActivity;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.request.UserAttributeRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;

import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 12/23/15.
 */
public class ModifyProfileAttrBaseActivity extends BaseActivity {

    public static final String INTENT_ATTRIBUTE_KEY = "INTENT_ATTRIBUTE_KEY";
    public static final String INTENT_ATTRIBUTE_VALUE = "INTENT_ATTRIBUTE_VALUE";
    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_HINT = "INTENT_HINT";

    protected String attributeKey;
    protected String attributeValue;
    protected String title;
    protected String hint;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initIntentAttr();
    }

    protected void initIntentAttr(){
        Intent intent = getIntent();
        attributeKey = intent.getStringExtra(INTENT_ATTRIBUTE_KEY);
        attributeValue = intent.getStringExtra(INTENT_ATTRIBUTE_VALUE);
        title = intent.getStringExtra(INTENT_TITLE);
        hint = intent.getStringExtra(INTENT_HINT);
    }

    protected void updateAttribute(){
        requestUpdateUserAttr();
    }


    private void requestUpdateUserAttr(){

        progressDialog = new SpotsDialog(this, R.style.ProgressDialogUpdating);
        progressDialog.show();

        String url = ServerMethod.profile_update_attribute();

        UserAttributeRequestDTO userAttributeRequestDTO = new UserAttributeRequestDTO();
        userAttributeRequestDTO.setAttributeName(attributeKey);
        userAttributeRequestDTO.setAttributeValue(attributeValue);

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.POST, url, UserDTO.class, userAttributeRequestDTO,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {
                        progressDialog.dismiss();
                        if(response.getStatus() == MyResponse.status_ok){
                            MyApplication.getContext().getMine().setUserInfo(response.getContent());
                            MyApplication.getContext().getMine().WriteSerializationToLocal(ModifyProfileAttrBaseActivity.this);
                            ModifyProfileAttrBaseActivity.this.finish();
                        }else {
                            MyErrorHelper.showErrorToast(ModifyProfileAttrBaseActivity.this, response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MyVolleyErrorHelper.showError(ModifyProfileAttrBaseActivity.this, error);
            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }
}

package com.jixianxueyuan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TaxonomyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.request.WeiXinWebPage;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 3/24/16.
 */
public class CreateNewsActivity extends BaseActivity {

    @BindView(R.id.create_news_actionbar)
    MyActionBar myActionBar;
    @BindView(R.id.create_news_url)
    EditText urlEditText;
    @BindView(R.id.submit_btn)
    Button mSubmitButton;


    private AlertDialog progressDialog;


    private WeiXinWebPage mWeiXinWebPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_news_activity);

        ButterKnife.bind(this);


        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    mSubmitButton.setEnabled(true);
                }else {
                    mSubmitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.submit_btn)void onSubmitClick(){
        if (checkParams()){
            submitContent();
        }
    }

    private boolean checkParams(){

        if(TextUtils.isEmpty(urlEditText.getText().toString())){
            Toast.makeText(this, R.string.url_is_empty, Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    private void buildTopicParam(){
        mWeiXinWebPage = new WeiXinWebPage();
        mWeiXinWebPage.setUrl(urlEditText.getText().toString());
    }


    private void submitContent() {
        showProgress();
        String url = ServerMethod.topic_submit_weixin_page();

        buildTopicParam();

        MyRequest<TopicDTO> myRequest = new MyRequest(Request.Method.POST,url,TopicDTO.class, mWeiXinWebPage,
                new Response.Listener<MyResponse<TopicDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok) {
                            hideProgress();
                            Toast.makeText(CreateNewsActivity.this, R.string.add_topic_success, Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        MyVolleyErrorHelper.showError(CreateNewsActivity.this, error);
                        hideProgress();
                    }
                });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void showProgress(){
        if (progressDialog == null){
            progressDialog = new SpotsDialog(this,R.style.ProgressDialogWait);
        }
        progressDialog.show();
    }

    private void hideProgress(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }


    public static void startActivity(Context context, long taxonomyId){
        Intent intent = new Intent(context, CreateNewsActivity.class);
        intent.putExtra(TopicType.TOPIC_TAXONOMY_ID, taxonomyId);
        context.startActivity(intent);
    }
}

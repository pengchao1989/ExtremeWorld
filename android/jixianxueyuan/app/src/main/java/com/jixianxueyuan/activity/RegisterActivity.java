package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.tencent.tauth.Tencent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 7/9/15.
 */
public class RegisterActivity extends Activity {

    private String hobby;
    private String nickName;
    private String gender;

    @InjectView(R.id.register_nick_name)EditText nickNameEditText;
    @InjectView(R.id.register_birth)EditText birthEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        ButterKnife.inject(this);

        initView();
    }

    private void initView(){

        hobby = Util.getApplicationMetaString(this, "hobby");
        MyLog.d(this.getClass().getSimpleName(), "hobby=" + hobby);

        Intent intent = this.getIntent();
        nickName = intent.getStringExtra("nickName");
        gender  = intent.getStringExtra("gender");

        nickNameEditText.setText(nickName);

    }

}

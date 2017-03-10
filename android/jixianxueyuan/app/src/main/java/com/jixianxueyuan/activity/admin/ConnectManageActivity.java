package com.jixianxueyuan.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.BaseActivity;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-3-10.
 */

public class ConnectManageActivity extends BaseActivity {

    @BindView(R.id.action_bar)
    MyActionBar myActionBar;
    @BindView(R.id.admin_connect_manage_server_edit)
    EditText serverEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_conncet_manage_activity);
        ButterKnife.bind(this);

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverUrl = serverEditText.getText().toString();
                ServerMethod.updateCustomServerUrl(ConnectManageActivity.this, serverUrl);
            }
        });

    }


}

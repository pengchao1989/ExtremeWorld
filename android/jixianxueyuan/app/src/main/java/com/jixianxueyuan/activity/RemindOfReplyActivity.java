package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.InjectView;

/**
 * Created by pengchao on 8/9/15.
 */
public class RemindOfReplyActivity extends Activity {

    @InjectView(R.id.remind_reply_actionbar)
    MyActionBar actionBar;
    @InjectView(R.id.remind_reply_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.remind_reply_listview)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.remind_reply_activity);
    }

    private void requestRemindReplyList(){

    }


}

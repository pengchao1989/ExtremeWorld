package com.jixianxueyuan.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jixianxueyuan.R;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 12/23/15.
 */
public class ModifyProfileAttrEditTextActivity extends ModifyProfileAttrBaseActivity {

    @BindView(R.id.modify_profile_attr_edittext_activity_actionbar)
    MyActionBar myActionBar;
    @BindView(R.id.modify_profile_attr_edittext_activity_view)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_profile_attr_edittext_activity);

        ButterKnife.bind(this);

        myActionBar.setTitle(title);
        editText.setHint(hint);
        editText.setText(attributeValue);

        myActionBar.setActionOnClickListener(new MyActionBar.MyActionBarListener() {
            @Override
            public void onFirstActionClicked() {
                attributeValue = editText.getText().toString();
                updateAttribute();
            }

            @Override
            public void onSecondActionClicked() {

            }
        });
    }
}

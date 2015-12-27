package com.jixianxueyuan.activity.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jixianxueyuan.R;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 12/23/15.
 */
public class ModifyProfileAttrEditTextActivity extends ModifyProfileAttrBaseActivity {

    @InjectView(R.id.modify_profile_attr_edittext_activity_actionbar)
    MyActionBar myActionBar;
    @InjectView(R.id.modify_profile_attr_edittext_activity_view)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_profile_attr_edittext_activity);

        ButterKnife.inject(this);

        myActionBar.setTitle(title);
        editText.setHint(hint);
        editText.setText(attributeValue);

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attributeValue = editText.getText().toString();
                updateAttribute();
            }
        });
    }

}

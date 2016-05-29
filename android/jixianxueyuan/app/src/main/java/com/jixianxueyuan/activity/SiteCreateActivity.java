package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.location.MyLocation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 5/29/16.
 */
public class SiteCreateActivity extends BaseActivity{

    private static final int REQUEST_CODE_PICKET_ADDRESS = 1;

    @InjectView(R.id.site_create_name_layout)
    LinearLayout nameLayout;
    @InjectView(R.id.site_create_address_layout)
    LinearLayout addressLayout;
    @InjectView(R.id.site_create_longitude_layout)
    LinearLayout longitudeLayout;
    @InjectView(R.id.site_create_latitude_layout)
    LinearLayout latitudeLinearLayout;
    @InjectView(R.id.site_create_name_edit_text)
    EditText nameEditText;
    @InjectView(R.id.site_create_address_text)
    TextView addressEditText;
    @InjectView(R.id.site_create_longitude_text)
    TextView longitudeEditText;
    @InjectView(R.id.site_create_latitude_text)
    TextView latitudeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_create_activity);

        ButterKnife.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_PICKET_ADDRESS){
                MyLocation myLocation = (MyLocation) data.getSerializableExtra("location");
                addressEditText.setText(myLocation.getAddress());
                latitudeEditText.setText(String.valueOf(myLocation.getLatitude()));
                longitudeEditText.setText(String.valueOf(myLocation.getLongitude()));
            }
        }
    }

    @OnClick(R.id.site_create_address_layout)void onAddressClick(){
        Intent intent = new Intent(SiteCreateActivity.this, AddressPicketActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICKET_ADDRESS);
    }
}
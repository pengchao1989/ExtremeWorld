package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.volley.Request;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.HobbyCheckboxGradAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.UploadPrefixName;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.HobbyMinDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.SiteDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.location.MyLocation;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUploadListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 5/29/16.
 */
public class SiteCreateActivity extends BaseActivity{

    private static final int REQUEST_CODE_PICKET_ADDRESS = 1;
    public static final int REQUEST_IMAGE_CODE = 2;
    public static final int CROP_IMAGE_CODE = 3;

    @InjectView(R.id.site_create_front_image)
    ImageView frontImageView;
    @InjectView(R.id.site_create_select_front_image)
    ImageView selectFrontImageVIew;
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
    @InjectView(R.id.site_create_des_edit)
    EditText desEditText;
    @InjectView(R.id.site_create_hobby_select_gridview)
    GridView gridView;

    //progress
    @InjectView(R.id.create_site_upload_progress_layout)
    RelativeLayout progressLayout;
    @InjectView(R.id.create_site_upload_progress_view)
    ProgressBar uploadProgress;
    @InjectView(R.id.create_site_upload_progress_textview)
    TextView progressTextView;

    private HobbyCheckboxGradAdapter hobbyCheckboxGradAdapter;

    private String frontImagePath;
    private String frontImageUrl;
    private QiniuSingleImageUpload qiniuSingleImageUpload;

    private static final int HANDLER_UPDATE_PROGRESS = 0x1;
    private static final int HANDLE_PROGRESS_PICTURE = 0x2;
    private static final int HANDLE_PROGRESS_IMAGE = 0x3;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (HANDLER_UPDATE_PROGRESS == msg.what){
                Bundle progressData = msg.getData();
                switch (progressData.getInt("type")){
                    case HANDLE_PROGRESS_PICTURE:
                        progressTextView.setText("正在上传图片" + String.format("%.1f",progressData.getDouble("percent") * 100) + "%")  ;
                        break;
                    case HANDLE_PROGRESS_IMAGE:
                        progressTextView.setText(getString(R.string.wait)+ String.format("%.1f",progressData.getDouble("percent") * 100) + "%")  ;
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_create_activity);

        ButterKnife.inject(this);

        hobbyCheckboxGradAdapter = new HobbyCheckboxGradAdapter(this);
        gridView.setAdapter(hobbyCheckboxGradAdapter);
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
            }else if (requestCode == REQUEST_IMAGE_CODE){
                List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                if(pathList.size() > 0){
                    String path = pathList.get(0);
                    Intent intent = new Intent(this, CropBgActivity.class);
                    intent.putExtra("imagePath", path);
                    startActivityForResult(intent,CROP_IMAGE_CODE);
                }
            }else if(requestCode == CROP_IMAGE_CODE){
                String filePath = data.getStringExtra("filePath");
                if(filePath != null){
                    frontImagePath = filePath;
                    ImageLoader.getInstance().displayImage("file://" + filePath, frontImageView, ImageLoaderConfig.getHeadOption(this));
                    //selectFrontImageVIew.setVisibility(View.GONE);
                }
            }
        }
    }

    @OnClick(R.id.site_create_front_image)void onSelectFrontClick(){
        showImageSelectActivity();
    }

    @OnClick(R.id.site_create_address_layout)void onAddressClick(){
        Intent intent = new Intent(SiteCreateActivity.this, AddressPicketActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICKET_ADDRESS);
    }

    @OnClick(R.id.site_create_submit_button)void onSubmitClick(){
        if (checkParams()){
            uploadUserBg();
        }
    }

    private void showImageSelectActivity(){
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }


    private boolean checkParams(){
        if (TextUtils.isEmpty(nameEditText.getText())){
            Toast.makeText(this, R.string.site_name_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(frontImagePath)){
            Toast.makeText(this, R.string.please_select_picture, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, R.string.please_select_address, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(latitudeEditText.getText()) || TextUtils.isEmpty(longitudeEditText.getText())){
            Toast.makeText(this, R.string.lat_lng_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(desEditText.getText())){
            Toast.makeText(this, R.string.site_des_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hobbyCheckboxGradAdapter.getSelectedHobbySets().size() == 0){
            Toast.makeText(this, R.string.please_select_hobby, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private SiteDTO buildParam(){
        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setName(nameEditText.getText().toString());
        siteDTO.setAddress(addressEditText.getText().toString());
        siteDTO.setLatitude(latitudeEditText.getText().toString());
        siteDTO.setLongitude(longitudeEditText.getText().toString());
        siteDTO.setFrontImg(frontImageUrl);
        siteDTO.setDescription(desEditText.getText().toString());
        siteDTO.setType(1);
        Set<HobbyDTO> hobbyDTOSet = hobbyCheckboxGradAdapter.getSelectedHobbySets();
        List<HobbyMinDTO> hobbyMinDTOList = new ArrayList<HobbyMinDTO>();
        for (HobbyDTO hobbyDTO : hobbyDTOSet){
            HobbyMinDTO hobbyMinDTO = new HobbyMinDTO();
            hobbyMinDTO.setId(hobbyDTO.getId());

            hobbyMinDTOList.add(hobbyMinDTO);
        }
        siteDTO.setHobbys(hobbyMinDTOList);

        return siteDTO;
    }


    private void requestSiteCreate(){
        String url = ServerMethod.site();
        SiteDTO siteDTO = buildParam();

        MyRequest request = new MyRequest<SiteDTO>(Request.Method.POST, url, SiteDTO.class, siteDTO, new com.android.volley.Response.Listener<MyResponse<SiteDTO>>() {
            @Override
            public void onResponse(MyResponse<SiteDTO> response) {
                hideUploadProgressView();
                Toast.makeText(SiteCreateActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                SiteCreateActivity.this.setResult(Activity.RESULT_OK);
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideUploadProgressView();
            }
        });

        MyApplication.getContext().getRequestQueue().add(request);
    }

    private void uploadUserBg(){
        showUploadProgressView();

        if(qiniuSingleImageUpload == null){
            qiniuSingleImageUpload = new QiniuSingleImageUpload(this);
        }
        qiniuSingleImageUpload.upload(frontImagePath, UploadPrefixName.SITE, new QiniuSingleImageUploadListener() {
            @Override
            public void onUploading(double percent) {
                updateProgressView(HANDLE_PROGRESS_PICTURE, percent);
            }

            @Override
            public void onUploadComplete(String url) {
                //提交site
                frontImageUrl = url;
                requestSiteCreate();
            }

            @Override
            public void onError(String error) {
                hideUploadProgressView();
                Toast.makeText(SiteCreateActivity.this, R.string.err, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showUploadProgressView(){
        progressLayout.setVisibility(View.VISIBLE);

    }

    private void hideUploadProgressView(){
        progressLayout.setVisibility(View.GONE);
    }

    private void updateProgressView(int type, double percent){

        Message msg = new Message();
        msg.what = HANDLER_UPDATE_PROGRESS;
        Bundle progressData = new Bundle();
        progressData.putInt("type", type);
        progressData.putDouble("percent", percent);
        msg.setData(progressData);
        handler.sendMessage(msg);

    }
}
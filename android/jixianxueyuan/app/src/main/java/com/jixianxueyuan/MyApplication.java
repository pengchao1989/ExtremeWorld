package com.jixianxueyuan;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;


import com.jixianxueyuan.dto.BaseInfoDTO;
import com.jixianxueyuan.record.service.AssertService;
import com.yixia.camera.VCamera;
import com.yixia.camera.util.DeviceUtils;

import java.io.File;

public class MyApplication extends Application {

	private static MyApplication application;

    BaseInfoDTO baseInfoDTO;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;

		// 设置拍摄视频缓存路径
		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Camera/VCameraDemo/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
		}
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this);

		//解压assert里面的文件
		startService(new Intent(this, AssertService.class));
	}


	public static Context getContext() {
		return application;
	}

    public BaseInfoDTO getBaseInfoDTO() {
        return baseInfoDTO;
    }

    public void setBaseInfoDTO(BaseInfoDTO baseInfoDTO) {
        this.baseInfoDTO = baseInfoDTO;
    }
}

package com.jixianxueyuan.record;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore.Video;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.javacv.FrameRecorder;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.jixianxueyuan.R;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.util.Collections;
import java.util.List;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;


public class FFmpegRecorderActivity extends Activity implements OnClickListener, OnTouchListener {

	private final static String CLASS_LABEL = "RecordActivity";
	private final static String LOG_TAG = CLASS_LABEL;

	private PowerManager.WakeLock mWakeLock;
	//视频文件的存放地址
	private String strVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "rec_video.mp4";
	//视频文件对象
	private File fileVideoPath = null;
	//视频文件在系统中存放的url
	private Uri uriVideoPath = null;
	//判断是否需要录制，点击下一步时暂停录制
	private boolean rec = false;
	//判断是否需要录制，手指按下继续，抬起时暂停
	boolean recording = false;
	//判断是否开始了录制，第一次按下屏幕时设置为true
	boolean	isRecordingStarted = false;
	//是否开启闪光灯
	boolean isFlashOn = false;
	TextView txtTimer, txtRecordingSize;
	//分别为闪光灯按钮、取消按钮、下一步按钮、转置摄像头按钮
	Button flashIcon = null,cancelBtn,nextBtn,switchCameraIcon = null;
	boolean nextEnabled = false;
	
	//录制视频和保存音频的类
	private volatile NewFFmpegFrameRecorder videoRecorder;
	
	//判断是否是前置摄像头
	private boolean isPreviewOn = false;
	//当前录制的质量，会影响视频清晰度和文件大小
	private int currentResolution = CONSTANTS.RESOLUTION_MEDIUM_VALUE;
	private Camera mCamera;

	//预览的宽高和屏幕宽高
	private int previewWidth = 480, screenWidth = 480;
	private int previewHeight = 480, screenHeight = 800;
	
	//音频的采样率，recorderParameters中会有默认值
	private int sampleRate = 44100;
	//调用系统的录制音频类
	private AudioRecord audioRecord; 
	//录制音频的线程
	private AudioRecordRunnable audioRecordRunnable;
	private Thread audioThread;
	//开启和停止录制音频的标记
	volatile boolean runAudioThread = true;

	//摄像头以及它的参数
	private Camera cameraDevice;
	private CameraView cameraView;
	Parameters cameraParameters = null;
	//IplImage对象,用于存储摄像头返回的byte[]，以及图片的宽高，depth，channel等
	private IplImage yuvIplImage = null;
	//分别为 默认摄像头（后置）、默认调用摄像头的分辨率、被选择的摄像头（前置或者后置）
	int defaultCameraId = -1, defaultScreenResolution = -1 , cameraSelection = 0;

	//Handler handler = new Handler();
	/*private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			if(rec)
				setTotalVideoTime();
			handler.postDelayed(this, 200);
		}
	};*/

	private Dialog dialog = null;
	//包含显示摄像头数据的surfaceView
	RelativeLayout topLayout = null;

	//第一次按下屏幕时记录的时间
	long firstTime = 0;
	//手指抬起是的时间
	long startPauseTime = 0;
	//每次按下手指和抬起之间的暂停时间
	long totalPauseTime = 0;
	//手指抬起是的时间
	long pausedTime = 0;
	//总的暂停时间
	long stopPauseTime = 0;
	//录制的有效总时间
	long totalTime = 0;
	//视频帧率
	private int frameRate = 30;
	//录制的最长时间
	private int recordingTime = 8000;
	//录制的最短时间
	private int recordingMinimumTime = 6000;
	//提示换个场景
	private int recordingChangeTime = 3000;
	
	boolean recordFinish = false;
	private  Dialog creatingProgress;
	
	//音频时间戳
	private volatile long mAudioTimestamp = 0L;
	//以下两个只做同步标志，没有实际意义
	private final int[] mVideoRecordLock = new int[0];
	private final int[] mAudioRecordLock = new int[0];
	private long mLastAudioTimestamp = 0L;
	private volatile long mAudioTimeRecorded;
	private long frameTime = 0L;
	//每一幀的数据结构
	private SavedFrames lastSavedframe = new SavedFrames(null,0L);
	//视频时间戳
	private long mVideoTimestamp = 0L;
	//时候保存过视频文件
	private boolean isRecordingSaved = false;
	private boolean isFinalizing = false;
	
	//进度条
	private ProgressView progressView;
	//捕获的第一幀的图片
	private String imagePath = null;
	private RecorderState currentRecorderState = RecorderState.PRESS;
	private ImageView stateImageView;
	
	private byte[] firstData = null;
	
	private Handler mHandler;
	private void initHandler(){
		mHandler = new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				switch (msg.what) {
				/*case 1:
					final byte[] data = (byte[]) msg.obj;
					ThreadPoolUtils.execute(new Runnable() {
						
						@Override
						public void run() {
							getFirstCapture(data);
						}
					});
					break;*/
				case 2:
					int resId = 0;
					if(currentRecorderState == RecorderState.PRESS){
						resId = R.mipmap.video_text01;
					}else if(currentRecorderState == RecorderState.LOOSEN){
						resId = R.mipmap.video_text02;
					}else if(currentRecorderState == RecorderState.CHANGE){
						resId = R.mipmap.video_text03;
					}else if(currentRecorderState == RecorderState.SUCCESS){
						resId = R.mipmap.video_text04;
					}
					stateImageView.setImageResource(resId);
					break;
				case 3:
					if(!recording)
						initiateRecording(true);
					else{
						//更新暂停的时间
						stopPauseTime = System.currentTimeMillis();
						totalPauseTime = stopPauseTime - startPauseTime - ((long) (1.0/(double)frameRate)*1000);
						pausedTime += totalPauseTime;
					}
					rec = true;
					//开始进度条增长
					progressView.setCurrentState(ProgressView.State.START);
					//setTotalVideoTime();
				break;
				case 4:
					//设置进度条暂停状态
					progressView.setCurrentState(ProgressView.State.PAUSE);
					//将暂停的时间戳添加到进度条的队列中
					progressView.putProgressList((int) totalTime);
					rec = false;
					startPauseTime = System.currentTimeMillis();
					if(totalTime >= recordingMinimumTime){
						currentRecorderState = RecorderState.SUCCESS;
						mHandler.sendEmptyMessage(2);
					}else if(totalTime >= recordingChangeTime){
						currentRecorderState = RecorderState.CHANGE;
						mHandler.sendEmptyMessage(2);
					}
					break;
				case 5:
					currentRecorderState = RecorderState.SUCCESS;
					mHandler.sendEmptyMessage(2);
					break;
				default:
					break;
				}
			}
		};
	}
	
	//neon库对opencv做了优化
	static {
		System.loadLibrary("checkneon");
	}

	public native static int  checkNeonFromJNI();
	private boolean initSuccess = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recorder);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, CLASS_LABEL); 
		mWakeLock.acquire(); 

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		//Find screen dimensions
		screenWidth = displaymetrics.widthPixels;
		screenHeight = displaymetrics.heightPixels;
		
		initHandler();
		
		initLayout();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!initSuccess)
			return false;
		return super.dispatchTouchEvent(ev);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessage(2);
		
		if (mWakeLock == null) {
			//获取唤醒锁,保持屏幕常亮
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, CLASS_LABEL);
			mWakeLock.acquire();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!isFinalizing)
			finish();
		
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Log.i("video", this.getLocalClassName()+"—destory");
		recording = false;
		runAudioThread = false;
		
		releaseResources();
			
		if (cameraView != null) {
			cameraView.stopPreview();
			if(cameraDevice != null){
				cameraDevice.setPreviewCallback(null);
				cameraDevice.release();
			}
			cameraDevice = null;
		}
		firstData = null;
		mCamera = null;
		cameraView = null;
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
	private void initLayout()
	{
		stateImageView = (ImageView) findViewById(R.id.recorder_surface_state);
		
		progressView = (ProgressView) findViewById(R.id.recorder_progress);
		cancelBtn = (Button) findViewById(R.id.recorder_cancel);
		cancelBtn.setOnClickListener(this);
		nextBtn = (Button) findViewById(R.id.recorder_next);
		nextBtn.setOnClickListener(this);
		//txtTimer = (TextView)findViewById(R.id.txtTimer);
		flashIcon = (Button)findViewById(R.id.recorder_flashlight);
		switchCameraIcon = (Button)findViewById(R.id.recorder_frontcamera);
		flashIcon.setOnClickListener(this);
		
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			switchCameraIcon.setVisibility(View.VISIBLE);
		}
		initCameraLayout();
	}

	private void initCameraLayout() {
		new AsyncTask<String, Integer, Boolean>(){

			@Override
			protected Boolean doInBackground(String... params) {
				boolean result = setCamera();
				
				if(!initSuccess){
					
					initVideoRecorder();
					startRecording();
					
					initSuccess = true;
				}
				
				return result;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result || cameraDevice == null){
					//FuncCore.showToast(FFmpegRecorderActivity.this, "无法连接到相机");
					finish();
					return;
				}
				
				topLayout = (RelativeLayout) findViewById(R.id.recorder_surface_parent);
				if(topLayout != null && topLayout.getChildCount() > 0)
					topLayout.removeAllViews();
				
				cameraView = new CameraView(FFmpegRecorderActivity.this, cameraDevice);
				
				handleSurfaceChanged();
				//设置surface的宽高
				RelativeLayout.LayoutParams layoutParam1 = new RelativeLayout.LayoutParams(screenWidth,(int) (screenWidth*(previewWidth/(previewHeight*1f))));
				layoutParam1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				//int margin = Util.calculateMargin(previewWidth, screenWidth);
				//layoutParam1.setMargins(0,margin,0,margin);

				RelativeLayout.LayoutParams layoutParam2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				layoutParam2.topMargin = screenWidth;
				
				View view = new View(FFmpegRecorderActivity.this);
				view.setFocusable(false);
				view.setBackgroundColor(Color.BLACK);
				view.setFocusableInTouchMode(false);
				
				topLayout.addView(cameraView, layoutParam1);
				topLayout.addView(view,layoutParam2);
				
				topLayout.setOnTouchListener(FFmpegRecorderActivity.this);
				
				switchCameraIcon.setOnClickListener(FFmpegRecorderActivity.this);
				if(cameraSelection == CameraInfo.CAMERA_FACING_FRONT)
					flashIcon.setVisibility(View.GONE);
				else
					flashIcon.setVisibility(View.VISIBLE);
			}
			
		}.execute("start");
	}

	private boolean setCamera()
	{
		try
		{
			
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO)
			{
				int numberOfCameras = Camera.getNumberOfCameras();
				
				CameraInfo cameraInfo = new CameraInfo();
				for (int i = 0; i < numberOfCameras; i++) {
					Camera.getCameraInfo(i, cameraInfo);
					if (cameraInfo.facing == cameraSelection) {
						defaultCameraId = i;
					}
				}
			}
			stopPreview();
			if(mCamera != null)
				mCamera.release();
			
			if(defaultCameraId >= 0)
				cameraDevice = Camera.open(defaultCameraId);
			else
				cameraDevice = Camera.open();

		}
		catch(Exception e)
		{	
			return false;
		}
		return true;
	}


	private void initVideoRecorder() {
		strVideoPath = Util.createFinalPath(this);//Util.createTempPath(tempFolderPath);
		
		RecorderParameters recorderParameters = Util.getRecorderParameter(currentResolution);
		sampleRate = recorderParameters.getAudioSamplingRate();
		frameRate = recorderParameters.getVideoFrameRate();
		frameTime = (1000000L / frameRate);
		
		fileVideoPath = new File(strVideoPath); 
		videoRecorder = new NewFFmpegFrameRecorder(strVideoPath, 480, 480, 1);
		videoRecorder.setFormat(recorderParameters.getVideoOutputFormat());
		videoRecorder.setSampleRate(recorderParameters.getAudioSamplingRate());
		videoRecorder.setFrameRate(recorderParameters.getVideoFrameRate());
		videoRecorder.setVideoCodec(recorderParameters.getVideoCodec());
		videoRecorder.setVideoQuality(recorderParameters.getVideoQuality()); 
		videoRecorder.setAudioQuality(recorderParameters.getVideoQuality());
		videoRecorder.setAudioCodec(recorderParameters.getAudioCodec());
		videoRecorder.setVideoBitrate(recorderParameters.getVideoBitrate());
		videoRecorder.setAudioBitrate(recorderParameters.getAudioBitrate());
		
		audioRecordRunnable = new AudioRecordRunnable();
		audioThread = new Thread(audioRecordRunnable);
	}

	public void startRecording() {

		try {
			videoRecorder.start();
			audioThread.start();

		} catch (NewFFmpegFrameRecorder.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止录制
	 * @author QD
	 *
	 */
	public class AsyncStopRecording extends AsyncTask<Void,Integer,Void>{
		
		private ProgressBar bar;
		private TextView progress;
		@Override
		protected void onPreExecute() {
			isFinalizing = true;
			recordFinish = true;
			runAudioThread = false;
			
			//创建处理进度条
			creatingProgress= new Dialog(FFmpegRecorderActivity.this,R.style.Dialog_loading_noDim);
			Window dialogWindow = creatingProgress.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = (int) (getResources().getDisplayMetrics().density*240);
			lp.height = (int) (getResources().getDisplayMetrics().density*80);
			lp.gravity = Gravity.CENTER;
			dialogWindow.setAttributes(lp);
			creatingProgress.setCanceledOnTouchOutside(false);
			creatingProgress.setContentView(R.layout.activity_recorder_progress);
			
			progress = (TextView) creatingProgress.findViewById(R.id.recorder_progress_progresstext);
			bar = (ProgressBar) creatingProgress.findViewById(R.id.recorder_progress_progressbar);
			creatingProgress.show();
			
			//txtTimer.setVisibility(View.INVISIBLE);
			//handler.removeCallbacks(mUpdateTimeTask);
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			progress.setText(values[0]+"%");
			bar.setProgress(values[0]);
		}
		
		/**
		 * 依据byte[]里的数据合成一张bitmap，
		 * 截成480*480，并且旋转90度后，保存到文件
		 * @param data
		 */
		private void getFirstCapture(byte[] data){
			
			publishProgress(10);
			
			String captureBitmapPath = CONSTANTS.CAMERA_FOLDER_PATH;
			
			captureBitmapPath = Util.createImagePath(FFmpegRecorderActivity.this);
			YuvImage localYuvImage = new YuvImage(data, 17, previewWidth,previewHeight, null);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileOutputStream outStream = null;
			
			publishProgress(50);
			
			try {
				File file = new File(captureBitmapPath);
				if(!file.exists())
					file.createNewFile();
				localYuvImage.compressToJpeg(new Rect(0, 0, previewWidth, previewHeight),100, bos);
				Bitmap localBitmap1 = BitmapFactory.decodeByteArray(bos.toByteArray(),
						0,bos.toByteArray().length);
				
				bos.close();
				
				Matrix localMatrix = new Matrix();
				if (cameraSelection == 0)
					localMatrix.setRotate(90.0F);
				else
					localMatrix.setRotate(270.0F);
				
				Bitmap	localBitmap2 = Bitmap.createBitmap(localBitmap1, 0, 0,
									localBitmap1.getHeight(),
									localBitmap1.getHeight(),
									localMatrix, true);
				
				publishProgress(70);
				
				ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
				localBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
					 
				outStream = new FileOutputStream(captureBitmapPath);
				outStream.write(bos2.toByteArray());
				outStream.close();
				
				localBitmap1.recycle();
				localBitmap2.recycle();
				
				publishProgress(90);
				
				isFirstFrame = false;
				imagePath = captureBitmapPath;
			} catch (FileNotFoundException e) {
				isFirstFrame = true;
				e.printStackTrace();
			} catch (IOException e) {
				isFirstFrame = true;
				e.printStackTrace();
			}        
		}
			
		
		@Override
		protected Void doInBackground(Void... params) {
			if(firstData != null)
				getFirstCapture(firstData);
			isFinalizing = false;
			if (videoRecorder != null && recording) {
				recording = false;
				releaseResources();
			}
			publishProgress(100);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			creatingProgress.dismiss();
			registerVideo();
			returnToCaller(true);
			videoRecorder = null;
		}
		
	}
	
	/**
	 * 放弃视频时弹出框
	 */
	private void showCancellDialog(){
		Util.showDialog(FFmpegRecorderActivity.this, "提示", "确定要放弃本视频吗？", 2, new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				if(msg.what == 1)
					videoTheEnd(false);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (recording) 
			showCancellDialog();
		else
			videoTheEnd(false);
	}

	/**
	 * 录制音频的线程
	 * @author QD
	 *
	 */
	class AudioRecordRunnable implements Runnable {
		
		int bufferSize;
		short[] audioData;
		int bufferReadResult;
		private final AudioRecord audioRecord;
		public volatile boolean isInitialized;
		private int mCount =0;
		private AudioRecordRunnable()
		{
			bufferSize = AudioRecord.getMinBufferSize(sampleRate, 
					AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, 
					AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,bufferSize);
			audioData = new short[bufferSize];
		}

		/**
		 * shortBuffer包含了音频的数据和起始位置
		 * @param shortBuffer
		 */
		private void record(ShortBuffer shortBuffer)
		{
			try
			{
				synchronized (mAudioRecordLock)
				{
					if (videoRecorder != null)
					{
						this.mCount += shortBuffer.limit();
						videoRecorder.record(0,new Buffer[] {shortBuffer});
					}
					return;
				}
			}
			catch (FrameRecorder.Exception localException){}
		}
		
		/**
		 * 更新音频的时间戳
		 */
		private void updateTimestamp()
		{
			if (videoRecorder != null)
			{
				int i = Util.getTimeStampInNsFromSampleCounted(this.mCount);
				if (mAudioTimestamp != i)
				{
					mAudioTimestamp = i;
					mAudioTimeRecorded =  System.nanoTime();
				}
			}
		}

		public void run()
		{
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			this.isInitialized = false;
			if(audioRecord != null)
			{
				//判断音频录制是否被初始化
				while (this.audioRecord.getState() == 0)
				{
					try
					{
						Thread.sleep(100L);
					}
					catch (InterruptedException localInterruptedException)
					{
					}
				}
				this.isInitialized = true;
				this.audioRecord.startRecording();
				while (((runAudioThread) || (mVideoTimestamp > mAudioTimestamp)) && (mAudioTimestamp < (1000 * recordingTime)))
				{
					updateTimestamp();
					bufferReadResult = this.audioRecord.read(audioData, 0, audioData.length);
					if ((bufferReadResult > 0) && ((recording && rec) || (mVideoTimestamp > mAudioTimestamp)))
						record(ShortBuffer.wrap(audioData, 0, bufferReadResult));
				}
				this.audioRecord.stop();
				this.audioRecord.release();
			}
		}
	}
	
	//获取第一幀的图片
	private boolean isFirstFrame = true;
	
		
	/**
	 * 显示摄像头的内容，以及返回摄像头的每一帧数据
	 * @author QD
	 *
	 */
	class CameraView extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {

		private SurfaceHolder mHolder;


		public CameraView(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			cameraParameters = mCamera.getParameters();
			mHolder = getHolder();
			mHolder.addCallback(CameraView.this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mCamera.setPreviewCallback(CameraView.this);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				stopPreview();
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
			}
		}

		public void surfaceChanged(SurfaceHolder  holder, int format, int width, int height) {
			if (isPreviewOn)
				mCamera.stopPreview();
			handleSurfaceChanged();
			startPreview();  
			mCamera.autoFocus(null);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			try {
				mHolder.addCallback(null);
				mCamera.setPreviewCallback(null);
				
			} catch (RuntimeException e) {
			}
		}

		public void startPreview() {
			if (!isPreviewOn && mCamera != null) {
				isPreviewOn = true;
				mCamera.startPreview();
			}
		}

		public void stopPreview() {
			if (isPreviewOn && mCamera != null) {
				isPreviewOn = false;
				mCamera.stopPreview();
			}
		}
	private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) 
	{
		
		byte [] yuv = new byte[imageWidth*imageHeight*3/2];
	    // Rotate the Y luma
	    int i = 0;
	    for(int x = 0;x < imageWidth;x++)
	    {
	        for(int y = imageHeight-1;y >= 0;y--)                               
	        {
	            yuv[i] = data[y*imageWidth+x];
	            i++;
	        }

	    }
	    // Rotate the U and V color components 
	    i = imageWidth*imageHeight*3/2-1;
	    for(int x = imageWidth-1;x > 0;x=x-2)
	    {
	        for(int y = 0;y < imageHeight/2;y++)                                
	        {
	            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
	            i--;
	            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
	            i--;
	        }
	    }
	    return yuv;
	}
	
	private byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) 
	{
		byte [] yuv = new byte[imageWidth*imageHeight*3/2];
		int i = 0;
		int count = 0;

		for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
			yuv[count] = data[i];
			count++;
		}

		i = imageWidth * imageHeight * 3 / 2 - 1;
		for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
				* imageHeight; i -= 2) {
			yuv[count++] = data[i - 1];
			yuv[count++] = data[i];
		}
		return yuv;
	}
	
	private byte[] rotateYUV420Degree270(byte[] data, int imageWidth, int imageHeight) 
	{
	    byte [] yuv = new byte[imageWidth*imageHeight*3/2];
	    int nWidth = 0, nHeight = 0;
	    int wh = 0;
	    int uvHeight = 0;
	    if(imageWidth != nWidth || imageHeight != nHeight)
	    {
	        nWidth = imageWidth;
	        nHeight = imageHeight;
	        wh = imageWidth * imageHeight;
	        uvHeight = imageHeight >> 1;//uvHeight = height / 2
	    }

	    //旋转Y
	    int k = 0;
	    for(int i = 0; i < imageWidth; i++) {
	        int nPos = 0;
	        for(int j = 0; j < imageHeight; j++) {
	        	yuv[k] = data[nPos + i];
	            k++;
	            nPos += imageWidth;
	        }
	    }

	    for(int i = 0; i < imageWidth; i+=2){
	        int nPos = wh;
	        for(int j = 0; j < uvHeight; j++) {
	        	yuv[k] = data[nPos + i];
	        	yuv[k + 1] = data[nPos + i + 1];
	            k += 2;
	            nPos += imageWidth;
	        }
	    }
	    //这一部分可以直接旋转270度，但是图像颜色不对
//	    // Rotate the Y luma
//	    int i = 0;
//	    for(int x = imageWidth-1;x >= 0;x--)
//	    {
//	        for(int y = 0;y < imageHeight;y++)                                 
//	        {
//	            yuv[i] = data[y*imageWidth+x];
//	            i++;
//	        }
//
//	    }
//	    // Rotate the U and V color components 
//		i = imageWidth*imageHeight;
//	    for(int x = imageWidth-1;x > 0;x=x-2)
//	    {
//	        for(int y = 0;y < imageHeight/2;y++)                                
//	        {
//	            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
//	            i++;
//	            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
//	            i++;
//	        }
//	    }
	    return rotateYUV420Degree180(yuv,imageWidth,imageHeight);
	}
	
	public byte[] cropYUV420(byte[] data,int imageW,int imageH,int newImageH){
		int cropH;
		int i,j,count,tmp;
		byte[] yuv = new byte[imageW*newImageH*3/2];
 
		cropH = (imageH - newImageH)/2;
 
		count = 0;
		for(j=cropH;j<cropH+newImageH;j++){
			for(i=0;i<imageW;i++){
				yuv[count++] = data[j*imageW+i];
			}
		}
 
		//Cr Cb
		tmp = imageH+cropH/2;
		for(j=tmp;j<tmp + newImageH/2;j++){
			for(i=0;i<imageW;i++){
				yuv[count++] = data[j*imageW+i];
			}
		}
 
		return yuv;
	}
									 
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		//计算时间戳
		long frameTimeStamp = 0L;
		if(mAudioTimestamp == 0L && firstTime > 0L)
			frameTimeStamp = 1000L * (System.currentTimeMillis() -firstTime);
		else if (mLastAudioTimestamp == mAudioTimestamp)
			frameTimeStamp = mAudioTimestamp + frameTime;
		else
		{
			long l2 = (System.nanoTime() - mAudioTimeRecorded) / 1000L;
			frameTimeStamp = l2 + mAudioTimestamp;
			mLastAudioTimestamp = mAudioTimestamp;
		}
		
		//录制视频
		synchronized (mVideoRecordLock) {
			if (recording && rec && lastSavedframe != null && lastSavedframe.getFrameBytesData() != null && yuvIplImage != null) 
			{
				//保存某一幀的图片
				if(isFirstFrame){
					isFirstFrame = false;
					firstData = data;
					/*Message msg = mHandler.obtainMessage(1);
					msg.obj = data;
					msg.what = 1;
					mHandler.sendMessage(msg);*/
					
				}
				//超过最低时间时，下一步按钮可点击
				totalTime = System.currentTimeMillis() - firstTime - pausedTime - ((long) (1.0/(double)frameRate)*1000);
				if(!nextEnabled && totalTime >= recordingChangeTime){
					nextEnabled = true;
					nextBtn.setEnabled(true);
				}
				
				if(nextEnabled && totalTime >= recordingMinimumTime){
					mHandler.sendEmptyMessage(5);
				}
				
				if(currentRecorderState == RecorderState.PRESS && totalTime >= recordingChangeTime){
					currentRecorderState = RecorderState.LOOSEN;
					mHandler.sendEmptyMessage(2);
				}
				
				mVideoTimestamp += frameTime;
				if(lastSavedframe.getTimeStamp() > mVideoTimestamp)
				mVideoTimestamp = lastSavedframe.getTimeStamp();
				try {
						yuvIplImage.getByteBuffer().put(lastSavedframe.getFrameBytesData());
						videoRecorder.setTimestamp(lastSavedframe.getTimeStamp());
						videoRecorder.record(yuvIplImage);
					} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
						Log.i("recorder", "录制错误"+e.getMessage());
						e.printStackTrace();
					}
				}
				byte[] tempData = rotateYUV420Degree90(data, previewWidth, previewHeight);
				if(cameraSelection == 1)
					tempData = rotateYUV420Degree270(data, previewWidth, previewHeight);
				lastSavedframe = new SavedFrames(tempData,frameTimeStamp);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(!recordFinish){
			if(totalTime< recordingTime){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//如果MediaRecorder没有被初始化
					//执行初始化
					mHandler.removeMessages(3);
					mHandler.removeMessages(4);
					mHandler.sendEmptyMessageDelayed(3,300);
					break;
				case MotionEvent.ACTION_UP:
					mHandler.removeMessages(3);
					mHandler.removeMessages(4);
					if(rec)
						mHandler.sendEmptyMessage(4);
					break;
				}
			}else{
				//如果录制时间超过最大时间，保存视频
				rec = false;
				saveRecording();
			}
		}
		return true;
	}
	/**
	 * 关闭摄像头的预览
	 */
	public void stopPreview() {
		if (isPreviewOn && mCamera != null) {
			isPreviewOn = false;
			mCamera.stopPreview();

		}
	}

	private void handleSurfaceChanged()
	{
		if(mCamera == null){
			//showToast(this, "无法连接到相机");
			finish();
			return;
		}
		//获取摄像头的所有支持的分辨率
		List<Size> resolutionList = Util.getResolutionList(mCamera);
		if(resolutionList != null && resolutionList.size() > 0){
			Collections.sort(resolutionList, new Util.ResolutionComparator());
			Size previewSize =  null;
			if(defaultScreenResolution == -1){
				boolean hasSize = false;
				//如果摄像头支持640*480，那么强制设为640*480
				for(int i = 0;i<resolutionList.size();i++){
					Size size = resolutionList.get(i);
					if(size != null && size.width==640 && size.height==480){
						previewSize = size;
						hasSize = true;
						break;
					}
				}
				//如果不支持设为中间的那个
				if(!hasSize){
					int mediumResolution = resolutionList.size()/2;
					if(mediumResolution >= resolutionList.size())
						mediumResolution = resolutionList.size() - 1;
					previewSize = resolutionList.get(mediumResolution);
				}
			}else{
				if(defaultScreenResolution >= resolutionList.size())
					defaultScreenResolution = resolutionList.size() - 1;
				previewSize = resolutionList.get(defaultScreenResolution);
			}
			//获取计算过的摄像头分辨率
			if(previewSize != null ){
				previewWidth = previewSize.width;
				previewHeight = previewSize.height;
				cameraParameters.setPreviewSize(previewWidth, previewHeight);
				if(videoRecorder != null)
				{
					videoRecorder.setImageWidth(previewWidth);
					videoRecorder.setImageHeight(previewHeight);
				}

			}
		}
		//设置预览帧率
		cameraParameters.setPreviewFrameRate(frameRate);
		//构建一个IplImage对象，用于录制视频
		//和opencv中的cvCreateImage方法一样
		yuvIplImage = IplImage.create(previewHeight, previewWidth,IPL_DEPTH_8U, 2);

		//系统版本为8一下的不支持这种对焦
		if(Build.VERSION.SDK_INT >  Build.VERSION_CODES.FROYO)
		{
			mCamera.setDisplayOrientation(Util.determineDisplayOrientation(FFmpegRecorderActivity.this, defaultCameraId));
			List<String> focusModes = cameraParameters.getSupportedFocusModes();
			if(focusModes != null){
				Log.i("video", Build.MODEL);
				 if (((Build.MODEL.startsWith("GT-I950"))
						 || (Build.MODEL.endsWith("SCH-I959"))
						 || (Build.MODEL.endsWith("MEIZU MX3")))&&focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){

					 cameraParameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				 }else if(focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
					cameraParameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
				}else
					cameraParameters.setFocusMode(Parameters.FOCUS_MODE_FIXED);
			}
		}
		else
			mCamera.setDisplayOrientation(90);
		mCamera.setParameters(cameraParameters);

	}
	@Override
	public void onClick(View v) {
		//下一步
		if(v.getId() == R.id.recorder_next){
			if (isRecordingStarted) {
				rec = false;
				saveRecording();
			}else
				initiateRecording(false);
		}else if(v.getId() == R.id.recorder_flashlight){
			if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
				//showToast(this, "不能开启闪光灯");
				return;
			}
			//闪光灯
			if(isFlashOn){
				isFlashOn = false;
				flashIcon.setSelected(false);
				cameraParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			}
			else{
				isFlashOn = true;
				flashIcon.setSelected(true);
				cameraParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			}
			mCamera.setParameters(cameraParameters);
		}else if(v.getId() == R.id.recorder_frontcamera){
			//转换摄像头
			cameraSelection = ((cameraSelection == CameraInfo.CAMERA_FACING_BACK) ? CameraInfo.CAMERA_FACING_FRONT:CameraInfo.CAMERA_FACING_BACK);
			initCameraLayout();

			if(cameraSelection == CameraInfo.CAMERA_FACING_FRONT)
				flashIcon.setVisibility(View.GONE);
			else{
				flashIcon.setVisibility(View.VISIBLE);
				if(isFlashOn){
					cameraParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(cameraParameters);
				}
			}
		}else if(v.getId() == R.id.recorder_cancel){
			if (recording) 
				showCancellDialog();
			else
				videoTheEnd(false);
		}
	}

	
	/**
	 * 结束录制
	 * @param isSuccess
	 */
	public void videoTheEnd(boolean isSuccess)
	{
		releaseResources();
		if(fileVideoPath != null && fileVideoPath.exists() && !isSuccess)
			fileVideoPath.delete();
		
		returnToCaller(isSuccess);
	}
	
	/**
	 * 设置返回结果
	 * @param valid
	 */
	private void returnToCaller(boolean valid)
	{
		try{
			setActivityResult(valid);
			if(valid){
				Intent intent = new Intent(this,FFmpegPreviewActivity.class);
				intent.putExtra("path", strVideoPath);
				intent.putExtra("imagePath", imagePath);
				startActivity(intent);
			}
		} catch (Throwable e)
		{
		}finally{
			finish();
		}
	}
	
	private void setActivityResult(boolean valid)
	{
		Intent resultIntent = new Intent();
		int resultCode;
		if (valid)
		{
			resultCode = RESULT_OK;
			resultIntent.setData(uriVideoPath);
		} else
			resultCode = RESULT_CANCELED;
		
		setResult(resultCode, resultIntent);
	}

	/**
	 * 向系统注册我们录制的视频文件，这样文件才会在sd卡中显示
	 */
	private void registerVideo()
	{
		Uri videoTable = Uri.parse(CONSTANTS.VIDEO_CONTENT_URI);
		
		Util.videoContentValues.put(Video.Media.SIZE, new File(strVideoPath).length());
		try{
			uriVideoPath = getContentResolver().insert(videoTable, Util.videoContentValues);
		} catch (Throwable e){
			uriVideoPath = null;
			strVideoPath = null;
			e.printStackTrace();
		} finally{}
		Util.videoContentValues = null;
	}
	

	/**
	 * 保存录制的视频文件
	 */
	private void saveRecording()
	{
		if(isRecordingStarted){
			runAudioThread = false;
			if(!isRecordingSaved){
				isRecordingSaved = true;
				new AsyncStopRecording().execute();
			}
		}else{
			videoTheEnd(false);
		}
	}

	/**
	 * 求出录制的总时间
	
	private synchronized void setTotalVideoTime(){
		if(totalTime > 0)
			txtTimer.setText(Util.getRecordingTimeFromMillis(totalTime));
		
	} */
	
	/**
	 * 释放资源，停止录制视频和音频
	 */
	private void releaseResources(){
		isRecordingSaved = true;
		try {
			if(videoRecorder != null)
			{
			videoRecorder.stop();
			videoRecorder.release();
			}
		} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
			e.printStackTrace();
		}
		
		yuvIplImage = null;
		videoRecorder = null;
		lastSavedframe = null;
		
		//progressView.putProgressList((int) totalTime);
		//停止刷新进度
		progressView.setCurrentState(ProgressView.State.PAUSE);
	}
	
	/**
	 * 第一次按下时，初始化录制数据
	 * @param isActionDown
	 */
	private void initiateRecording(boolean isActionDown)
	{
		isRecordingStarted = true;
		firstTime = System.currentTimeMillis();
	
		recording = true;
		totalPauseTime = 0;
		pausedTime = 0;
		
		//txtTimer.setVisibility(View.VISIBLE);
		//handler.removeCallbacks(mUpdateTimeTask);
		//handler.postDelayed(mUpdateTimeTask, 100);
	}
	
	public static enum RecorderState {
		PRESS(1),LOOSEN(2),CHANGE(3),SUCCESS(4);
		
		static RecorderState mapIntToValue(final int stateInt) {
			for (RecorderState value : RecorderState.values()) {
				if (stateInt == value.getIntValue()) {
					return value;
				}
			}
			return PRESS;
		}

		private int mIntValue;

		RecorderState(int intValue) {
			mIntValue = intValue;
		}

		int getIntValue() {
			return mIntValue;
		}
	}
}
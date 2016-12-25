package com.jixianxueyuan.player.bar;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnPositionUpdateListener;
import com.baidu.cyberplayer.core.BVideoView.OnTotalCacheUpdateListener;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.activity.TopicDetailActivity.PlayerStatus;

/**
 * 简易播放条控件 该类依赖的资源文件有： drawable-xhdpi/seekbar_holo_light.xml
 * drawable-xhdpi/seekbar_ratio.png drawable-xhdpi/toggle_btn_pause.png
 * drawable-xhdpi/toggle_btn_play.png layout/media_controller_bar.xml
 * 
 * @author baidu
 *
 */
public class SimpleMediaController extends RelativeLayout
        implements OnPositionUpdateListener, OnTotalCacheUpdateListener {

    private static final String TAG = "SimpleMediaController";

    private boolean isPrepared = false;

    private ImageButton playButton;
    private ImageView switchScreenButton;
    private TextView positionView;
    private SeekBar seekBar;
    private TextView durationView;

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

    private BVideoView mVideoView = null;

    boolean mbIsDragging = false;

    private OnScreenBtnClickCallBack mOnScreenBtnClickCallBack;
    private boolean mIsFullScreen = false;

    public SimpleMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SimpleMediaController(Context context) {
        super(context);
        initUI();
    }

    public void setOnScreenBtnClickCallBack(OnScreenBtnClickCallBack clickCallBack){
        mOnScreenBtnClickCallBack = clickCallBack;
    }

    private void initUI() {

        // inflate controller bar
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.bar_simple_media_controller, this);

        playButton = (ImageButton) layout.findViewById(R.id.btn_play);
        playButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mVideoView == null) {
                    Log.d(TAG, "playButton checkstatus changed, but bindView=null");
                } else {
                    if (!isPrepared) {
                        Log.d(TAG, "playButton: Will invoke start()");
                        // playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                        mVideoView.start();
                        changeStatus(TopicDetailActivity.PlayerStatus.PLAYER_PREPARING);
                    } else {
                        if (mVideoView.isPlaying()) {
                            Log.d(TAG, "playButton: Will invoke pause()");
                            playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                            mVideoView.pause();
                        } else {
                            Log.d(TAG, "playButton: Will invoke resume()");
                            playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                            mVideoView.resume();
                        }
                    }
                }
            }

        });
        switchScreenButton = (ImageView) layout.findViewById(R.id.btn_switch_screen);
        switchScreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView == null){
                    Log.d(TAG, "playButton checkstatus changed, but bindView=null");
                }else {
                    if (mOnScreenBtnClickCallBack != null){
                        mIsFullScreen = !mIsFullScreen;
                        if (mIsFullScreen){
                            switchScreenButton.setImageResource(R.drawable.btn_to_mini);
                        }else {
                            switchScreenButton.setImageResource(R.drawable.btn_to_fullscreen);
                        }
                        mOnScreenBtnClickCallBack.onSwitch(mIsFullScreen);
                    }
                }
            }
        });

        positionView = (TextView) layout.findViewById(R.id.tv_position);
        seekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        seekBar.setMax(0);
        durationView = (TextView) layout.findViewById(R.id.tv_duration);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePostion(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mbIsDragging = true;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoView.getDuration() > 0) {
                    // 仅非直播的视频支持拖动
                    currentPositionInSeconds = seekBar.getProgress();
                    if (mVideoView != null) {
                        mVideoView.seekTo(seekBar.getProgress());
                    }

                    if (currentStatus == PlayerStatus.PLAYER_COMPLETED) {
                        mVideoView.start();
                        changeStatus(PlayerStatus.PLAYER_PREPARING);
                    }
                }
                mbIsDragging = false;
            }
        });
        enableControllerBar(false);
    }

    private PlayerStatus currentStatus = PlayerStatus.PLAYER_IDLE;

    /**
     * 
     * @param status
     */
    public void changeStatus(final PlayerStatus status) {
        Log.d(TAG, "mediaController: changeStatus=" + status.name());
        currentStatus = status;
        isMaxSetted = false;
        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                if (status == PlayerStatus.PLAYER_IDLE) {
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(false);
                    updatePostion(mVideoView == null ? 0 : mVideoView.getCurrentPosition());
                    updateDuration(mVideoView == null ? 0 : mVideoView.getDuration());
                    isPrepared = false;
                } else if (status == PlayerStatus.PLAYER_PREPARING) {
                    playButton.setEnabled(false);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(false);
                    isPrepared = false;
                } else if (status == PlayerStatus.PLAYER_PREPARED) {
                    isPrepared = true;
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                    seekBar.setEnabled(true);
                } else if (status == PlayerStatus.PLAYER_COMPLETED) {
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    isPrepared = false;
                }
            }

        });

    }

    /**
     * BVideoView implements VideoViewControl, so it's a BVideoView object
     * actually
     * 
     * @param player
     */
    public void setMediaPlayerControl(BVideoView player) {
        mVideoView = player;
        mVideoView.setOnPositionUpdateListener(this);
        mVideoView.setOnTotalCacheUpdateListener(this);
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        if (mVideoView == null) {
            return;
        }

        if (mVideoView.getDuration() > 0) {
            setProgress((int) currentPositionInSeconds);
        }

        this.setVisibility(View.VISIBLE);
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        this.setVisibility(View.GONE);
    }

    /**
     * @hide
     */
    public boolean getIsDragging() {
        return mbIsDragging;
    }

    private void updateDuration(int second) {
        if (durationView != null) {
            durationView.setText(formatSecond(second));
        }
    }

    private void updatePostion(int second) {
        if (positionView != null) {
            positionView.setText(formatSecond(second));
        }
    }

    private String formatSecond(int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }

    private boolean isMaxSetted = false;

    /**
     * Set the max process for the seekBar, usually the lasting seconds
     * 
     * @hide
     */
    public void setMax(int maxProgress) {
        if (isMaxSetted) {
            return;
        }
        if (seekBar != null) {
            seekBar.setMax(maxProgress);
        }
        updateDuration(maxProgress);
        if (maxProgress > 0) {
            isMaxSetted = true;
        }
    }

    /**
     * @hide
     * @param progress
     */
    public void setProgress(int progress) {
        if (seekBar != null) {
            seekBar.setProgress(progress);
        }
    }

    /**
     * @hide
     * @param progress
     */
    public void setCache(int cache) {
        if (seekBar != null && cache != seekBar.getSecondaryProgress()) {
            seekBar.setSecondaryProgress(cache);
        }   
    }

    /**
     * @hide
     * @param isEnable
     */
    public void enableControllerBar(boolean isEnable) {
        seekBar.setEnabled(isEnable);
        playButton.setEnabled(isEnable);
    }

    private long currentPositionInSeconds = 0L;

    /**
     * onPositionUpdate is invoked per 200ms
     */
    @Override
    public boolean onPositionUpdate(long newPositionInMilliSeconds) {
        long newPositionInSeconds = newPositionInMilliSeconds / 1000;
        long previousPosition = currentPositionInSeconds;
        if (newPositionInSeconds > 0 && !getIsDragging()) {
            currentPositionInSeconds = newPositionInSeconds;
        }
        if (getVisibility() != View.VISIBLE) {
            // 如果控制条不可见，则不设置进度
            return false;
        }
        if (!getIsDragging()) {
            int duration = mVideoView.getDuration();
            if (duration > 0) {
                this.setMax(duration);
                // 直播视频的duration为0，此时不设置进度
                if (newPositionInSeconds > 0 && previousPosition != newPositionInSeconds) {
                    this.setProgress((int) newPositionInSeconds);
                }
            }
        }
        return false;
    }

    @Override
    public void onTotalCacheUpdate(final long arg0) {
        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                setCache((int) arg0 + 10);
            }

        });
    }

    public interface OnScreenBtnClickCallBack{
        void onSwitch(boolean isFullScreen);
    }

}

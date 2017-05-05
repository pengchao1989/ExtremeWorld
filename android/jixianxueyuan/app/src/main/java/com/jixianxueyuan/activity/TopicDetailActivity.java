package com.jixianxueyuan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.LikeRecyclerAdapter;
import com.jixianxueyuan.adapter.TopicDetailListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.downloader.TopicDownloaderManager;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.ImageConfig;
import com.jixianxueyuan.config.MediaType;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.config.StaticResourceConfig;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.AgreeResultDTO;
import com.jixianxueyuan.dto.CollectionDTO;
import com.jixianxueyuan.dto.LikeDTO;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.TopicExtraDTO;
import com.jixianxueyuan.dto.TopicScoreDTO;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.dto.VideoExtraDTO;
import com.jixianxueyuan.dto.request.ReplyRequest;
import com.jixianxueyuan.dto.request.TopicScoreRequestDTO;
import com.jixianxueyuan.dto.request.ZanRequest;
import com.jixianxueyuan.fragment.MineFragment;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.BitmapUtils;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.ShareUtils;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.util.qiniu.QiniuMultiImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuMultiImageUploadListener;
import com.jixianxueyuan.widget.ClickLoadMoreView;
import com.jixianxueyuan.widget.KeyboardChangeListener;
import com.jixianxueyuan.widget.MediaController;
import com.jixianxueyuan.widget.MyActionBar;
import com.jixianxueyuan.widget.ReplyWidget;
import com.jixianxueyuan.widget.ReplyWidgetListener;
import com.jixianxueyuan.widget.RoundProgressBarWidthNumber;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.nekocode.badge.BadgeDrawable;
import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 5/22/15.
 */
public class TopicDetailActivity extends BaseActivity implements ReplyWidgetListener{

    public final static String tag = TopicDetailActivity.class.getSimpleName();
    public final static String INTENT_TOPIC = "topic";
    public final static String INTENT_TOPIC_ID = "topicId";

    @BindView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @BindView(R.id.topic_detail_actionbar)MyActionBar actionBar;
    @BindView(R.id.topic_detail_listview)ListView listView;
    @BindView(R.id.reply_widget_layout)LinearLayout replyWidgetLayout;
    @BindView(R.id.content_container)RelativeLayout contentLayout;

    //vidoe
    @BindView(R.id.video_cover_image)ImageView coverImageView;
    @BindView(R.id.video_play_btn)ImageView playButton;
    @BindView(R.id.videoview)PLVideoView videoView;
    @BindView(R.id.video_layout)RelativeLayout videoLayout;
    @BindView(R.id.short_video_detail_progress)
    RoundProgressBarWidthNumber roundProgressBarWidthNumber;
    @BindView(R.id.video_cache)
    ProgressBar mVideoCacheProgressView;


    @BindView(R.id.create_topic_upload_progress_layout)
    RelativeLayout progressLayout;
    @BindView(R.id.create_topic_upload_progress_view)
    ProgressBar uploadProgress;
    @BindView(R.id.create_topic_upload_progress_textview)
    TextView progressTextView;


    private long topicId = -1;
    private TopicDTO topicDTO;
    private TopicExtraDTO topicExtraDTO;

    private int currentPage = 0;
    private int totalPage = 0;
    private TopicDetailListAdapter adapter;


    private View headView;
    private HeadViewHolder headViewHolder;
    private ClickLoadMoreView loadMoreButton;

    private ReplyWidget replyWidget;
    private AlertDialog progressDialog;

    private DiskLruCache mDiskLruCache;

    private boolean interceptFlag = false;
    private int mProgressNum = 0;

    private float mCurrentMyRating = 0.0f;

    private ArrayList<String> imageUrlArrayList = new ArrayList<String>();

    private List<String> localImagePathList = null;
    LinkedHashMap<String,NativeUtil.CompressResult> localPathOfCompressInfoMap = null;
    private LinkedHashMap<String,String> serverImagePathMap = null;
    private boolean isUploadedImage = false;
    boolean isUploadedVideo = false;

    private String mReplyString = "";

    //video
    private MediaController mMediaController;

    //like
    LikeRecyclerAdapter likeRecyclerAdapter;


    final int HADLER_DOWNLOAD_VIDEO_SUCCESS = 0x1;
    final int HADLER_DOWNLOAD_VIDEO_FAILED = 0x2;
    final int HANDLER_DOWNLOAD_UPDATE = 0x3;
    final int HANDLER_INIT_CONTENT_SPANNED_SUCCESS = 0x4;
    final int HANDLER_INIT_CONTENT_SPANNED_FAILED = 0x5;
    public static final int HANDLER_UPDATE_PROGRESS = 0x7;
    public static final int HANDLER_PLAY_VIDEO = 0x8;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HADLER_DOWNLOAD_VIDEO_SUCCESS:

                    roundProgressBarWidthNumber.setVisibility(View.GONE);

                    String url = getGoodVideoSource(topicDTO);
                    String key = Util.stringToMD5(url);

                    playLocalVideo(DiskCachePath.getDiskCacheDir(TopicDetailActivity.this, "short_video").getPath() + "/" + key + ".0");
                    break;
                case HANDLER_DOWNLOAD_UPDATE:
                    roundProgressBarWidthNumber.setProgress(mProgressNum);
                    break;

                case HANDLER_INIT_CONTENT_SPANNED_SUCCESS:

                    Spanned spanned = (Spanned) msg.obj;
                    headViewHolder.contentTextView.setText(spanned);
                    break;

                case HANDLER_INIT_CONTENT_SPANNED_FAILED:
                    break;
                case HANDLER_UPDATE_PROGRESS:
                    Bundle progressData = msg.getData();
                    if (progressData.getInt("type") == 1){
                        progressTextView.setText("正在上传第" + progressData.getInt("index") + "张图片  " + String.format("%.1f",progressData.getDouble("percent") * 100) + "%")  ;
                    }else if (progressData.getInt("type") == 2){
                        progressTextView.setText("正在上传视频  " + String.format("%.1f",progressData.getDouble("percent") * 100) + "%")  ;
                    }else if (progressData.getInt("type") == 3){
                        progressTextView.setText("正在压缩图片第  " + progressData.getInt("index") + "张图片")  ;
                    }
                    break;
                case HANDLER_PLAY_VIDEO:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_detail_activity);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();

        Bundle bubdle = intent.getExtras();
        if(bubdle.containsKey(INTENT_TOPIC)){
            topicDTO = (TopicDTO) bubdle.getSerializable(INTENT_TOPIC);
        }else if(bubdle.containsKey(INTENT_TOPIC_ID)){
            topicId = bubdle.getLong(INTENT_TOPIC_ID);
        }

        initTopicHeadView();
        initFooterView();

        bottomSheetLayout.setPeekOnDismiss(true);
        adapter = new TopicDetailListAdapter(this);
        listView.setAdapter(adapter);

        replyWidget = new ReplyWidget(this, replyWidgetLayout);
        replyWidget.setReplyWidgetListener(this);
        actionBar.setActionOnClickListener(new MyActionBar.MyActionBarListener() {
            @Override
            public void onFirstActionClicked() {
                if (topicDTO != null) {
                    showShareMenu(topicDTO);
                }
            }

            @Override
            public void onSecondActionClicked() {
                if (topicDTO != null){
                    VideoDetailDTO videoDetailDTO = topicDTO.getVideoDetail();
                    if (videoDetailDTO == null){
                        return;
                    }
                    String videoUrl = getGoodVideoSource(topicDTO);
                    if (videoUrl != null){
                        TopicDownloaderManager.getInstance().downloadVideo(TopicDetailActivity.this, topicDTO.getTitle(), videoUrl);
                    }
                    MobclickAgent.onEvent(TopicDetailActivity.this, UmengEventId.TOPIC_DETAIL_DOWNLOAD_CLICK);
                }
            }
        });

        if(topicDTO != null){
            refreshHeadView();
            requestReplyList();
            requestTopicExtra(topicDTO.getId());
        }else if(topicId != -1){
            requestTopicDetail();
            requestTopicExtra(topicId);
        }else {
            Toast.makeText(this,getString(R.string.err), Toast.LENGTH_SHORT).show();
            finish();
        }

        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                MyLog.d(tag, "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
                replyWidget.onKeyboardChange(isShow);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.v(tag, "onPause");
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (videoView != null){
            //TODO pause video
            videoView.pause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.v(tag, "onResume");

        if (videoView != null){
            if (!videoView.isPlaying()) {
                //TODO resume
                videoView.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyLog.v(tag, "onStop");
        //TODO 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
        if (videoView != null &&videoView.isPlaying() ) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (headViewHolder.webView != null){
            headViewHolder.webView.destroy();
        }

        //TODO stop
        if (videoView != null){
            videoView.stopPlayback();
        }

        MyLog.v(tag, "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        replyWidget.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void initTopicHeadView()
    {
        headView = LayoutInflater.from(this).inflate(R.layout.topic_detail_head_view, null);
        headViewHolder = new HeadViewHolder(headView);
        listView.addHeaderView(headView);
    }

    private void initFooterView()
    {

        loadMoreButton = new ClickLoadMoreView(this);
        loadMoreButton.setVisibility(View.GONE);
        loadMoreButton.setClickLoadMoreViewListener(new ClickLoadMoreView.ClickLoadMoreViewListener() {
            @Override
            public void runLoad() {
                getNextPage();
            }
        });
        listView.addFooterView(loadMoreButton);
    }

    private void refreshHeadView(){

        headViewHolder.collectionButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                submitCollection();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        //like
        headViewHolder.likeCountTextView.setText(topicDTO.getAgreeCount() + getString(R.string.agree_of_me));

        //score
        if (TopicType.CHALLENGE.equals(topicDTO.getType())){
            actionBar.setTitle(getString(R.string.challenge));
            headViewHolder.ratingLayout.setVisibility(View.VISIBLE);
            headViewHolder.ratingBar.setRating((float) topicDTO.getScore() / 2.0f);
            headViewHolder.ratingValue.setText(String.format("%.1f", topicDTO.getScore()));
            String ratingCountText = getString(R.string.count_score);
            ratingCountText = String.format(ratingCountText, topicDTO.getScoreCount());
            headViewHolder.ratingCount.setText(ratingCountText);
            headViewHolder.ratingLayout.setOnClickListener(null);
            headViewHolder.myRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser){
                        mCurrentMyRating = rating*2;
                        headViewHolder.mySubmitRatingText.setVisibility(View.GONE);
                        headViewHolder.mySubmitRatingButton.setVisibility(View.VISIBLE);
                        headViewHolder.myRatingText.setText(String.format("%.1f", mCurrentMyRating));
                    }
                }
            });

            headViewHolder.mySubmitRatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitMyTopicScore(mCurrentMyRating);
                }
            });
        }

        //course label
        if (topicDTO.getCourse() != null){
            final BadgeDrawable drawable =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                            .badgeColor(0xffCC9933)
                            .text1("动作")
                            .text2(topicDTO.getCourse().getName())
                            .build();
            SpannableString spannableString =
                    new SpannableString(TextUtils.concat(
                            drawable.toSpannable()
                    ));
            headViewHolder.scoreNameTextView.setText(spannableString);
            headViewHolder.scoreNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseDetailActivity.startActivity(TopicDetailActivity.this, topicDTO.getCourse());
                }
            });
        }

        //web view
        if(!StringUtils.isBlank(topicDTO.getUrl())){
            headViewHolder.webView.setVisibility(View.VISIBLE);

            initWebViewSetting(headViewHolder.webView);

            if(!StringUtils.isBlank(topicDTO.getUrl())){
                headViewHolder.webView.loadUrl(topicDTO.getUrl());
                CookieSyncManager.createInstance(this);
                CookieSyncManager.getInstance().sync();
            }else {
                Toast.makeText(this, "url is null", Toast.LENGTH_LONG).show();
            }

            headViewHolder.mUserHeadLayout.setVisibility(View.GONE);
            headViewHolder.titleTextView.setVisibility(View.GONE);
            return;
        }


        headViewHolder.titleTextView.setText(topicDTO.getTitle());
        headViewHolder.nameTextView.setText(topicDTO.getUser().getName());
        String timeAgo = DateTimeFormatter.getTimeAgo(this, topicDTO.getCreateTime());
        headViewHolder.timeTextView.setText(timeAgo);


        String avatarUrl = topicDTO.getUser().getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }
        headViewHolder.avatarImageView.setImageURI(ImageUriParseUtil.parse(avatarUrl));
        headViewHolder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomeActivity.startActivity(TopicDetailActivity.this, topicDTO.getUser());
            }
        });


        //参考链接
        // http://www.ibm.com/developerworks/cn/web/1407_zhangqian_androidhtml/#_清单 4._fromHtml()方法定义
        //http://www.jb51.net/article/46799.htm
/*        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Spanned spanned = Html.fromHtml(topicDTO.getContent(), new MImageGetter(TopicDetailActivity.this.headViewHolder.contentTextView, TopicDetailActivity.this), new MTagHandler());

                Message msg = new Message();
                msg.what = HANDLER_INIT_CONTENT_SPANNED_SUCCESS;
                msg.obj = spanned;
                handler.sendMessage(msg);
            }
        });
        t.start();*/



/*
        List<AnalyzeContent.ContentFragment> contentFragmentList = new LinkedList<AnalyzeContent.ContentFragment>();
        contentFragmentList = AnalyzeContent.analyzeContent2(topicDTO.getContent());

        for(int n=0; n != contentFragmentList.size() ; n++ )
        {
            if(contentFragmentList.get(n).mType == AnalyzeContent.ContentFragment.IMG_URL_TYPE)
            {
                ImageView imageviwe = new ImageView(this);
                imageviwe.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageviwe.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageviwe.setPadding(0,4,0,4);

                headViewHolder.replyWidgetLayout.addView(imageviwe);

                ImageLoader.getInstance().displayImage(contentFragmentList.get(n).mText + "!AndroidDetail", imageviwe);
            }
            else if(contentFragmentList.get(n).mType == AnalyzeContent.ContentFragment.TEXT_TYPE)
            {
                EmojiconTextView textView = new EmojiconTextView(this);
                textView.setTextSize(20);
                textView.setEmojiconSize(48);
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                headViewHolder.replyWidgetLayout.addView(textView);
                String temp = contentFragmentList.get(n).mText ;
                textView.setText(Html.fromHtml(temp));
            }

        }
*/

        //content
/*        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        headViewHolder.replyWidgetLayout.addView(textView);
        textView.setText(topicDTO.getContent());*/
        headViewHolder.contentTextView.setText(topicDTO.getContent());

        //image
        final MediaWrapDTO mediaWrapDTO = topicDTO.getMediaWrap();
        if(mediaWrapDTO != null){
            for(MediaDTO mediaDTO: mediaWrapDTO.getMedias()){

                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setFadeDuration(300)
                        .setPlaceholderImage(R.mipmap.photo)
                        .setBackground(getResources().getDrawable(R.drawable.photo_frame))
                        .build();

                SimpleDraweeView imageviwe = new SimpleDraweeView(this);
                imageviwe.setHierarchy(hierarchy);
                if (mediaDTO.getWidth() <= 0 || mediaDTO.getHeight() <= 0){
                    imageviwe.setLayoutParams(new ViewGroup.LayoutParams(ImageConfig.DETAIL_IMAGE_DEFAULT_WIDHT,ImageConfig.DETAIL_IMAGE_DEFAULT_HEIGHT));
                }else if(mediaDTO.getWidth() < ImageConfig.DETAIL_IMAGE_DEFAULT_WIDHT){
                    imageviwe.setLayoutParams(new ViewGroup.LayoutParams(mediaDTO.getWidth(), mediaDTO.getHeight()));
                }
                else {
                    int width = ImageConfig.DETAIL_IMAGE_DEFAULT_WIDHT;
                    int height = (int) (((float)ImageConfig.DETAIL_IMAGE_DEFAULT_WIDHT / mediaDTO.getWidth()) * mediaDTO.getHeight());
                    imageviwe.setLayoutParams(new ViewGroup.LayoutParams(width,height));
                }

                imageviwe.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageviwe.setPadding(0, 8, 0, 8);

                headViewHolder.contentLayout.addView(imageviwe);

                imageviwe.setImageURI(ImageUriParseUtil.parse(mediaDTO.getPath() + QiniuImageStyle.DETAIL));

                imageviwe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageGalleryActivity.startActivity(TopicDetailActivity.this, topicDTO.getTitle(), imageUrlArrayList);
                    }
                });
                imageUrlArrayList.add(mediaDTO.getPath() + QiniuImageStyle.DETAIL);
            }
        }


        //video
        if (topicDTO.getType() == TopicType.VIDEO || topicDTO.getVideoDetail() != null)
        {
            if(getGoodVideoSource(topicDTO) != null)
            {
                videoLayout.setVisibility(View.VISIBLE);
                coverImageView.setImageURI(ImageUriParseUtil.parse(topicDTO.getVideoDetail().getThumbnail() + "!detail"));
                videoView.setCoverView(coverImageView);

                // You can also use a custom `MediaController` widget
                mMediaController = new MediaController(this, false, false);
                videoView.setMediaController(mMediaController);

                videoView.setBufferingIndicator(mVideoCacheProgressView);



                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoCacheProgressView.setVisibility(View.VISIBLE);
                        playVideo();
                    }
                });

                videoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            actionBar.showSecondAction();
        }
    }

    private void initWebViewSetting(WebView webView){
        com.tencent.smtt.sdk.WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(com.tencent.smtt.sdk.WebSettings.RenderPriority.HIGH);
    }

    private void updateExtraInfoView(){
        if (topicExtraDTO != null){
            if (topicExtraDTO.isAgreed()){
                replyWidget.setLiked(true);
            }
            if (topicExtraDTO.isCollected()){
                headViewHolder.collectionButton.setLiked(true);
                headViewHolder.collectionButton.setEnabled(false);
            }
            if (topicExtraDTO.getMyMarkScore() > 0){
                headViewHolder.myRatingLayout.setVisibility(View.GONE);
            }else {
                headViewHolder.myRatingLayout.setVisibility(View.VISIBLE);
            }

            if (topicExtraDTO.getLikeList() != null && topicExtraDTO.getLikeList().size() > 0){
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                headViewHolder.likeRecyclerView.setLayoutManager(linearLayoutManager);
                likeRecyclerAdapter = new LikeRecyclerAdapter(TopicDetailActivity.this, topicExtraDTO.getLikeList());
                headViewHolder.likeRecyclerView.setAdapter(likeRecyclerAdapter);
            }

        }
    }

    private void playVideo()
    {
        if(!topicDTO.getType().equals(TopicType.S_VIDEO)){
            playWebVideo(getGoodVideoSource(topicDTO));
        }else {
            openDiskLruCache();
            String key = Util.stringToMD5(getGoodVideoSource(topicDTO));
            try {

                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);

                if(snapShot != null) {
                    playLocalVideo(DiskCachePath.getDiskCacheDir(this, "short_video").getPath() + "/" + key + ".0");
                }
                else {
                    roundProgressBarWidthNumber.setVisibility(View.VISIBLE);
                    downloadVideoFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doHideFootView()
    {
        if(totalPage > 1)
        {
            if(loadMoreButton.isLoading() == true)
            {
                loadMoreButton.onFinish();
            }

            if(currentPage >= totalPage)
            {
                loadMoreButton.setOver();
            }
        }
    }


    private void getNextPage()
    {
        if(currentPage < totalPage)
        {
            requestReplyList();
        }
        else
        {
            Toast.makeText(this,R.string.not_more, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isLastPage()
    {
        if(currentPage >= totalPage)
        {
            return true;
        }

        return false;
    }

    private void requestTopicDetail(){

        String url = ServerMethod.topic() + "/" + topicId;
        MyRequest<TopicDTO> myRequest = new MyRequest<TopicDTO>(Request.Method.GET, url, TopicDTO.class,
                new Response.Listener<MyResponse<TopicDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicDTO> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            topicDTO = response.getContent();
                            refreshHeadView();
                            requestReplyList();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestTopicExtra(long topicId){
        String url = ServerMethod.topic_extra()  + topicId;
        MyRequest<TopicExtraDTO> myRequest = new MyRequest<TopicExtraDTO>(Request.Method.GET, url, TopicExtraDTO.class,
                new Response.Listener<MyResponse<TopicExtraDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicExtraDTO> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            topicExtraDTO = response.getContent();
                            updateExtraInfoView();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestReplyList()
    {
        String url = ServerMethod.reply() + "?topicId="+ topicDTO.getId() + "&page=" + (currentPage + 1) ;

        MyPageRequest<ReplyDTO> myPageRequest = new MyPageRequest<ReplyDTO>(Request.Method.GET,url,ReplyDTO.class,
                new Response.Listener<MyResponse<MyPage<ReplyDTO>>>()
                {
                    @Override
                    public void onResponse(MyResponse<MyPage<ReplyDTO>> response) {

                        MyLog.d(tag,"response=" + response);

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            MyPage<ReplyDTO> page = response.getContent();
                            List<ReplyDTO> topicDTOs = page.getContents();
                            adapter.addNextPageData(topicDTOs);

                            totalPage = page.getTotalPages();
                            currentPage++;

                            doHideFootView();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void submitReply(String replyContent)
    {

        showProgress();
        //隐藏键盘
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reply();

        ReplyRequest replyRequest = buildReplyDTO(replyContent);


        MyRequest<ReplyDTO> stringRequest = new MyRequest<ReplyDTO>(Request.Method.POST,url,ReplyDTO.class, replyRequest,
                new Response.Listener<MyResponse<ReplyDTO>>() {
                    @Override
                    public void onResponse(MyResponse<ReplyDTO> response) {
                        MyLog.d(tag, "onResponse=" + response.toString());

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            //若是在最后一页则 成功后将回复更新到view上
                            if(isLastPage())
                            {
                                ReplyDTO replyDTO = response.getContent();
                                UserDTO mine = MyApplication.getContext().getMine().getUserInfo();
                                replyDTO.getUser().setName(mine.getName());
                                replyDTO.getUser().setAvatar(mine.getAvatar());
                                replyDTO.getUser().setGender(mine.getGender());
                                replyDTO.getUser().setId(mine.getId());
                                adapter.addNew(replyDTO);
                            }

                            replyWidget.clean();
                            Toast.makeText(TopicDetailActivity.this, R.string.reply_success,Toast.LENGTH_LONG).show();

                            hideProgress();

                            //更新reply到UI


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLog.d(tag, "onErrorResponse" + error.toString());
                        hideProgress();
                    }
                });

        queue.add(stringRequest);
    }

    private ReplyRequest buildReplyDTO(String replyString)
    {
        ReplyRequest replyDTO = new ReplyRequest();
        replyDTO.setContent(replyString);

        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(MyApplication.getContext().getMine().getUserInfo().getId());
        replyDTO.setUser(userMinDTO);

        TopicDTO topic = new TopicDTO();
        topic.setId(topicDTO.getId());
        replyDTO.setTopic(topic);

        //image
        MediaWrapDTO mediaWrapDTO = new MediaWrapDTO();
        List<MediaDTO> mediaDTOList = new ArrayList<MediaDTO>();
        if(serverImagePathMap != null){
            Iterator iter = serverImagePathMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();

                String key = (String) entry.getKey();
                String url = StaticResourceConfig.IMG_DOMAIN + (String) entry.getValue();
                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setType(MediaType.IMAGE);
                mediaDTO.setPath(url);

                //size
                NativeUtil.CompressResult compressResult = localPathOfCompressInfoMap.get(key);
                if (compressResult != null){
                    mediaDTO.setWidth(compressResult.width);
                    mediaDTO.setHeight(compressResult.height);
                }

                mediaDTOList.add(mediaDTO);
            }
            mediaWrapDTO.setMedias(mediaDTOList);
            replyDTO.setMediaWrap(mediaWrapDTO);
        }

        return replyDTO;
    }

    private boolean submitZan()
    {
        String url = ServerMethod.zan();

        ZanRequest zanRequest = new ZanRequest();
        zanRequest.setTopicId(topicDTO.getId());
        zanRequest.setUserId(MyApplication.getContext().getMine().getUserInfo().getId());


        MyRequest<AgreeResultDTO> myRequest = new MyRequest(Request.Method.POST, url, AgreeResultDTO.class,zanRequest, new Response.Listener<MyResponse<AgreeResultDTO>>() {
            @Override
            public void onResponse(MyResponse<AgreeResultDTO> response) {
                replyWidget.setLiked(true);
                headViewHolder.likeCountTextView.setText(response.getContent().getCount() + getString(R.string.agree_of_me));
                //add to like list
                if (likeRecyclerAdapter != null){
                    LikeDTO likeDTO = new LikeDTO();
                    UserMinDTO userMinDTO = MyApplication.getContext().getMine().getUserMinInfo();
                    likeDTO.setUser(userMinDTO);
                    likeRecyclerAdapter.addToHead(likeDTO);
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.d(tag, "onErrorResponse" + error.toString());
            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
        return false;
    }

    private void submitCollection(){
        String url = ServerMethod.collection() + "/" + topicDTO.getId();
        MyRequest<CollectionDTO> myRequest = new MyRequest<CollectionDTO>(Request.Method.POST, url, CollectionDTO.class, null, new Response.Listener<MyResponse<CollectionDTO>>() {
            @Override
            public void onResponse(MyResponse<CollectionDTO> response) {
                if(response.getStatus() == MyResponse.status_ok){
                    headViewHolder.collectionButton.setEnabled(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void submitMyTopicScore(double score){

        TopicScoreRequestDTO topicScoreRequestDTO = new TopicScoreRequestDTO();
        topicScoreRequestDTO.setTopicId(topicDTO.getId());
        topicScoreRequestDTO.setScore(score);

        String url = ServerMethod.topic_score();
        MyRequest<TopicScoreDTO> myRequest = new MyRequest<TopicScoreDTO>(Request.Method.POST, url, TopicScoreDTO.class, topicScoreRequestDTO, new Response.Listener<MyResponse<TopicScoreDTO>>() {
            @Override
            public void onResponse(MyResponse<TopicScoreDTO> response) {
                if (headViewHolder != null){
                    headViewHolder.myRatingLayout.setVisibility(View.GONE);
                    headViewHolder.ratingBar.setRating((float) (response.getContent().getTopicAvgScore() / 2.0f));
                    headViewHolder.ratingValue.setText(String.format("%.1f", response.getContent().getTopicAvgScore()));
                    String ratingCountText = getString(R.string.count_score);
                    ratingCountText = String.format(ratingCountText, response.getContent().getTopicTotalScoreCount());
                    headViewHolder.ratingCount.setText(ratingCountText);
                    Toast.makeText(TopicDetailActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void uploadImage()
    {
        showUploadProgressView();

        //压缩图片
        localImagePathList = compressImage(localImagePathList);


        QiniuMultiImageUpload qiNiuPictureUpload = new QiniuMultiImageUpload(this);
        qiNiuPictureUpload.upload(localImagePathList, new QiniuMultiImageUploadListener() {

            @Override
            public void onUploading(int index, String key, double percent) {
                updateProgressView(1, index, percent);
            }

            @Override
            public void onUploadFailed() {
                hideUploadProgressView();
            }

            @Override
            public void onUploadComplete(LinkedHashMap<String, String> result) {
                hideUploadProgressView();
                serverImagePathMap = result;
                if (serverImagePathMap != null) {
                    isUploadedImage = true;

                    submitReply(mReplyString);


                } else {

                }
            }

            @Override
            public void onUploadCancelled() {
                hideUploadProgressView();
            }
        });
    }

    private List<String> compressImage(List<String> filePathList){
        localPathOfCompressInfoMap = new LinkedHashMap<String, NativeUtil.CompressResult>();
        List<String> compressImageFilePath = new ArrayList<String>();
        int index = 0;
        for (String filePath : filePathList){
            updateProgressView(3, index, 0.0);
            File saveFile = new File(DiskCachePath.getDiskCacheDir(TopicDetailActivity.this, "compressCache"), "compress_" + System.currentTimeMillis() + ".jpg");
            Bitmap bitmap = BitmapUtils.getBitmap(filePath);
            NativeUtil.CompressResult compressResult = NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
            compressImageFilePath.add(saveFile.getAbsolutePath());
            localPathOfCompressInfoMap.put(saveFile.getAbsolutePath(), compressResult);
            index++;
        }
        return compressImageFilePath;
    }

    private void showProgress(){
        if (progressDialog == null){
            progressDialog = new SpotsDialog(this,R.style.ProgressDialogWait);
        }
        progressDialog.show();
    }

    private void hideProgress(){
        progressDialog.dismiss();
    }

    private void showUploadProgressView(){
        progressLayout.setVisibility(View.VISIBLE);

    }

    private void hideUploadProgressView(){
        progressLayout.setVisibility(View.GONE);
    }

    private void updateProgressView(int type, int index, double percent){

        Message msg = new Message();
        msg.what = HANDLER_UPDATE_PROGRESS;
        Bundle progressData = new Bundle();
        progressData.putInt("type", type);
        progressData.putInt("index", index+1);
        progressData.putDouble("percent", percent);
        msg.setData(progressData);
        handler.sendMessage(msg);

    }


    @Override
    public void onCommit(String text) {
        if (!TextUtils.isEmpty(text) || (localImagePathList != null && localImagePathList.size() > 0)){
            mReplyString = text;
            if (localImagePathList != null && localImagePathList.size() > 0){
                uploadImage();
            }else {
                submitReply(text);
            }
        }
    }

    @Override
    public void onImageChange(List<String> imagePath) {
        localImagePathList = imagePath;
    }

    @Override
    public void onLikeClicked() {
        //点赞
        if (replyWidget.isLiked()){
            Toast.makeText(TopicDetailActivity.this, "you liked it!!!", Toast.LENGTH_LONG).show();
        }else {
            submitZan();
        }
    }

    public static class HeadViewHolder
    {
        @BindView(R.id.user_info_head_layout)RelativeLayout mUserHeadLayout;
        @BindView(R.id.topic_detail_title)TextView titleTextView;
        @BindView(R.id.user_head_name)TextView nameTextView;
        @BindView(R.id.user_head_time)TextView timeTextView;
        @BindView(R.id.user_head_avatar)SimpleDraweeView avatarImageView;
        @BindView(R.id.web_view)WebView webView;
        @BindView(R.id.topic_detail_head_course_name)TextView scoreNameTextView;
        @BindView(R.id.topic_detail_content_textview)TextView contentTextView;
        @BindView(R.id.topic_detail_content_container)LinearLayout contentLayout;
        @BindView(R.id.topic_detail_collection_button)LikeButton collectionButton;
        @BindView(R.id.rating_layout)LinearLayout ratingLayout;
        @BindView(R.id.ratingBar)RatingBar ratingBar;
        @BindView(R.id.rating_value)TextView ratingValue;
        @BindView(R.id.rating_count)TextView ratingCount;
        @BindView(R.id.my_rating_layout)RelativeLayout myRatingLayout;
        @BindView(R.id.my_ratingBar)RatingBar myRatingBar;
        @BindView(R.id.my_submit_rating_text)TextView mySubmitRatingText;
        @BindView(R.id.my_submit_rating_button)Button mySubmitRatingButton;
        @BindView(R.id.my_rating_score_text)TextView myRatingText;
        @BindView(R.id.like_recycler_view)RecyclerView likeRecyclerView;
        @BindView(R.id.like_count_text)TextView likeCountTextView;


        public HeadViewHolder(View headView)
        {
            ButterKnife.bind(this, headView);
        }
    }


    //video
    private void playLocalVideo(String path)
    {
        MyLog.d(tag, "videoPath=" + path);

        playButton.setVisibility(View.GONE);
        coverImageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

        videoView.setVideoPath(path);
        videoView.start();
    }

    private void playWebVideo(String url){


        if (!TextUtils.isEmpty(url)){
            playButton.setVisibility(View.GONE);
            coverImageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            videoView.setVideoPath(url);
            videoView.start();
        }else {
            Toast.makeText(this, R.string.video_is_empty, Toast.LENGTH_LONG).show();
        }

    }


    private void downloadVideoFile() {

        Thread thread = new Thread(new DownloadRunnable(getGoodVideoSource(topicDTO)));
        thread.start();

    }

    private void openDiskLruCache()
    {
        try {
            File cacheDir = DiskCachePath.getDiskCacheDir(this, "short_video");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(this), 1, 30 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class DownloadRunnable implements Runnable
    {
        private String url;

        public DownloadRunnable(String url)
        {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                String key = Util.stringToMD5(this.url);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null)
                {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (downloadUrlToStream(this.url, outputStream))
                    {
                        editor.commit();

                    } else
                    {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();

                handler.sendEmptyMessage(HADLER_DOWNLOAD_VIDEO_SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;

        InputStream is = null;

        try
        {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            is = urlConnection.getInputStream();

            int length = urlConnection.getContentLength();
            int count = 0, oldProgressNum = 0;
            byte buf[] = new byte[1024];
            do
            {
                int numread = is.read(buf);
                count += numread;
                mProgressNum = (int) (((float) count / length) * 100);
                if(mProgressNum  > oldProgressNum)
                {
                    oldProgressNum = mProgressNum;
                    handler.sendEmptyMessage(HANDLER_DOWNLOAD_UPDATE);
                }

                if (numread <= 0) {
                    break;
                }
                outputStream.write(buf,0, numread);

            }while (!interceptFlag);

            return true;
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {

                if (is != null) {
                    is.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @OnItemClick(R.id.topic_detail_listview)void onReplyItemClicked(int position){
        if (position == 0){
            return;
        }
        Intent intent = new Intent(TopicDetailActivity.this, ReplyDetailActivity.class);
        ReplyDTO replyDTO = (ReplyDTO) adapter.getItem(position -1);
        intent.putExtra("reply", replyDTO);
        startActivity(intent);
    }

    //share

    private void showShareMenu(final TopicDTO topicDTO){

        final String hobby = Util.getApplicationMetaString(this, "HOBBY");


        VideoDetailDTO videoDetailDTO = topicDTO.getVideoDetail();
        String imageUrl = "";
        if (videoDetailDTO != null){
            imageUrl = videoDetailDTO.getThumbnail() + QiniuImageStyle.LIST_ITEM;
        }else {
            imageUrl = HobbyType.getHobbyLogoUrl(hobby);
        }

        final UMImage image = new UMImage(TopicDetailActivity.this, imageUrl);



        MenuSheetView menuSheetView =
                new MenuSheetView(this, MenuSheetView.MenuType.GRID, "分享给好友...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String url = "http://www.jixianxueyuan.com/" + hobby + "/topic/" + topicDTO.getId();
                        if (!TextUtils.isEmpty(topicDTO.getUrl())){
                            url = topicDTO.getUrl();
                        }

                        ShareUtils.ShareItem shareItem = null;
                        UMVideo video = null;
                        if (topicDTO.getVideoDetail() != null && !TextUtils.isEmpty(getGoodVideoSource(topicDTO))){
                            video = new UMVideo(url);
                            video.setThumb(new UMImage(TopicDetailActivity.this,topicDTO.getVideoDetail().getThumbnail()));
                        }


                        switch (item.getItemId()){
                            case R.id.menu_share_qq:
                                new ShareAction(TopicDetailActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                        .withText(topicDTO.getTitle())
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .withMedia(video)
                                        .share();
                                break;
                            case R.id.menu_share_kongjian:
                                new ShareAction(TopicDetailActivity.this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                                        .withTitle(topicDTO.getTitle())
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .withMedia(video)
                                        .share();

                                break;
                            case R.id.menu_share_weibo:
                                new ShareAction(TopicDetailActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                                        .withText(topicDTO.getTitle())
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .share();
                                break;
                            case R.id.menu_share_weixin:
                                new ShareAction(TopicDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                        .withTitle(topicDTO.getTitle())
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .withMedia(video)
                                        .share();
                                break;
                            case R.id.menu_share_weixin_circle:
                                new ShareAction(TopicDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                        .withTitle(topicDTO.getTitle())
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .withMedia(video)
                                        .share();
                                break;
                        }


                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.share);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(TopicDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TopicDetailActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(TopicDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public String getGoodVideoSource(TopicDTO topicDTO){
        if (topicDTO == null){
            return "";
        }
        List<VideoExtraDTO> videoExtraDTOList = topicDTO.getVideoDetail().getVideoExtraList();
        if (videoExtraDTOList != null && videoExtraDTOList.size() > 0){
            for (VideoExtraDTO videoExtraDTO : videoExtraDTOList){
                if ("high".equals(videoExtraDTO.getType())){
                    return videoExtraDTO.getSrc();
                }
            }
        }

        return topicDTO.getVideoDetail().getVideoSource();

    }
}

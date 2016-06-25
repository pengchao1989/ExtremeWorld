package com.jixianxueyuan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.TopicDetailListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.AgreeResultDTO;
import com.jixianxueyuan.dto.CollectionDTO;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.TopicExtraDTO;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.dto.request.ReplyRequest;
import com.jixianxueyuan.dto.request.ZanRequest;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.ShareUtils;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.AdvancedWebView;
import com.jixianxueyuan.widget.ClickLoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;
import com.jixianxueyuan.widget.ReplyWidget;
import com.jixianxueyuan.widget.ReplyWidgetListener;
import com.jixianxueyuan.widget.RoundProgressBarWidthNumber;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 5/22/15.
 */
public class TopicDetailActivity extends BaseActivity implements ReplyWidgetListener, AdvancedWebView.Listener {

    public final static String tag = TopicDetailActivity.class.getSimpleName();
    public final static String INTENT_TOPIC = "topic";
    public final static String INTENT_TOPIC_ID = "INTENT_TOPIC_ID";

    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.topic_detail_actionbar)MyActionBar actionBar;
    @InjectView(R.id.topic_detail_listview)ListView listView;
    @InjectView(R.id.reply_widget_layout)LinearLayout contentLayout;


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

    private ArrayList<String> imageUrlArrayList = new ArrayList<String>();


    final int HADLER_DOWNLOAD_VIDEO_SUCCESS = 0x1;
    final int HADLER_DOWNLOAD_VIDEO_FAILED = 0x2;
    final int HANDLER_DOWNLOAD_UPDATE = 0x3;
    final int HANDLER_INIT_CONTENT_SPANNED_SUCCESS = 0x4;
    final int HANDLER_INIT_CONTENT_SPANNED_FAILED = 0x5;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HADLER_DOWNLOAD_VIDEO_SUCCESS:

                    headViewHolder.roundProgressBarWidthNumber.setVisibility(View.GONE);

                    String url = topicDTO.getVideoDetail().getVideoSource()/*"http://7u2nc3.com1.z0.glb.clouddn.com/short_videofm11QHWk09-1CaKh6JpN-A__.mp4"*/;

                    String key = Util.stringToMD5(url);

                    playLocalVideo(DiskCachePath.getDiskCacheDir(TopicDetailActivity.this, "short_video").getPath() + "/" + key + ".0");
                    break;
                case HANDLER_DOWNLOAD_UPDATE:
                    headViewHolder.roundProgressBarWidthNumber.setProgress(mProgressNum);
                    break;

                case HANDLER_INIT_CONTENT_SPANNED_SUCCESS:

                    Spanned spanned = (Spanned) msg.obj;
                    headViewHolder.contentTextView.setText(spanned);
                    break;

                case HANDLER_INIT_CONTENT_SPANNED_FAILED:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_detail_activity);

        ButterKnife.inject(this);

        Intent intent = this.getIntent();

        Bundle bubdle = intent.getExtras();
        if(bubdle.containsKey("topic")){
            topicDTO = (TopicDTO) bubdle.getSerializable(INTENT_TOPIC);
        }else if(bubdle.containsKey("topicId")){
            topicId = bubdle.getLong("topicId");
        }

        initTopicHeadView();
        initFooterView();

        bottomSheetLayout.setPeekOnDismiss(true);
        adapter = new TopicDetailListAdapter(this);
        listView.setAdapter(adapter);

        replyWidget = new ReplyWidget(this, contentLayout);
        replyWidget.setReplyWidgetListener(this);

        actionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topicDTO != null) {
                    showShareMenu(topicDTO);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (headViewHolder.webView != null){
            headViewHolder.webView.destroy();
        }
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

        headViewHolder.zanButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //点赞
                submitZan();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });


        headViewHolder.zanCountTextView.setText(String.valueOf(topicDTO.getAgreeCount()));

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
        }


        //web view
        if(!StringUtils.isBlank(topicDTO.getUrl())){
            headViewHolder.webView.setVisibility(View.VISIBLE);
            headViewHolder.webView.loadUrl(topicDTO.getUrl());
            headViewHolder.webView.setListener(this, this);
            headViewHolder.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
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
        ImageLoader.getInstance().displayImage(avatarUrl, headViewHolder.avatarImageView);


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

                headViewHolder.contentLayout.addView(imageviwe);

                ImageLoader.getInstance().displayImage(contentFragmentList.get(n).mText + "!AndroidDetail", imageviwe);
            }
            else if(contentFragmentList.get(n).mType == AnalyzeContent.ContentFragment.TEXT_TYPE)
            {
                EmojiconTextView textView = new EmojiconTextView(this);
                textView.setTextSize(20);
                textView.setEmojiconSize(48);
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                headViewHolder.contentLayout.addView(textView);
                String temp = contentFragmentList.get(n).mText ;
                textView.setText(Html.fromHtml(temp));
            }

        }
*/

        //content
/*        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        headViewHolder.contentLayout.addView(textView);
        textView.setText(topicDTO.getContent());*/
        headViewHolder.contentTextView.setText(topicDTO.getContent());

        //image
        final MediaWrapDTO mediaWrapDTO = topicDTO.getMediaWrap();
        if(mediaWrapDTO != null){
            for(MediaDTO mediaDTO: mediaWrapDTO.getMedias()){
                ImageView imageviwe = new ImageView(this);
                imageviwe.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageviwe.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageviwe.setPadding(0, 8, 0, 8);
                headViewHolder.contentLayout.addView(imageviwe);
                ImageLoader.getInstance().displayImage(mediaDTO.getPath() + QiniuImageStyle.DETAIL, imageviwe, ImageLoaderConfig.getImageOption(TopicDetailActivity.this));
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
            if(topicDTO.getVideoDetail().getVideoSource() != null)
            {
                headViewHolder.videoLayout.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(topicDTO.getVideoDetail().getThumbnail() + "!detail", headViewHolder.coverImageView, ImageLoaderConfig.getImageOption(TopicDetailActivity.this));


                headViewHolder.playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initVideo();
                    }
                });
            }
        }
    }

    private void updateExtraInfoView(){
        if (topicExtraDTO != null){
            if (topicExtraDTO.isAgreed()){
                headViewHolder.zanButton.setLiked(true);
                headViewHolder.zanButton.setEnabled(false);
            }
            if (topicExtraDTO.isCollected()){
                headViewHolder.collectionButton.setLiked(true);
                headViewHolder.collectionButton.setEnabled(false);
            }
        }
    }

    private void initVideo()
    {
        headViewHolder.playButton.setVisibility(View.GONE);
        headViewHolder.videoView.setVideoPlayCallback(mVideoPlayCallback);

        if(!topicDTO.getType().equals(TopicType.S_VIDEO)){
            playWebVideo(topicDTO.getVideoDetail().getVideoSource());
        }else {
            openDiskLruCache();
            String key = Util.stringToMD5(topicDTO.getVideoDetail().getVideoSource());
            try {

                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);

                if(snapShot != null) {
                    playLocalVideo(DiskCachePath.getDiskCacheDir(this, "short_video").getPath() + "/" + key + ".0");
                }
                else {
                    headViewHolder.roundProgressBarWidthNumber.setVisibility(View.VISIBLE);
                    downloadVideoFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            headViewHolder.videoView.close();
            headViewHolder.playButton.setVisibility(View.VISIBLE);
            headViewHolder.coverImageView.setVisibility(View.VISIBLE);
            headViewHolder.videoView.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                headViewHolder.videoView.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                headViewHolder.videoView.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };

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

                            //隐藏键盘
                            View view = getWindow().peekDecorView();
                            if (view != null) {
                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }


                            //更新reply到UI


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLog.d(tag, "onErrorResponse" + error.toString());
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
                headViewHolder.zanButton.setEnabled(false);
                headViewHolder.zanCountTextView.setText(String.valueOf(response.getContent().getCount()));
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

    private void showLocationProgress(){
        progressDialog = new SpotsDialog(this,R.style.ProgressDialogWait);
        progressDialog.show();
    }

    private void hideLocationProgress(){
        progressDialog.dismiss();
    }


    @Override
    public void onCommit(String text) {
        if (!TextUtils.isEmpty(text)){
            submitReply(text);
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        showLocationProgress();
    }

    @Override
    public void onPageFinished(String url) {
        hideLocationProgress();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    public static class HeadViewHolder
    {
        @InjectView(R.id.user_info_head_layout)RelativeLayout mUserHeadLayout;
        @InjectView(R.id.topic_detail_title)TextView titleTextView;
        @InjectView(R.id.user_head_name)TextView nameTextView;
        @InjectView(R.id.user_head_time)TextView timeTextView;
        @InjectView(R.id.user_head_avatar)ImageView avatarImageView;
        @InjectView(R.id.videoview)SuperVideoPlayer videoView;
        @InjectView(R.id.web_view)AdvancedWebView webView;
        @InjectView(R.id.topic_detail_head_view_video_cover_image)
        ImageView coverImageView;
        @InjectView(R.id.topic_detail_head_view_video_play_btn)
        ImageView playButton;
        @InjectView(R.id.short_video_detail_progress)
        RoundProgressBarWidthNumber roundProgressBarWidthNumber;
        @InjectView(R.id.topic_detail_head_view_video_layout)FrameLayout videoLayout;
        @InjectView(R.id.topic_detail_head_zan)LikeButton zanButton;
        @InjectView(R.id.topic_detail_head_zan_count)TextView zanCountTextView;
        @InjectView(R.id.topic_detail_head_zhan_layout)LinearLayout zanLayout;
        @InjectView(R.id.topic_detail_content_textview)TextView contentTextView;
        @InjectView(R.id.topic_detail_content_container)LinearLayout contentLayout;
        @InjectView(R.id.topic_detail_collection_button)LikeButton collectionButton;
        @InjectView(R.id.rating_layout)LinearLayout ratingLayout;
        @InjectView(R.id.ratingBar)RatingBar ratingBar;
        @InjectView(R.id.rating_value)TextView ratingValue;
        @InjectView(R.id.rating_count)TextView ratingCount;


        public HeadViewHolder(View headView)
        {
            ButterKnife.inject(this, headView);
        }
    }


    //video
    private void playLocalVideo(String path)
    {
        MyLog.d(tag, "videoPath=" + path);

        headViewHolder.coverImageView.setVisibility(View.GONE);
        headViewHolder.videoView.setVisibility(View.VISIBLE);

        headViewHolder.videoView.loadLocalVideo(path);
        headViewHolder.videoView.requestFocus();
    }

    private void playWebVideo(String url){
        headViewHolder.coverImageView.setVisibility(View.GONE);
        headViewHolder.videoView.setVisibility(View.VISIBLE);

        Video video = new Video();
        VideoUrl videoUrl1 = new VideoUrl();
        videoUrl1.setFormatName("auto");
        videoUrl1.setFormatUrl(url);
        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);
        video.setVideoName("");
        video.setVideoUrl(arrayList1);

        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);

        headViewHolder.videoView.loadMultipleVideo(videoArrayList);
        headViewHolder.videoView.requestFocus();
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            headViewHolder.videoView.setPageType(MediaController.PageType.SHRINK);
        }
    }


    private void downloadVideoFile() {

        Thread thread = new Thread(new DownloadRunnable(topicDTO.getVideoDetail().getVideoSource()));
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
        Intent intent = new Intent(TopicDetailActivity.this, ReplyDetailActivity.class);
        ReplyDTO replyDTO = (ReplyDTO) adapter.getItem(position -1);
        intent.putExtra("reply", replyDTO);
        startActivity(intent);
    }

    //share

    private void showShareMenu(final TopicDTO topicDTO){

        String hobby = Util.getApplicationMetaString(this, "HOBBY");


        VideoDetailDTO videoDetailDTO = topicDTO.getVideoDetail();
        String imageUrl = "";
        if (videoDetailDTO != null){
            imageUrl = videoDetailDTO.getThumbnail() + QiniuImageStyle.LIST_ITEM;
        }else {
            imageUrl = HobbyType.getHobbyLogoUrl(hobby);
        }

        final UMImage image = new UMImage(TopicDetailActivity.this, imageUrl);
        final String url = "http://www.jixianxueyuan.com/" + hobby + "/topic/" + topicDTO.getId();

        MenuSheetView menuSheetView =
                new MenuSheetView(this, MenuSheetView.MenuType.GRID, "分享给好友...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        ShareUtils.ShareItem shareItem = null;
                        UMVideo video = null;
                        if (topicDTO.getVideoDetail() != null && !TextUtils.isEmpty(topicDTO.getVideoDetail().getVideoSource())){
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
            Toast.makeText(TopicDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}

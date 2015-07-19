package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.TopicDetailListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.AgreeResultDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.request.ReplyRequest;
import com.jixianxueyuan.dto.request.ZanRequest;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.AnalyzeContent;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.MImageGetter;
import com.jixianxueyuan.util.MTagHandler;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.ReplyWidget;
import com.jixianxueyuan.widget.ReplyWidgetListener;
import com.jixianxueyuan.widget.RoundProgressBarWidthNumber;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yumfee.emoji.EmojiconEditText;
import com.yumfee.emoji.EmojiconTextView;


import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/22/15.
 */
public class TopicDetailActivity extends Activity implements ReplyWidgetListener {

    public final static String tag = TopicDetailActivity.class.getSimpleName();

    @InjectView(R.id.topic_detail_listview)ListView listView;
    @InjectView(R.id.reply_widget_layout)LinearLayout contentLayout;


    TopicDTO topicDTO;

    int currentPage = 0;
    int totalPage = 0;
    TopicDetailListAdapter adapter;


    View headView;
    HeadViewHolder headViewHolder;
    View footerView;
    Button loadMoreButton;

    ReplyWidget replyWidget;

    DiskLruCache mDiskLruCache;

    boolean interceptFlag = false;
    int mProgressNum = 0;


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

                    playVideo(DiskCachePath.getDiskCacheDir(TopicDetailActivity.this, "short_video").getPath() + "/"+ key + ".0" );
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
        topicDTO = (TopicDTO) intent.getSerializableExtra("topic");

        initTopicHeadView();
        initFooterView();

        adapter = new TopicDetailListAdapter(this);
        listView.setAdapter(adapter);

        replyWidget = new ReplyWidget(this, contentLayout);
        replyWidget.setReplyWidgetListener(this);


        requestReplyList();

    }


    private void initTopicHeadView()
    {
        headView = LayoutInflater.from(this).inflate(R.layout.topic_detail_head_view, null);

        headViewHolder = new HeadViewHolder(headView);

        Intent intent = this.getIntent();


        headViewHolder.titleTextView.setText(topicDTO.getTitle());
        headViewHolder.nameTextView.setText(topicDTO.getUser().getName());
        String timeAgo = DateTimeFormatter.getTimeAgo(this, topicDTO.getCreateTime());
        headViewHolder.timeTextView.setText(timeAgo);
        headViewHolder.zanCountTextView.setText(String.valueOf(topicDTO.getAgreeCount()));

        String url =  topicDTO.getUser().getAvatar() + "!androidListAvatar";
        ImageLoader.getInstance().displayImage(url, headViewHolder.avatarImageView);



        //参考链接
        // http://www.ibm.com/developerworks/cn/web/1407_zhangqian_androidhtml/#_清单 4._fromHtml()方法定义
        //http://www.jb51.net/article/46799.htm
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Spanned spanned = Html.fromHtml(topicDTO.getContent(), new MImageGetter(TopicDetailActivity.this.headViewHolder.contentTextView, TopicDetailActivity.this), new MTagHandler());

                Message msg = new Message();
                msg.what = HANDLER_INIT_CONTENT_SPANNED_SUCCESS;
                msg.obj = spanned;
                handler.sendMessage(msg);
            }
        });
        t.start();



        /*List<AnalyzeContent.ContentFragment> contentFragmentList = new LinkedList<AnalyzeContent.ContentFragment>();
        contentFragmentList = AnalyzeContent.analyzeContent2(topicDTO.getContent());

        for(int n=0; n != contentFragmentList.size() ; n++ )
        {
            if(contentFragmentList.get(n).mType == AnalyzeContent.ContentFragment.IMG_URL_TYPE)
            {
                ImageView imageviwe = new ImageView(this);
                imageviwe.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageviwe.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

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
        if (topicDTO.getType() == TopicType.VIDEO || topicDTO.getVideoDetail() != null)
        {
            if(topicDTO.getVideoDetail().getVideoSource() != null)
            {
                initVideo();
            }
        }

        headViewHolder.zanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                submitZan();
            }
        });

        listView.addHeaderView(headView);
    }

    private void initVideo()
    {
        headViewHolder.videoLayout.setVisibility(View.VISIBLE);

        openDiskLruCache();

        String key = Util.stringToMD5(topicDTO.getVideoDetail().getVideoSource());


        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);

            if(snapShot != null)
            {
                playVideo(DiskCachePath.getDiskCacheDir(this, "short_video").getPath() +"/" + key + ".0" );
            }
            else
            {
                headViewHolder.roundProgressBarWidthNumber.setVisibility(View.VISIBLE);
                downloadVideoFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doHideFootView()
    {
        if(totalPage > 1)
        {
            if(footerView.getVisibility() != View.VISIBLE)
            {
                footerView.setVisibility(View.VISIBLE);
            }

            if(currentPage >= totalPage)
            {
                loadMoreButton.setText(R.string.not_more, TextView.BufferType.NORMAL);
            }
        }
    }

    private void initFooterView()
    {
        footerView = LayoutInflater.from(this).inflate(R.layout.loadmore, null, false);

        loadMoreButton = (Button) footerView.findViewById(R.id.loadmore_button);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextPage();
            }
        });
        listView.addFooterView(footerView);
    }

    private void getNextPage()
    {
        if(currentPage < totalPage)
        {
            requestReplyList();
        }
        else
        {
            Toast.makeText(this,"没了", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isLastPage()
    {
        if(currentPage == totalPage)
        {
            return true;
        }

        return false;
    }

    private void requestReplyList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reply + "?topicId="+ topicDTO.getId() + "&page=" + (currentPage + 1) ;
        MyLog.d(tag, "request=" + url);

        MyPageRequest<ReplyDTO> stringRequest = new MyPageRequest<ReplyDTO>(Request.Method.GET,url,ReplyDTO.class,
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

        queue.add(stringRequest);
    }

    private void submitReply(String replyContent)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reply;

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
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ServerMethod.zan;

        ZanRequest zanRequest = new ZanRequest();
        zanRequest.setTopicId(topicDTO.getId());
        zanRequest.setUserId(MyApplication.getContext().getMine().getUserInfo().getId());


        MyRequest<AgreeResultDTO> myRequest = new MyRequest(Request.Method.POST, url, AgreeResultDTO.class,zanRequest, new Response.Listener<MyResponse<AgreeResultDTO>>() {
            @Override
            public void onResponse(MyResponse<AgreeResultDTO> response) {

                headViewHolder.zanButton.setImageResource(R.mipmap.icon_hand_click_1);
                headViewHolder.zanCountTextView.setText(String.valueOf(response.getContent().getCount()));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.d(tag, "onErrorResponse" + error.toString());
            }
        });

        queue.add(myRequest);
        return false;
    }


    @Override
    public void onCommit(String text) {
        submitReply(text);
    }

    public class HeadViewHolder
    {
        @InjectView(R.id.topic_detail_title)EmojiconTextView titleTextView;
        @InjectView(R.id.user_head_name)TextView nameTextView;
        @InjectView(R.id.user_head_time)TextView timeTextView;
        @InjectView(R.id.user_head_avatar)ImageView avatarImageView;
        @InjectView(R.id.topic_detail_content_container)LinearLayout contentLayout;
        @InjectView(R.id.videoview)VideoView videoView;
        @InjectView(R.id.short_video_detail_progress)
        RoundProgressBarWidthNumber roundProgressBarWidthNumber;
        @InjectView(R.id.topic_detail_head_view_video_layout)FrameLayout videoLayout;
        @InjectView(R.id.topic_detail_head_zan)ImageButton zanButton;
        @InjectView(R.id.topic_detail_head_zan_count)TextView zanCountTextView;
        @InjectView(R.id.topic_detail_head_zhan_layout)LinearLayout zanLayout;
        @InjectView(R.id.topic_detail_content_textview)TextView contentTextView;


        public HeadViewHolder(View headView)
        {
            ButterKnife.inject(this, headView);
        }
    }


    //video
    private void playVideo(String path)
    {
        MyLog.d(tag, "videoPath=" + path);

        headViewHolder.videoView.setVideoPath(path);
        headViewHolder.videoView.requestFocus();
        headViewHolder.videoView.start();
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
            mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(this), 1, 20 * 1024 * 1024);
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
                    MyLog.d(ShortVideoDetailActivity.tag, "progress=" + mProgressNum);
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
}

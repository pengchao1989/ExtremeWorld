package com.jixianxueyuan.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pengchao on 2015/4/12.
 */
public class TopicListFragment extends Fragment implements ObservableScrollViewCallbacks {

    public static final String tag = TopicListFragment.class.getSimpleName();

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    @InjectView(R.id.topic_list_fragment_listview)
    ObservableListView listView;
    @InjectView(R.id.top_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.list_background)
    View mListBackgroundView;
    @InjectView(R.id.overlay)
    View mOverlayView;
    @InjectView(R.id.image)
    ImageView mImageView;
    @InjectView(R.id.fab)
    CircleImageView mFab;
    @InjectView(R.id.title)
    TextView mTitleView;

    private ImageView switchButton;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;


    /*以下两个参数用于定义topic范围，从arg传递过来*/
    private String topicType = TopicType.ALL;
    private Long topicTaxonomyId;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;
    private boolean isFine = false;


    TopicListAdapter adapter;

    boolean isRequesting = false;
    boolean isRefreshData = false;

    private MyApplication application;
    private Mine mine;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TopicListAdapter(this.getActivity());
        application = (MyApplication) this.getActivity().getApplicationContext();
        mine = application.getMine();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(tag, "onCreateView");

        View view = inflater.inflate(R.layout.topic_list_fragment, container, false);

        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        if (bundle.containsKey(TopicType.TYPE)) {
            topicType = bundle.getString(TopicType.TYPE);
        }
        if (bundle.containsKey("topicTaxonomyId")) {
            topicTaxonomyId = bundle.getLong("topicTaxonomyId");
        }

        initHeadView();
        initFooterView();

        listView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshTopicList();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        getNextPage();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRefreshData) {
            refreshTopicList();
        }
    }

    private void initHeadView(){
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = 0;
        View paddingView = new View(this.getActivity());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);
        listView.addHeaderView(paddingView);
        listView.setScrollViewCallbacks(this);

        mTitleView.setText(getResources().getString(R.string.app_name));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopicListFragment.this.getActivity(), "FAB is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ImageLoader.getInstance().displayImage(mine.getUserInfo().getAvatar(), mFab, ImageLoaderConfig.getAvatarOption(this.getActivity()));
    }

    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this.getActivity());
        listView.addFooterView(autoLoadMoreView);
    }

    @OnItemClick(R.id.topic_list_fragment_listview)
    void onItemClicked(int position) {
        if (position == 0 || position >= adapter.getCount()) {
            return;
        }
        TopicDTO topicDTO = adapter.getItem(position - 1);

        Intent intent = null;
        switch (topicDTO.getType()) {
            case TopicType.MOOD:
            case TopicType.NEWS:
            case TopicType.COURSE:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.MOOD);
                break;
            case TopicType.DISCUSS:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.DISCUSS);
                break;
            case TopicType.VIDEO:
            case TopicType.S_VIDEO:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.S_VIDEO);
                break;

        }

        if (intent != null) {
            intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
            startActivity(intent);
        }
    }

    private void switchFine() {
        isFine = !isFine;
        if (isFine) {
            switchButton.setImageResource(R.mipmap.ic_option);
        } else {
            switchButton.setImageResource(R.mipmap.ic_option_2);
        }
        refreshTopicList();
    }

    private void refreshTopicList() {
        currentPage = 0;

        requestTopicList();
    }

    private void getNextPage() {
        if (currentPage < totalPage) {
            requestTopicList();
        }
    }

    private void doHideFootView() {
        if (totalPage > 0 && currentPage >= totalPage) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void requestTopicList() {
        if (isRequesting) {
            return;
        }

        swipeRefreshLayout.setRefreshing(true);

        String url = null;

        if (isFine) {
            url = ServerMethod.topic_fine() + "?page=" + (currentPage + 1);
        } else {
            switch (topicType) {
                case TopicType.ALL:
                    url = ServerMethod.topic() + "?page=" + (currentPage + 1);
                    break;
                case TopicType.DISCUSS:
                case TopicType.NEWS:
                    url = ServerMethod.topic() + "?type=" + topicType + "&taxonomyId=" + topicTaxonomyId + "&page=" + (currentPage + 1);
                    break;
                case TopicType.S_VIDEO:
                    url = ServerMethod.topic() + "?type=" + topicType + "&taxonomyId=" + topicTaxonomyId + "&page=" + (currentPage + 1);
                    break;
            }
        }


        MyLog.d(tag, "request url=" + url);
        if (url == null) {
            return;
        }

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest(Request.Method.GET, url, TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if (response.getStatus() == MyResponse.status_ok) {
                            MyPage page = response.getContent();
                            List<TopicDTO> topicDTOs = page.getContents();
                            if (currentPage == 0) {
                                adapter.refresh(topicDTOs);
                            } else {
                                adapter.addDatas(topicDTOs);
                            }

                            isRefreshData = true;

                            totalPage = page.getTotalPages();
                            currentPage = page.getCurPage() + 1;
                            doHideFootView();
                            swipeRefreshLayout.setRefreshing(false);

                        }
                        isRequesting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isRequesting = false;
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(TopicListFragment.this.getActivity(), error);
                    }
                });

        isRequesting = true;
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, TopicListFragment.this.getActivity().findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }
}

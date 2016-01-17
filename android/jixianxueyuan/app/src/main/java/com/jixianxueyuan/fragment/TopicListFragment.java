package com.jixianxueyuan.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.commons.ScrollReceive;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 2015/4/12.
 */
public class TopicListFragment extends FlexibleSpaceWithImageBaseFragment<ObservableListView> {

    public static final String tag = TopicListFragment.class.getSimpleName();
    public static final String INTENT_IS_FIRST = "INTENT_IS_FIRST";
    public static final String INTENT_IS_FINE = "INTENT_IS_FINE";
    public static final String INTENT_HAS_HEAD = "INTENT_HAS_HEAD";

    @InjectView(R.id.scroll)
    ObservableListView listView;
/*    @InjectView(R.id.top_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;*/
    @InjectView(R.id.list_background)
    View mListBackgroundView;


    /*以下两个参数用于定义topic范围，从arg传递过来*/
    private String topicType = TopicType.ALL;
    private Long topicTaxonomyId;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;
    private boolean isFine = false;


    TopicListAdapter adapter;
    boolean isFirst = false;
    boolean isInitData = false;
    boolean isInitView = false;
    boolean hasHead = false;

    boolean isRequesting = false;
    boolean isRefreshData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TopicListAdapter(this.getActivity());
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
        if(bundle.containsKey(INTENT_IS_FINE)){
            isFine = bundle.getBoolean(INTENT_IS_FINE);
        }
        if(bundle.containsKey(INTENT_IS_FIRST)){
            isFirst = bundle.getBoolean(INTENT_IS_FIRST);
        }
        if(bundle.containsKey(INTENT_HAS_HEAD)){
            hasHead = bundle.getBoolean(INTENT_HAS_HEAD,false);
        }

        initHeaderView(view);
        initFooterView();

        listView.setAdapter(adapter);

/*        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                MyLog.d(tag, "onRefresh");
                refreshTopicList();
            }
        });*/

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

        isInitView = true;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRefreshData && isFirst) {
            refreshTopicList();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && isInitView && !isFirst){
            if(!isInitData){
                isInitData = true;
                MyLog.d(tag, "setUserVisibleHint");
                refreshTopicList();
            }
        }
    }

    private void initHeaderView(View view){
        if(!hasHead){
            return;
        }
        View paddingView = new View(getActivity());
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                flexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        listView.addHeaderView(paddingView);

        listView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(listView, new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    int offset = scrollY % flexibleSpaceImageHeight;
                    listView.setSelectionFromTop(0, -offset);
                }
            });
            updateFlexibleSpace(scrollY, view);
        } else {
            updateFlexibleSpace(0, view);
        }

        listView.setScrollViewCallbacks(this);

        updateFlexibleSpace(0, view);
    }


    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this.getActivity());
        listView.addFooterView(autoLoadMoreView);
    }

    @OnItemClick(R.id.scroll)
    void onItemClicked(int position) {
        if (position == 0 || position > adapter.getCount()) {
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

        MobclickAgent.onEvent(TopicListFragment.this.getContext(), UmengEventId.HOME_TOPIC_LIST_ITEM_CLICK, topicDTO.getType());

        if (intent != null) {
            intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
            startActivity(intent);
        }
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

        //swipeRefreshLayout.setRefreshing(true);

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
                            //swipeRefreshLayout.setRefreshing(false);

                        }else {
                            MyErrorHelper.showErrorToast(TopicListFragment.this.getActivity(), response.getError());
                        }
                        isRequesting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isRequesting = false;
                        //swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(TopicListFragment.this.getActivity(), error);
                    }
                });

        isRequesting = true;
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    @SuppressWarnings("NewApi")
    @Override
    public void setScrollY(int scrollY, int threshold) {
        View view = getView();
        if (view == null) {
            return;
        }
        ObservableListView listView = (ObservableListView) view.findViewById(R.id.scroll);
        if (listView == null) {
            return;
        }
        View firstVisibleChild = listView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            listView.setSelectionFromTop(position, -offset);
        }
    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

        View listBackgroundView = view.findViewById(R.id.list_background);

        // Translate list background
        ViewHelper.setTranslationY(listBackgroundView, Math.max(0, -scrollY + flexibleSpaceImageHeight));

        // Also pass this event to parent Fragment
        ScrollReceive parentFragment = (ScrollReceive) getParentFragment();
        if (parentFragment != null) {
            parentFragment.onScrollChanged(scrollY, (ObservableListView) view.findViewById(R.id.scroll));
        }
    }
}

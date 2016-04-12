package com.jixianxueyuan.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.sdk.android.trade.TradeService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.trade.page.ItemDetailPage;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.google.gson.Gson;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CreateTopicActivity;
import com.jixianxueyuan.activity.CreateVideoActivity;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.activity.WebActivity;
import com.jixianxueyuan.adapter.CustomMenuItemAdapter;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.ScrollReceive;
import com.jixianxueyuan.commons.im.IMManager;
import com.jixianxueyuan.config.ExhibitionAction;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.ExhibitionDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.event.HomeRefreshEvent;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ACache;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.ExhibitionItemHolderView;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.victor.loading.rotate.RotateLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 11/15/15.
 */
public class DynamicHomeFragment extends BaseFragment implements ScrollReceive {

    protected static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private List<ExhibitionDTO> exhibitionDTOList;


    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.sliding_tabs) ImageView mSlidingTabLayout;
    //@InjectView(R.id.image) ImageView headImageView;
    @InjectView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @InjectView(R.id.overlay) View overlayView;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.rotate_loading)RotateLoading rotateLoading;
    @InjectView(R.id.message_is_new) ImageView messageIsNewImageView;

    private NavigationAdapter mPagerAdapter;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;

    private MyApplication application;
    private Mine mine;

    private boolean mIsFine = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) this.getActivity().getApplicationContext();
        mine = application.getMine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_home_fragment,container,false);
        ButterKnife.inject(this, view);

        mPagerAdapter = new NavigationAdapter(this.getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIsFine = true;
                    mSlidingTabLayout.setImageResource(R.mipmap.ic_option);
                } else if (position == 1) {
                    mIsFine = false;
                    mSlidingTabLayout.setImageResource(R.mipmap.ic_option_2);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);

        // Initialize the first Fragment's state when layout is completed.
        ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
            @Override
            public void run() {
                translateTab(0, false);
            }
        });


        convenientBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startExhibitionDetail(position);
            }
        });
        initExhibitionList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3500);
        updateMessageView();
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onScrollChanged(int scrollY, Scrollable s) {
        FlexibleSpaceWithImageBaseFragment fragment =
                (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(mPager.getCurrentItem());
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        Scrollable scrollable = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollable == null) {
            return;
        }
        if (scrollable == s) {
            // This method is called by not only the current fragment but also other fragments
            // when their scrollY is changed.
            // So we need to check the caller(S) is the current fragment.
            int adjustedScrollY = Math.min(scrollY, mFlexibleSpaceHeight - mTabHeight);
            translateTab(adjustedScrollY, false);
            propagateScroll(adjustedScrollY);
        }

    }

    private void translateTab(int scrollY, boolean animated) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);


        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
        int minOverlayTransitionY = tabHeight - overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(convenientBanner, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));


/*        // If tabs are moving, cancel it to start a new animation.
        ViewPropertyAnimator.animate(mSlidingTabLayout).cancel();
        // Tabs will move between the top of the screen to the bottom of the image.
        float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight - mTabHeight, 0, mFlexibleSpaceHeight - mTabHeight);
        if (animated) {
            // Animation will be invoked only when the current tab is changed.
            ViewPropertyAnimator.animate(mSlidingTabLayout)
                    .translationY(translationY)
                    .setDuration(200)
                    .start();
        } else {
            // When Fragments' scroll, translate tabs immediately (without animation).
            ViewHelper.setTranslationY(mSlidingTabLayout, translationY);
        }*/
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle(View view) {
        final TextView mTitleView = (TextView) view.findViewById(R.id.title);
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, view.findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }

    private void propagateScroll(int scrollY) {
        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(scrollY);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            FlexibleSpaceWithImageBaseFragment f =
                    (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            f.setScrollY(scrollY, mFlexibleSpaceHeight);
            f.updateFlexibleSpace(scrollY);
        }
    }


    @OnClick(R.id.sliding_tabs) void onSwitch(){
        mIsFine = !mIsFine;
        if (mIsFine){
            mPager.setCurrentItem(0);
            mSlidingTabLayout.setImageResource(R.mipmap.ic_option);
        }else {
            mPager.setCurrentItem(1);
            mSlidingTabLayout.setImageResource(R.mipmap.ic_option_2);
        }
        MobclickAgent.onEvent(DynamicHomeFragment.this.getContext(), UmengEventId.HOME_SWITCH_BUTTON_CLICK);
    }

    @OnClick(R.id.message) void onMessageClick(){
       /* YWIMKit mIMKit = YWAPI.getIMKitInstance();*/
        YWIMKit ywimKit = IMManager.getInstance().getYwimKit();
        Intent intent = ywimKit.getConversationActivityIntent();
        startActivity(intent);
    }

    @OnClick(R.id.add_topic) void onAddTopicClick(){
        showCreateMenuLayout();
        MobclickAgent.onEvent(this.getActivity(), UmengEventId.TAB_CREATE_CLICK);
    }

    @OnClick(R.id.home_fragment_refresh_button)void onRefreshClick(){
        TopicListFragment curTopicListFragment;
        if (mIsFine){
            curTopicListFragment = (TopicListFragment) mPagerAdapter.getItemAt(0);
        }else {
            curTopicListFragment = (TopicListFragment) mPagerAdapter.getItemAt(1);
        }
        curTopicListFragment.doRefresh();

    }

    private void showCreateMenuLayout() {
        CustomMenuItemAdapter adapter = new CustomMenuItemAdapter(this.getActivity(), R.menu.create);
        final DialogPlus dialog = DialogPlus.newDialog(this.getContext())
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        MenuItem menuItem = (MenuItem) item;
                        switch (menuItem.getItemId()){
                            case R.id.menu_create_mood:
                                Intent intent = new Intent(DynamicHomeFragment.this.getActivity(), CreateTopicActivity.class);
                                intent.putExtra(TopicType.TYPE, TopicType.MOOD);
                                startActivity(intent);
                                MobclickAgent.onEvent(DynamicHomeFragment.this.getActivity(), UmengEventId.HOME_CREATE_ITEM_CLICK, TopicType.MOOD);
                                break;
                            case R.id.menu_create_video:
                                Intent intentVideo = new Intent(DynamicHomeFragment.this.getActivity(), CreateVideoActivity.class);
                                intentVideo.putExtra(TopicType.TYPE, TopicType.VIDEO);
                                startActivity(intentVideo);
                                MobclickAgent.onEvent(DynamicHomeFragment.this.getActivity(), UmengEventId.HOME_CREATE_ITEM_CLICK, TopicType.VIDEO);
                                break;
                            case R.id.menu_create_short_video:
                                Intent intentSVideo = new Intent(DynamicHomeFragment.this.getActivity(), CreateTopicActivity.class);
                                intentSVideo.putExtra(TopicType.TYPE, TopicType.S_VIDEO);
                                startActivity(intentSVideo);
                                MobclickAgent.onEvent(DynamicHomeFragment.this.getActivity(), UmengEventId.HOME_CREATE_ITEM_CLICK, TopicType.S_VIDEO);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setGravity(Gravity.TOP)
                .setContentHolder(new GridHolder(adapter.getCount()))
                .create();
        dialog.show();
    }

    /**
     * This adapter provides three types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"fine", "near"};

        private int mScrollY;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            FlexibleSpaceWithImageBaseFragment f;
            Bundle args = new Bundle();
            args.putBoolean(TopicListFragment.INTENT_HAS_HEAD, true);
            switch (position) {
                case 0: {
                    f = new TopicListFragment();
                    args.putBoolean(TopicListFragment.INTENT_IS_FINE, true);
                    args.putBoolean(TopicListFragment.INTENT_IS_FIRST, true);
                    break;
                }
                case 1: {
                    f = new TopicListFragment();
                    args.putBoolean(TopicListFragment.INTENT_IS_FINE, false);
                    break;
                }
                default: {
                    f = new TopicListFragment();
                    break;
                }
            }
            f.setArguments(mScrollY);
            f.setArguments(args);
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    private void initExhibitionList(){
        ACache aCache = ACache.get(this.getContext());
        String url = ServerMethod.exhibition();
        MyPage<ExhibitionDTO> exhibitionDTOMyPage = (MyPage<ExhibitionDTO>) aCache.getAsObject(url);
        if (exhibitionDTOMyPage != null){
            MyLog.e("DynamicHomeFragment", "Load cache success");
            exhibitionDTOList = exhibitionDTOMyPage.getContents();
        }
        if (exhibitionDTOList != null){
            updateExhibitionHeadVIew();
        }else {
            requestExhibitionList();
        }
    }

    private void requestExhibitionList(){

        final String url = ServerMethod.exhibition();

        MyPageRequest<ExhibitionDTO> myPageRequest = new MyPageRequest<ExhibitionDTO>(Request.Method.GET, url, ExhibitionDTO.class,
                new Response.Listener<MyResponse<MyPage<ExhibitionDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<ExhibitionDTO>> response) {
                        if (response.getStatus() == MyResponse.status_ok){
                            if (response.getContent() != null){
                                DynamicHomeFragment.this.exhibitionDTOList = response.getContent().getContents();
                                updateExhibitionHeadVIew();

                                //cache
                                ACache aCache = ACache.get(DynamicHomeFragment.this.getContext());
                                aCache.put(url, response.getContent(), ACache.TIME_HOUR);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void updateExhibitionHeadVIew(){
        convenientBanner.setPages(new CBViewHolderCreator<ExhibitionItemHolderView>() {
            @Override
            public ExhibitionItemHolderView createHolder() {
                return new ExhibitionItemHolderView();
            }
        }, exhibitionDTOList)
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
    }

    private void startExhibitionDetail(int position){
        if (position < exhibitionDTOList.size()){
            ExhibitionDTO exhibitionDTO = exhibitionDTOList.get(position);
            if (ExhibitionAction.OPEN_TOPIC.equals(exhibitionDTO.getAction())){
                String topicJson = exhibitionDTO.getData();
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(topicJson)){
                    TopicDTO topicDTO = gson.fromJson(topicJson, TopicDTO.class);
                    if (!TextUtils.isEmpty(topicDTO.getUrl())){
                        WebActivity.startActivity(this.getActivity(), topicDTO.getTitle(), topicDTO.getUrl());
                    }else {
                        Intent intent = new Intent(DynamicHomeFragment.this.getActivity(), TopicDetailActivity.class);
                        intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
                        startActivity(intent);
                    }
                }
            }else if (ExhibitionAction.OPEN_URL.equals(exhibitionDTO.getAction())){
                WebActivity.startActivity(this.getActivity(), exhibitionDTO.getTitle(), exhibitionDTO.getData());
            }
            else if (ExhibitionAction.OPEN_TAOBAO_PRODUCT.equals(exhibitionDTO.getAction())){
                TradeService tradeService = AlibabaSDK.getService(TradeService.class);
                TaokeParams taokeParams = new TaokeParams();
                taokeParams.pid = "mm_111250070_0_0";
                ItemDetailPage itemDetailPage = new ItemDetailPage(exhibitionDTO.getData(), null);
                tradeService.show(itemDetailPage, taokeParams, DynamicHomeFragment.this.getActivity(), null, new TradeProcessCallback() {

                    @Override
                    public void onFailure(int i, String s) {

                    }

                    @Override
                    public void onPaySuccess(TradeResult tradeResult) {
                        Toast.makeText(DynamicHomeFragment.this.getContext(), "成功", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
            else {
                Toast.makeText(DynamicHomeFragment.this.getContext(), R.string.app_version_is_low, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateMessageView(){
        if (IMManager.getInstance().isReceiverNewMessage(this.getContext())){
            messageIsNewImageView.setVisibility(View.VISIBLE);
        }else {
            messageIsNewImageView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onMessageEvent(HomeRefreshEvent event){
        if (event.message.equals(HomeRefreshEvent.EVENT_START)){
            rotateLoading.start();
        }else if (event.message.equals(HomeRefreshEvent.EVENT_STOP)){
            rotateLoading.stop();
        }
    }
}

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CreateTopicActivity;
import com.jixianxueyuan.activity.CreateVideoActivity;
import com.jixianxueyuan.activity.UserHomeActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.ScrollReceive;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.widget.NetworkImageHolderView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pengchao on 11/15/15.
 */
public class DynamicHomeFragment extends BaseFragment implements ScrollReceive {

    protected static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private List<String> networkImages;
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };


    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.sliding_tabs) ImageView mSlidingTabLayout;
    //@InjectView(R.id.image) ImageView headImageView;
    @InjectView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @InjectView(R.id.overlay) View overlayView;
    @InjectView(R.id.pager)
    ViewPager mPager;

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
        initConvenientBanner();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    private void initConvenientBanner(){

        networkImages= Arrays.asList(images);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages);
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
        YWIMKit mIMKit = YWAPI.getIMKitInstance();
        Intent intent = mIMKit.getConversationActivityIntent();
        startActivity(intent);
    }

    @OnClick(R.id.add_topic) void onAddTopicClick(){
        showMenuSheet(MenuSheetView.MenuType.GRID);
        MobclickAgent.onEvent(this.getActivity(), UmengEventId.TAB_CREATE_CLICK);
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

    private void showMenuSheet(final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(this.getActivity(), menuType, "Create...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
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



                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.create);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }
}

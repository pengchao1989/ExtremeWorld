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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.UserHomeActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.ScrollReceive;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.util.MyLog;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pengchao on 11/15/15.
 */
public class DynamicHomeFragment extends BaseFragment implements ScrollReceive {

    protected static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    @InjectView(R.id.fab) CircleImageView mFab;
    @InjectView(R.id.sliding_tabs) ImageView mSlidingTabLayout;
    @InjectView(R.id.image) ImageView headImageView;
    @InjectView(R.id.overlay) View overlayView;
    @InjectView(R.id.title) TextView titleView;
    @InjectView(R.id.pager) ViewPager mPager;

    private NavigationAdapter mPagerAdapter;
    private int mFlexibleSpaceHeight;
    private int mFlexibleSpaceShowFabOffset;
    private int mTabHeight;
    private int mFabMargin;
    private boolean mFabIsShown;

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

        titleView.setText(mine.getUserInfo().getSignature());

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
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);


        mFabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ImageLoader.getInstance().displayImage(mine.getUserInfo().getAvatar(), mFab, ImageLoaderConfig.getAvatarOption(this.getActivity()));
        ImageLoader.getInstance().displayImage(mine.getUserInfo().getBg() + QiniuImageStyle.COVER, headImageView, ImageLoaderConfig.getHeadOption(this.getActivity()));
        // Initialize the first Fragment's state when layout is completed.
        ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
            @Override
            public void run() {
                translateTab(0, false);
            }
        });

        return view;
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

        translateFAB(scrollY);

    }

    private void translateTab(int scrollY, boolean animated) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);


        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
        int minOverlayTransitionY = tabHeight - overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(headImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY - tabHeight) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle(titleView);
        ViewHelper.setPivotY(titleView, 0);
        ViewHelper.setScaleX(titleView, scale);
        ViewHelper.setScaleY(titleView, scale);

        // Translate title text
        int maxTitleTranslationY = flexibleSpaceImageHeight - tabHeight - getActionBarSize();
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(titleView, titleTranslationY);

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

    private void translateFAB(int scrollY){
        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceHeight - mFab.getHeight() / 2,
                mTabHeight - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = headImageView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, headImageView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
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

    @OnClick(R.id.fab)void onAvatarClick(){
        MobclickAgent.onEvent(DynamicHomeFragment.this.getContext(), UmengEventId.HOME_MINE_AVATAR_CLICK);
        Intent intent = new Intent(this.getActivity(), UserHomeActivity.class);
        intent.putExtra(UserHomeActivity.INTENT_USER, mine.getUserInfo());
        startActivity(intent);

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
}

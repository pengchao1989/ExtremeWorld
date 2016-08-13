package com.jixianxueyuan.widget.alex.swipebacklayout;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jixianxueyuan.widget.alex.callback.OnSwipeBackListener;

/**
 * 作者：alex
 * 时间：2016/8/12 11:49
 * 博客地址：http://www.jianshu.com/users/c3c4ea133871/subscriptions
 */
public class SwipeBackLayout extends FrameLayout {
    private ViewDragHelper viewDragHelper;
    private View rootView;
    private Activity activity;

    /**
     * 手机屏幕的 宽度
     */
    private int screenWidth;
    /**
     * 是否 允许 执行 滑动返回操作
     */
    private boolean canSwipeBack;
    private OnSwipeBackListener onSwipeBackListener;

    public SwipeBackLayout(Context context) {
        super(context);
        initView();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        screenWidth = getScreenWidth();
        canSwipeBack = true;
        ViewDragHelperCallback dragHelperCallback = new ViewDragHelperCallback();
        viewDragHelper = ViewDragHelper.create(this, 1.0F, dragHelperCallback);
        /*跟踪左边界拖动*/
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    /**
     * 绑定 Activity
     *
     * @param activity 容器 Activity
     */
    public SwipeBackLayout attachActivity(Activity activity) {
        this.activity = activity;
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        View content = decor.getChildAt(0);
        decor.removeView(content);
        rootView = content;
        addView(content);
        decor.addView(this);
        return this;
    }

    /**
     * 是否 允许 执行 滑动返回操作
     */
    public SwipeBackLayout canSwipeBack(boolean canSwipeBack) {
        this.canSwipeBack = canSwipeBack;
        return this;
    }

    public SwipeBackLayout onSwipeBackListener(OnSwipeBackListener onSwipeBackListener) {
        this.onSwipeBackListener = onSwipeBackListener;
        return this;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!canSwipeBack) {
            return false;
        }
        /*拦截代理*/
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * 这里必须 返回 true，否则 viewDragHelper 将会失效
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canSwipeBack) {
            return false;
        }
        Log.e("","进来了");
        /*Touch Event 代理*/
        viewDragHelper.processTouchEvent(event);
        //return super.onTouchEvent(event);
        return true;
    }

    /*view 刚初始化的时候就会被调用一次*/
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!isInEditMode() && (viewDragHelper != null) && viewDragHelper.continueSettling(true)) {
            /*开启自动滑动*/
            ViewCompat.postInvalidateOnAnimation(this);
            invalidate();
        }
    }

    private final class ViewDragHelperCallback extends ViewDragHelper.Callback {

        /**
         * 手指释放的时候回调
         */
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.e("","进来了");
            // 拖动距离大于屏幕的一半右移，拖动距离小于屏幕的一半左移
            int left = releasedChild.getLeft();
            if (left > getWidth() / 2) {
                viewDragHelper.settleCapturedViewAt(getWidth(), 0);
                invalidate();
            } else {
                viewDragHelper.settleCapturedViewAt(0, 0);
                invalidate();
            }
        }

        /**
         * 当captureview的位置发生改变时回调
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.e(""," 进度 = " + left * 100 / screenWidth);
            if (onSwipeBackListener != null) {
                onSwipeBackListener.onSwipeProgress(left * 100 / screenWidth);
            }
            if (left >= (screenWidth - 4)) {
                finish();
            }
        }

        /**
         * 当触摸到边界时回调。
         */
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            /**捕捉要拖拽的对象*/
            viewDragHelper.captureChildView(rootView, pointerId);
        }

        /**
         * true的时候会锁住当前的边界，false则unLock。
         */
        public boolean onEdgeLock(int edgeFlags) {
            return false;
        }

        /**
         * 在边界拖动时回调
         * 返回值为true 可以滑动 ；为false 则不能滑动
         */
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            /*移动子 View*/
            viewDragHelper.captureChildView(rootView, pointerId);
        }

        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        /**
         * 尝试捕获子view， 返回true表示允许。
         *
         * @param child     尝试捕获的view
         * @param pointerId 指示器id？
         */
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        /**
         * 处理水平方向上的拖动，返回值就是最终确定的移动的位置。
         * 实际上就是判断如果这个坐标在layout之内 那我们就返回这个坐标值。
         * 除此之外就是如果你的layout设置了padding的话，
         * 也可以让子view的活动范围在padding之内的.
         *
         * @param child 被拖动到view
         * @param left  移动到达的x轴的距离
         * @param dx    建议的移动的x距离
         */
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e("","进来了");
            /*拖动限制（大于左边界）*/
            return Math.max(0, left);
        }
    }

    private void finish() {
        if (onSwipeBackListener != null) {
            onSwipeBackListener.onSwipeProgress(100);
            onSwipeBackListener.onFinish();
        }
        if (activity != null) {
            activity.finish();
        }
        if (viewDragHelper != null) {
            viewDragHelper.cancel();
        }
        if (rootView != null) {
            rootView.destroyDrawingCache();
        }
        viewDragHelper = null;
        rootView = null;
        activity = null;
    }

    /**
     * 获得屏幕高度
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}

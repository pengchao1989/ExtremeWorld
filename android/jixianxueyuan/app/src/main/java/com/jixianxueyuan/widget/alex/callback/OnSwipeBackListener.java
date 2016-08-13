package com.jixianxueyuan.widget.alex.callback;

/**
 * 作者：Alex
 * 时间：2016年08月13日    00:55
 * 博客：http://www.jianshu.com/users/c3c4ea133871/subscriptions
 */

public abstract class OnSwipeBackListener {
    /**
     * 滑动 进度 的 监听 [0, 100]
     */
    public void onSwipeProgress(int progress) {
    }

    /**
     * 滑动结束 的监听
     */
    public abstract void onFinish();
}

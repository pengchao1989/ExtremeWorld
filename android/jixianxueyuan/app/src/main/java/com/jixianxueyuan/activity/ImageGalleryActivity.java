package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.HackyViewPager;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.widget.MyActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 3/12/16.
 * topic图片浏览
 */
public class ImageGalleryActivity extends BaseActivity {

    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_IMAGE_URL_LIST = "INTENT_IMAGE_URL_LIST";

    private String title;
    private ArrayList<String> imageUrlList;

    @InjectView(R.id.image_gallery_actionbar)
    MyActionBar actionBar;
    @InjectView(R.id.view_pager)
    HackyViewPager mViewPager;
    @InjectView(R.id.pager_index)
    TextView pageIndexTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery_activity);
        ButterKnife.inject(this);

        imageUrlList = getIntent().getStringArrayListExtra(INTENT_IMAGE_URL_LIST);
        title = getIntent().getStringExtra(INTENT_TITLE);
        if (imageUrlList == null || imageUrlList.size() == 0){
            finish();
        }

        actionBar.setTitle(title);

        mViewPager.setAdapter(new SamplePagerAdapter(this, imageUrlList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateIndexView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState != null) {
        }

        updateIndexView(1);
    }

    private void updateIndexView(int position) {
        String indexString = position + "/" + imageUrlList.size();
        pageIndexTextView.setText(indexString);
    }

    public static void startActivity(Context context, String title, ArrayList<String> imageUrlList){
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putStringArrayListExtra(INTENT_IMAGE_URL_LIST, imageUrlList);
        context.startActivity(intent);
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private ArrayList<String> imageUrlArrayList;
        private Context context;

        public SamplePagerAdapter(Context context, ArrayList<String> imageList){
            this.context = context;
            imageUrlArrayList = new ArrayList<String>();
            imageUrlArrayList.addAll(imageList);
        }

        @Override
        public int getCount() {
            return imageUrlArrayList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            ImageLoader.getInstance().displayImage(imageUrlArrayList.get(position), photoView, ImageLoaderConfig.getImageOption(context));

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
        }
        super.onSaveInstanceState(outState);
    }
}

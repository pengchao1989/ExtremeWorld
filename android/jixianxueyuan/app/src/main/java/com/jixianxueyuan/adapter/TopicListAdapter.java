package com.jixianxueyuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.UserHomeActivity;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.ScreenUtils;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import cn.nekocode.badge.BadgeDrawable;


/**
 * Created by pengchao on 2015/4/12.
 */
public class TopicListAdapter extends BaseAdapter {

    Context context;

    List<TopicDTO> topicDTOList;

    private DisplayImageOptions options;

    public TopicListAdapter(Context context)
    {
        this.context = context;
        topicDTOList = new ArrayList<TopicDTO>();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    public void refresh(List<TopicDTO> list)
    {
        topicDTOList.clear();
        topicDTOList.addAll(list);

        this.notifyDataSetChanged();
    }

    public void addDatas(List<TopicDTO> topicDTOs)
    {
        topicDTOList.addAll(topicDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topicDTOList.size();
    }

    @Override
    public TopicDTO getItem(int position) {
        if(position < topicDTOList.size()){
            return topicDTOList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return topicDTOList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.topic_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TopicDTO topicDTO = topicDTOList.get(position);


        viewHolder.nameTextView.setText(topicDTO.getUser().getName());
        String timeAgo = DateTimeFormatter.getTimeAgo(context, topicDTO.getCreateTime());
        viewHolder.timeTextView.setText(timeAgo);
        viewHolder.viewCountTextView.setText( String.valueOf(topicDTO.getAllReplyCount()));
        viewHolder.agreeCountTextView.setText(String.valueOf(topicDTO.getAgreeCount()));

        String avatarUrl = topicDTO.getUser().getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }

        if (!TextUtils.isEmpty(avatarUrl)){
            Uri uri = Uri.parse(avatarUrl);
            viewHolder.avatarImageView.setImageURI(uri);
        }else {
            Uri uri = Uri.EMPTY;
            viewHolder.avatarImageView.setImageURI(uri);
        }


        MediaWrapDTO mediawrap = topicDTO.getMediaWrap();

        VideoDetailDTO videoDetailDTO = topicDTO.getVideoDetail();



        if(topicDTO.getType() != null && topicDTO.getType().length() > 0)
        {
            switch (topicDTO.getType())
            {
                case TopicType.MOOD:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_mood), context.getString(R.string.mood), topicDTO);
                    break;

                case TopicType.DISCUSS:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_mood), context.getString(R.string.discuss), topicDTO);
                    break;

                case TopicType.VIDEO:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_video), context.getString(R.string.video), topicDTO);
                    break;

                case TopicType.S_VIDEO:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_video), context.getString(R.string.s_video), topicDTO);
                    break;

                case TopicType.ACTIVITY:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_mood), context.getString(R.string.activity), topicDTO);
                    break;
                case TopicType.NEWS:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_news), context.getString(R.string.news), topicDTO);
                    break;
                case TopicType.COURSE:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_course), context.getString(R.string.course), topicDTO);
                    break;
                case TopicType.CHALLENGE:
                    showTitle(viewHolder, context.getResources().getColor(R.color.topic_type_challenge), context.getString(R.string.challenge), topicDTO);
                    break;
            }

        }


        //多媒体信息
        if(videoDetailDTO != null)
        {
            viewHolder.videoFrontLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(videoDetailDTO.getThumbnail())){
                Uri uri = Uri.parse(videoDetailDTO.getThumbnail() + QiniuImageStyle.LIST_ITEM);
                if (uri == null){
                    uri = Uri.EMPTY;
                }
                viewHolder.videoFrontImageView.setImageURI(uri);
            }
        }
        else
        {
            viewHolder.videoFrontLayout.setVisibility(View.GONE);
        }

        viewHolder.topicImageView_1.setVisibility(View.GONE);
        viewHolder.topicImageView_2.setVisibility(View.GONE);
        viewHolder.topicImageView_3.setVisibility(View.GONE);
        viewHolder.topicImageView_4.setVisibility(View.GONE);
        viewHolder.topicImageView_5.setVisibility(View.GONE);


        if(mediawrap != null)
        {
            if(mediawrap.getMedias().size() > 0)
            {

                MediaDTO mediaDto = mediawrap.getMedias().get(0);
                String url =  mediaDto.getPath();
                viewHolder.topicImageView_1.setVisibility(View.VISIBLE);
                viewHolder.topicImageView_1.setImageURI(ImageUriParseUtil.parse(url + QiniuImageStyle.LIST_ITEM));
            }
            if(mediawrap.getMedias().size() > 1)
            {

                MediaDTO mediaDto = mediawrap.getMedias().get(1);
                String url =  mediaDto.getPath();
                viewHolder.topicImageView_2.setVisibility(View.VISIBLE);
                viewHolder.topicImageView_2.setImageURI(ImageUriParseUtil.parse(url + QiniuImageStyle.LIST_ITEM));
            }
            if(mediawrap.getMedias().size() > 2)
            {
                MediaDTO mediaDto = mediawrap.getMedias().get(2);
                String url =  mediaDto.getPath();
                viewHolder.topicImageView_3.setVisibility(View.VISIBLE);
                viewHolder.topicImageView_3.setImageURI(ImageUriParseUtil.parse(url + QiniuImageStyle.LIST_ITEM));
            }
            if(mediawrap.getMedias().size() > 3)
            {
                MediaDTO mediaDto = mediawrap.getMedias().get(3);
                String url =  mediaDto.getPath();
                viewHolder.topicImageView_4.setVisibility(View.VISIBLE);
                viewHolder.topicImageView_4.setImageURI(ImageUriParseUtil.parse(url + QiniuImageStyle.LIST_ITEM));
            }
            if(mediawrap.getMedias().size() > 4)
            {
                MediaDTO mediaDto = mediawrap.getMedias().get(4);
                String url =  mediaDto.getPath();
                viewHolder.topicImageView_5.setVisibility(View.VISIBLE);
                viewHolder.topicImageView_5.setImageURI(ImageUriParseUtil.parse(url + QiniuImageStyle.LIST_ITEM));
            }
        }

        viewHolder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, UmengEventId.TOPIC_LIST_AVATAR_CLICK);
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.INTENT_USER_MIN, topicDTOList.get(position).getUser());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void showTitle(ViewHolder viewHolder, int badgeColor, String badgeString, TopicDTO topicDTO) {
/*       BadgeDrawable drawable2 =
                new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                        .badgeColor(badgeColor)
                        .text1(badgeString)
                        .textSize(ScreenUtils.sp2px(context,13.8f))
                        .build();
        SpannableString spannableString =
                new SpannableString(TextUtils.concat(
                        drawable2.toSpannable(),
                        " ", topicDTO.getTitle()
                        ));*/

        SpannableString ss;
        badgeString = "[" + badgeString + "]";
        ss=new SpannableString(badgeString + topicDTO.getTitle());
        ss.setSpan(new ForegroundColorSpan(badgeColor), 0, badgeString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.titleTextView.setText(ss);
    }


    public static class ViewHolder{

        @BindView(R.id.topic_list_item_title)
        TextView titleTextView;
        @BindView(R.id.topic_list_item_avatar)
        SimpleDraweeView avatarImageView;

        @BindView(R.id.topic_list_item_name)
        TextView nameTextView;

        @BindView(R.id.topic_list_item_time)
        TextView timeTextView;

        @BindView(R.id.topic_list_item_view_count)
        TextView viewCountTextView;

        @BindView(R.id.topic_list_item_agree_count)
        TextView agreeCountTextView;

        @BindView(R.id.topic_list_item_image_1)
        SimpleDraweeView topicImageView_1;

        @BindView(R.id.topic_list_item_image_2)
        SimpleDraweeView topicImageView_2;

        @BindView(R.id.topic_list_item_image_3)
        SimpleDraweeView topicImageView_3;

        @BindView(R.id.topic_list_item_image_4)
        SimpleDraweeView topicImageView_4;

        @BindView(R.id.topic_list_item_image_5)
        SimpleDraweeView topicImageView_5;

        @BindView(R.id.topic_list_item_video_front_layout)
        RelativeLayout videoFrontLayout;

        @BindView(R.id.topic_list_item_video_front_image)
        SimpleDraweeView videoFrontImageView;


        public ViewHolder(View itemView){

            ButterKnife.bind(this,itemView);
        }
    }
}

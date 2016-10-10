package com.jixianxueyuan.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.jixianxueyuan.dto.CollectionDTO;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 2/28/16.
 */
public class CollectionListAdapter extends BaseAdapter{

    private Context context;
    private List<CollectionDTO> collectionDTOList;

    public CollectionListAdapter(Context context){
        this.context = context;
        collectionDTOList = new ArrayList<CollectionDTO>();
    }

    public void addData(List<CollectionDTO> collectionDTOs){
        collectionDTOList.addAll(collectionDTOs);
        this.notifyDataSetChanged();
    }

    public void refreshData(List<CollectionDTO> collectionDTOs){
        collectionDTOList.clear();
        collectionDTOList.addAll(collectionDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collectionDTOList.size();
    }

    @Override
    public CollectionDTO getItem(int position) {
        return collectionDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.collection_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final TopicDTO topicDTO = collectionDTOList.get(position).getTopic();


        viewHolder.nameTextView.setText(topicDTO.getUser().getName());
        String timeAgo = DateTimeFormatter.getTimeAgo(context, topicDTO.getCreateTime());
        viewHolder.timeTextView.setText(timeAgo);

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
            ImageLoader.getInstance().displayImage(videoDetailDTO.getThumbnail() + "!AndroidListItem", viewHolder.videoFrontImageView);
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
                viewHolder.topicImageView_1.setVisibility(View.VISIBLE);
                MediaDTO mediaDto = mediawrap.getMedias().get(0);

                String url =  mediaDto.getPath();
                ImageLoader.getInstance().displayImage(url + "!AndroidListItem", viewHolder.topicImageView_1);
            }
            if(mediawrap.getMedias().size() > 1)
            {
                viewHolder.topicImageView_2.setVisibility(View.VISIBLE);
                MediaDTO mediaDto = mediawrap.getMedias().get(1);

                String url =  mediaDto.getPath();
                ImageLoader.getInstance().displayImage(url + "!AndroidListItem", viewHolder.topicImageView_2);
            }
            if(mediawrap.getMedias().size() > 2)
            {
                viewHolder.topicImageView_3.setVisibility(View.VISIBLE);
                MediaDTO mediaDto = mediawrap.getMedias().get(2);

                String url =  mediaDto.getPath();
                ImageLoader.getInstance().displayImage(url + "!AndroidListItem", viewHolder.topicImageView_3);
            }
            if(mediawrap.getMedias().size() > 3)
            {
                viewHolder.topicImageView_4.setVisibility(View.VISIBLE);
                MediaDTO mediaDto = mediawrap.getMedias().get(3);

                String url =  mediaDto.getPath();
                ImageLoader.getInstance().displayImage(url + "!AndroidListItem", viewHolder.topicImageView_4);
            }
            if(mediawrap.getMedias().size() > 4)
            {
                viewHolder.topicImageView_5.setVisibility(View.VISIBLE);
                MediaDTO mediaDto = mediawrap.getMedias().get(4);

                String url =  mediaDto.getPath();
                ImageLoader.getInstance().displayImage(url + "!AndroidListItem", viewHolder.topicImageView_5);
            }
        }

        viewHolder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, UmengEventId.TOPIC_LIST_AVATAR_CLICK);
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.INTENT_USER_MIN, topicDTO.getUser());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void showTitle(ViewHolder viewHolder, int badgeColor, String badgeString, TopicDTO topicDTO) {
        SpannableString ss;
        badgeString = "[" + badgeString + "]";
        ss=new SpannableString(badgeString + topicDTO.getTitle());
        ss.setSpan(new ForegroundColorSpan(badgeColor), 0, badgeString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString =
                new SpannableString(TextUtils.concat(
                        ss,
                        " ", topicDTO.getTitle()
                ));


        viewHolder.titleTextView.setText(spannableString);
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

        @BindView(R.id.topic_list_item_image_1)
        ImageView topicImageView_1;

        @BindView(R.id.topic_list_item_image_2)
        ImageView topicImageView_2;

        @BindView(R.id.topic_list_item_image_3)
        ImageView topicImageView_3;

        @BindView(R.id.topic_list_item_image_4)
        ImageView topicImageView_4;

        @BindView(R.id.topic_list_item_image_5)
        ImageView topicImageView_5;

        @BindView(R.id.topic_list_item_video_front_layout)
        RelativeLayout videoFrontLayout;

        @BindView(R.id.topic_list_item_video_front_image)
        ImageView videoFrontImageView;


        public ViewHolder(View itemView){

            ButterKnife.bind(this, itemView);
        }
    }
}

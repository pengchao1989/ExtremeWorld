package com.jixianxueyuan.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.UserHomeActivity;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.SubReplyDTO;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 5/23/15.
 */
public class TopicDetailListAdapter extends BaseAdapter {

    Context context;
    LinkedList<ReplyDTO> replyDTOs;

    public TopicDetailListAdapter(Context context)
    {
        this.context = context;
        replyDTOs = new LinkedList<ReplyDTO>();
    }

    public void addNextPageData(List<ReplyDTO> replyDTOs)
    {
        this.replyDTOs.addAll(replyDTOs);
        this.notifyDataSetChanged();
    }

    public void addNew(ReplyDTO replyDTO)
    {
        this.replyDTOs.add(replyDTO);
        this.notifyDataSetChanged();
    }

    public void adFrontPageData()
    {

    }

    @Override
    public int getCount() {
        return replyDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return replyDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return replyDTOs.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null)
        {
            convertView = LinearLayout.inflate(context, R.layout.topic_detail_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        final ReplyDTO replyDTO = replyDTOs.get(position);

        viewHolder.nameTextView.setText(replyDTO.getUser().getName());
        viewHolder.timeTextView.setText(replyDTO.getCreateTime());
        viewHolder.replyContentTextView.setText(replyDTO.getContent());
        viewHolder.floorTextView.setText(replyDTO.getFloor() + context.getString(R.string.floor));

        String avatarUrl = replyDTO.getUser().getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }
        viewHolder.avatarImageView.setImageURI(ImageUriParseUtil.parse(avatarUrl));
        viewHolder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomeActivity.startActivity(context, replyDTO.getUser());
            }
        });

        //reply image
        MediaWrapDTO mediaWrapDTO = replyDTO.getMediaWrap();
        if (mediaWrapDTO != null){
            List<MediaDTO> mediaDTOList = mediaWrapDTO.getMedias();
            if (mediaDTOList != null && mediaDTOList.size() > 0){
                MediaDTO mediaDTO = mediaDTOList.get(0);
                if (MediaDTO.TYPE_IMAGE.equals(mediaDTO.getType())){
                    viewHolder.imageView.setVisibility(View.VISIBLE);
                    viewHolder.imageView.setImageURI(ImageUriParseUtil.parse(mediaDTO.getPath() + QiniuImageStyle.LIST_ITEM));
                }
            }else {
                viewHolder.imageView.setVisibility(View.GONE);
            }
        }else {
            viewHolder.imageView.setVisibility(View.GONE);
        }


        //sub reply
        showSubReply(viewHolder, replyDTO);

        return convertView;
    }

    private void showSubReply(ViewHolder viewHolder, ReplyDTO replyDTO) {
        viewHolder.subReplyContent1.setVisibility(View.GONE);
        viewHolder.subReplyContent2.setVisibility(View.GONE);
        viewHolder.subReplyContent3.setVisibility(View.GONE);
        viewHolder.loadMoreSubReplyTextView.setVisibility(View.GONE);

        List<SubReplyDTO> subReplyDTOList = replyDTO.getSubReplys();
        if(subReplyDTOList.size() > 0){
            viewHolder.subReplyContent1.setVisibility(View.VISIBLE);

            String name = subReplyDTOList.get(0).getUser().getName();
            String content = subReplyDTOList.get(0).getContent();
            SpannableString ss=new SpannableString(name+":"+content);

            //ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan(){

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(context.getResources().getColor(R.color.blue));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
                    MyLog.d("TopicDetailListAdapter", "onCLick");
                }
            },0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.subReplyContent1.setText(ss);

        }
        if(subReplyDTOList.size() > 1){
            viewHolder.subReplyContent2.setVisibility(View.VISIBLE);
            String name = subReplyDTOList.get(1).getUser().getName();
            String content = subReplyDTOList.get(1).getContent();
            SpannableString ss=new SpannableString(name+":"+content);

            //ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(context.getResources().getColor(R.color.blue));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
                }
            }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.subReplyContent2.setText(ss);
        }
        if(subReplyDTOList.size() > 2){
            viewHolder.subReplyContent3.setVisibility(View.VISIBLE);
            String name = subReplyDTOList.get(2).getUser().getName();
            String content = subReplyDTOList.get(2).getContent();
            SpannableString ss=new SpannableString(name+":"+content);

            //ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(context.getResources().getColor(R.color.blue));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
                }
            }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.subReplyContent3.setText(ss);


        }
        if(subReplyDTOList.size() > 3){
            viewHolder.loadMoreSubReplyTextView.setVisibility(View.VISIBLE);
            String loadMoreStr = context.getString(R.string.load_sub_reply);
            loadMoreStr = String.format(loadMoreStr, subReplyDTOList.size() - 3);
            viewHolder.loadMoreSubReplyTextView.setText(loadMoreStr);
        }
    }


    public static class ViewHolder
    {
        @BindView(R.id.user_head_avatar)SimpleDraweeView avatarImageView;
        @BindView(R.id.user_head_name)TextView nameTextView;
        @BindView(R.id.user_head_time)TextView timeTextView;
        @BindView(R.id.user_head_right_tip)TextView floorTextView;
        @BindView(R.id.topic_detail_list_item_reply_content)TextView replyContentTextView;
        @BindView(R.id.image_view)SimpleDraweeView imageView;

        @BindView(R.id.topic_detail_list_item_sub_reply_content_1)
        TextView subReplyContent1;
        @BindView(R.id.topic_detail_list_item_sub_reply_content_2)
        TextView subReplyContent2;
        @BindView(R.id.topic_detail_list_item_sub_reply_content_3)
        TextView subReplyContent3;
        @BindView(R.id.topic_detail_list_item_sub_reply_loadmore)
        TextView loadMoreSubReplyTextView;

        public ViewHolder(View itemView)
        {
            ButterKnife.bind(this, itemView);
        }
    }

}

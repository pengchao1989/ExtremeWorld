package com.jixianxueyuan.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.ReplyWidgetImageListAdapter;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 5/24/15.
 */
public class ReplyWidget implements ReplyWidgetImageListAdapter.OnImageDeleteListener {

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int REQUEST_VIDEO_CODE = 2;


    Context context;
    ReplyWidgetListener replyWidgetListener;

    LinearLayout contentContainer;
    LinearLayout actionContainer;

    LinearLayout bottomContainer;
    ImageView addButton;
    ImageView addImageButton;
    ImageView submitButton;
    EditText contentEditText;
    ImageView hasDotImageView;

    RecyclerView recyclerView;
    ReplyWidgetImageListAdapter imageListAdapter;


    private boolean isKeyboardShow = false;
    private boolean isLiked = false;

    public ReplyWidget(Context context, LinearLayout contentContainer)
    {
        this.context = context;
        this.contentContainer = contentContainer;

        initView();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case REQUEST_IMAGE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    // do your logic ....
                    if(null != imageListAdapter){
                        imageListAdapter.setImagePathList(path);
                        modifyHasDotView();
                        if (replyWidgetListener != null){
                            replyWidgetListener.onImageChange(imageListAdapter.getImagePathList());
                        }
                    }

                }
                break;
        }
    }

    private void initView()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.reply_widget, null);

        bottomContainer = (LinearLayout)view.findViewById(R.id.reply_widget_bottom_container);
        actionContainer = (LinearLayout)view.findViewById(R.id.reply_widget_action_container);

        addButton = (ImageView) view.findViewById(R.id.reply_widget_add_button);
        addImageButton = (ImageView) view.findViewById(R.id.reply_add_image_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.reply_widget_image_listview);

        submitButton = (ImageView) view.findViewById(R.id.reply_widget_submit_button);
        contentEditText = (EditText) view.findViewById(R.id.reply_widget_edittext);
        hasDotImageView = (ImageView) view.findViewById(R.id.reply_widget_has_dot);
        final View rootView = view.findViewById(R.id.reply_widget_root_view);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isKeyboardShow){
                    hideKeyboard();
                    switchActionContainer();
                }else {
                    replyWidgetListener.onLikeClicked();
                }
            }
        });

        //On submit, add the edittext text to listview and clear the edittext
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newText = contentEditText.getText().toString();

                if (replyWidgetListener != null) {
                    replyWidgetListener.onCommit(newText);
                }
            }
        });

        contentEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isKeyBoardShow = showSoftKeyboard(contentEditText);
                if (isKeyBoardShow) {
                    hideActionContainer();
                }
                return false;
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initImageList();
                gotoSelectImage();
            }
        });


        contentContainer.addView(view);
    }

    public String getText(){
        return contentEditText.getText().toString();
    }

    public void setText(String text){
        contentEditText.setText(text);
        contentEditText.setSelection(text.length());
    }

    public void setHint(String hint){
        contentEditText.setText("");
        contentEditText.setHint(hint);
    }

    public void resetHint(){
        contentEditText.setText("");
        contentEditText.setHint(R.string.we_speek);
    }

    public void showKeyboard(){
        showSoftKeyboard(contentEditText);
    }

    public void hideKeyboard(){
        hideSoftKeyboard(contentEditText);
        switchAddButton();
    }

    public void setReplyWidgetListener(ReplyWidgetListener replyWidgetListener) {
        this.replyWidgetListener = replyWidgetListener;
    }

    public void setLiked(boolean liked){
        this.isLiked = liked;
        if (isLiked){
            switchAddButton();
        }
    }

    public boolean isLiked(){
        return isLiked;
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    private void switchActionContainer(){
        if(bottomContainer.getVisibility() == View.GONE){
            showActionContainer();
        }else {
            hideActionContainer();
        }
    }

    private void showActionContainer(){
        bottomContainer.setVisibility(View.VISIBLE);
    }

    private void hideActionContainer(){
        bottomContainer.setVisibility(View.GONE);
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean showSoftKeyboard(View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean bool = inputMethodManager.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        return bool;
    }

    private void modifyHasDotView(){
        if(null != imageListAdapter){
            if(imageListAdapter.getImagePathList().size() > 0){
                hasDotImageView.setVisibility(View.VISIBLE);
            }else {
                hasDotImageView.setVisibility(View.GONE);
            }
        }
    }

    private void initImageList(){
        if(null == imageListAdapter){
            imageListAdapter = new ReplyWidgetImageListAdapter(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imageListAdapter);
        }
    }

    private void gotoSelectImage(){
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);

        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

        if(imageListAdapter != null){
            if(imageListAdapter.getImagePathList().size() > 0){
                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, imageListAdapter.getImagePathList());
            }
        }

        ((Activity)context).startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    private void switchAddButton(){
        if (isKeyboardShow){
            addButton.setImageResource(R.mipmap.more);
        }else{
            if (bottomContainer.getVisibility() == View.VISIBLE){
                addButton.setImageResource(R.mipmap.more);
            }else {
                if (isLiked){
                    addButton.setImageResource(R.mipmap.like_4);
                }else {
                    addButton.setImageResource(R.mipmap.like_3);
                }
            }
        }
    }
    public void clean()
    {
        if(contentEditText != null) {
            contentEditText.setText("");
        }
        if (imageListAdapter != null){
            imageListAdapter.clean();
        }
        if (hasDotImageView != null){
            hasDotImageView.setVisibility(View.GONE);
        }
    }

    public void onKeyboardChange(boolean isShow){
        isKeyboardShow = isShow;
        switchAddButton();
    }

    @Override
    public void onDelete(int size) {
        if(size == 0){
            modifyHasDotView();
        }
        if (replyWidgetListener != null){
            replyWidgetListener.onImageChange(imageListAdapter.getImagePathList());
        }
    }
}

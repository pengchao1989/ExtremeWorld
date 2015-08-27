package com.jixianxueyuan.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.ReplyWidgetImageListAdapter;
import com.jixianxueyuan.util.MyLog;
import com.yumfee.emoji.EmojiconEditText;
import com.yumfee.emoji.EmojiconGridView;
import com.yumfee.emoji.EmojiconsPopup;
import com.yumfee.emoji.emoji.Emojicon;

import java.util.ArrayList;
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
    ImageView addEmojiButton;
    LinearLayout addImageButton;
    ImageView submitButton;
    EmojiconEditText emojiconEditText;
    ImageView hasDotImageView;

    RecyclerView recyclerView;
    ReplyWidgetImageListAdapter imageListAdapter;

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
        addEmojiButton = (ImageView) view.findViewById(R.id.reply_widget_emoji_button);
        addImageButton = (LinearLayout) view.findViewById(R.id.reply_add_image_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.reply_widget_image_listview);

        submitButton = (ImageView) view.findViewById(R.id.reply_widget_submit_button);
        emojiconEditText = (EmojiconEditText) view.findViewById(R.id.reply_widget_edittext);
        hasDotImageView = (ImageView) view.findViewById(R.id.reply_widget_has_dot);
        final View rootView = view.findViewById(R.id.reply_widget_root_view);

        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(rootView, context);


        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change addEmojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                //changeEmojiKeyboardIcon(addEmojiButton, R.mipmap.smiley);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojiconEditText == null || emojicon == null) {
                    return;
                }

                int start = emojiconEditText.getSelectionStart();
                int end = emojiconEditText.getSelectionEnd();
                if (start < 0) {
                    emojiconEditText.append(emojicon.getEmoji());
                } else {
                    emojiconEditText.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconEditText.dispatchKeyEvent(event);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(emojiconEditText);
                switchActionContainer();
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        addEmojiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                bottomContainer.setVisibility(View.GONE);

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        //changeEmojiKeyboardIcon(addEmojiButton, R.mipmap.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        emojiconEditText.setFocusableInTouchMode(true);
                        emojiconEditText.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
                        //changeEmojiKeyboardIcon(addEmojiButton, R.mipmap.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });

        //On submit, add the edittext text to listview and clear the edittext
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newText = emojiconEditText.getText().toString();

                if (replyWidgetListener != null) {
                    replyWidgetListener.onCommit(newText);
                }
            }
        });

        emojiconEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isKeyBoardShow = showSoftKeyboard(emojiconEditText);
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
        return emojiconEditText.getText().toString();
    }

    public void setText(String text){
        emojiconEditText.setText(text);
        emojiconEditText.setSelection(text.length());
    }

    public void setHint(String hint){
        emojiconEditText.setText("");
        emojiconEditText.setHint(hint);
    }

    public void resetHint(){
        emojiconEditText.setText("");
        emojiconEditText.setHint(R.string.we_speek);
    }

    public void showKeyboard(){
        showSoftKeyboard(emojiconEditText);
    }

    public void hideKeyboard(){
        hideSoftKeyboard(emojiconEditText);
    }

    public void setReplyWidgetListener(ReplyWidgetListener replyWidgetListener) {
        this.replyWidgetListener = replyWidgetListener;
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
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 4);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

        if(imageListAdapter != null){
            if(imageListAdapter.getImagePathList().size() > 0){
                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, imageListAdapter.getImagePathList());
            }
        }

        ((Activity)context).startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    public void clean()
    {
        if(emojiconEditText != null)
        {
            emojiconEditText.setText("");
        }
    }

    @Override
    public void onDelete(int size) {
        if(size == 0){
            modifyHasDotView();
        }
    }
}

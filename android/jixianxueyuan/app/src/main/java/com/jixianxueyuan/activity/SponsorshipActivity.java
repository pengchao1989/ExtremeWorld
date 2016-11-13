package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CustomMenuItemAdapter;
import com.jixianxueyuan.adapter.SponsorshipListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.commons.pay.PayHelper;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.SponsorshipDTO;
import com.jixianxueyuan.dto.SponsorshipTradeDTO;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.request.SponsorshipRequestDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 12/3/15.
 */
public class SponsorshipActivity extends BaseActivity {

    public static final String TAG = SponsorshipActivity.class.getSimpleName();

    @BindView(R.id.bottom_sheet)
    BottomSheetLayout bottomSheetLayout;
    @BindView(R.id.sponsorship_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.sponsorship_activity_list_view)
    ListView listView;

    double money = 0.1;

    private SponsorshipListAdapter adapter;

    private List<SponsorshipDTO> sponsorshipDTOList;

    private PayHelper payHelper;

    private SponsorshipRequestDTO sponsorshipRequestDTO;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsorship_activity);
        ButterKnife.bind(this);

        bottomSheetLayout.setPeekOnDismiss(true);

        adapter = new SponsorshipListAdapter(this);
        listView.setAdapter(adapter);

        initFooterView();
        registerListener();

        refreshData();
    }

    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this);
        listView.addFooterView(autoLoadMoreView);
    }

    @OnItemClick(R.id.sponsorship_activity_list_view)void onItemClick(int position){
        SponsorshipDTO sponsorshipDTO = adapter.getItem(position);

        Intent intent = new Intent(SponsorshipActivity.this, UserHomeActivity.class);
        intent.putExtra(UserHomeActivity.INTENT_USER_MIN, sponsorshipDTO.getUser());
        startActivity(intent);
    }

    private void registerListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        getNextPage();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }
    private void refreshData(){
        currentPage = 0;
        requestNewSponsorship();
    }

    private void getNextPage(){
        if (currentPage < totalPage) {
            requestNewSponsorship();
        }
    }

    private void doHideFootView() {
        if (totalPage > 0 && currentPage >= totalPage) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void requestTopSponsorship(){

        String url = ServerMethod.sponsorship();

        MyPageRequest<SponsorshipDTO> myPageRequest =
                new MyPageRequest<SponsorshipDTO>(Request.Method.GET, url, SponsorshipDTO.class, new Response.Listener<MyResponse<MyPage<SponsorshipDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<SponsorshipDTO>> response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void requestNewSponsorship(){

        String url = ServerMethod.sponsorship() + "?page=" + (currentPage + 1);

        MyPageRequest<SponsorshipDTO> myPageRequest =
                new MyPageRequest<SponsorshipDTO>(Request.Method.GET, url, SponsorshipDTO.class, new Response.Listener<MyResponse<MyPage<SponsorshipDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<SponsorshipDTO>> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            sponsorshipDTOList = response.getContent().getContents();
                            if (currentPage == 0){
                                adapter.setData(sponsorshipDTOList);
                            }else {
                                adapter.addData(sponsorshipDTOList);
                            }

                            totalPage = response.getContent().getTotalPages();
                            currentPage = response.getContent().getCurPage() + 1;
                            doHideFootView();

                        }else {
                            MyErrorHelper.showErrorToast(SponsorshipActivity.this, response.getError());
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyVolleyErrorHelper.showError(SponsorshipActivity.this, error);
                    }
                });
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void createSponsorshipOrder(){

        String url = ServerMethod.sponsorship();
        MyRequest<SponsorshipTradeDTO> myRequest = new MyRequest<SponsorshipTradeDTO>(Request.Method.POST,
                url, SponsorshipTradeDTO.class,
                sponsorshipRequestDTO,
                new Response.Listener<MyResponse<SponsorshipTradeDTO>>() {
                    @Override
                    public void onResponse(MyResponse<SponsorshipTradeDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok){
                            if(payHelper == null){
                                payHelper = new PayHelper(SponsorshipActivity.this);
                            }
                            SponsorshipTradeDTO sponsorshipTradeDTO = response.getContent();
                            if(sponsorshipTradeDTO != null){

                            }else {

                            }
                            payHelper.pay(sponsorshipTradeDTO.getTrade().getInternalTradeNo(),
                                    String.valueOf(sponsorshipTradeDTO.getSum()));
                        }else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void showMoneyGridSheet(){
        CustomMenuItemAdapter adapter = new CustomMenuItemAdapter(this, R.menu.donation_money);
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(adapter)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialogPlus, View view) {
                        TextView moneyTextView = (TextView) dialogPlus.getHolderView().findViewById(R.id.sponsorship_bottom_form_money_text);
                        switch (view.getId()) {
                            case R.id.sponsorship_bottom_form_money_1:
                                money = 1;
                                updateMoneyTextView(moneyTextView, money);
                                break;
                            case R.id.sponsorship_bottom_form_money_5:
                                money = 5;
                                updateMoneyTextView(moneyTextView, money);
                                break;
                            case R.id.sponsorship_bottom_form_money_20:
                                money = 20;
                                updateMoneyTextView(moneyTextView, money);
                                break;
                            case R.id.sponsorship_bottom_form_money_50:
                                money = 50;
                                updateMoneyTextView(moneyTextView, money);
                                break;
                            case R.id.sponsorship_bottom_form_money_100:
                                money = 100;
                                updateMoneyTextView(moneyTextView, money);
                                break;
                            case R.id.sponsorship_bottom_form_cancel:
                                dialogPlus.dismiss();
                                break;
                            case R.id.sponsorship_bottom_form_ok:
                                TextView messageTextView = (TextView) dialogPlus.getHolderView().findViewById(R.id.sponsorship_bottom_form_message_edit);
                                String message = messageTextView.getText().toString();
                                buildSponsorshipRequestDTO(money, message);
                                createSponsorshipOrder();
                                dialogPlus.dismiss();
                                break;
                        }
                    }
                })
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(R.layout.sponsorship_bottom_form_layout))
                .create();
        dialog.show();

        TextView moneyTextView = (TextView) dialog.getHolderView().findViewById(R.id.sponsorship_bottom_form_money_text);
        updateMoneyTextView(moneyTextView, 0.1);

    }

    private void updateMoneyTextView(TextView textView, double money){
        String moneyText = getString(R.string.donation) + String.format("%.2f", money) + getString(R.string.money_unit);
        textView.setText(moneyText);
    }

    private void buildSponsorshipRequestDTO(double money, String message) {
        if(sponsorshipRequestDTO == null){
            sponsorshipRequestDTO = new SponsorshipRequestDTO();
        }
        sponsorshipRequestDTO.setSum(money);
        sponsorshipRequestDTO.setMessage(message);
        HobbyDTO hobbyDTO = new HobbyDTO();
        Long hobbyId = HobbyType.getHobbyId(MyApplication.getContext().getAppInfomation().getCurrentHobbyStamp());
        hobbyDTO.setId(hobbyId);
        sponsorshipRequestDTO.setHobby(hobbyDTO);
        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(MyApplication.getContext().getMine().getUserInfo().getId());
        sponsorshipRequestDTO.setUser(userMinDTO);
        sponsorshipRequestDTO.setTicket(UUID.randomUUID().toString());
    }

    @OnClick(R.id.sponsorship_activity_donation)void onDonation(){
        //showMoneySheet(MenuSheetView.MenuType.GRID);
        showMoneyGridSheet();
    }


}

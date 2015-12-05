package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.SponsorshipListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.commons.pay.PayHelper;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.SponsorshipDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 12/3/15.
 */
public class SponsorshipActivity extends BaseActivity {

    public static final String TAG = SponsorshipActivity.class.getSimpleName();

    @InjectView(R.id.bottom_sheet)
    BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.sponsorship_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.sponsorship_activity_list_view)
    ListView listView;

    private SponsorshipListAdapter adapter;

    private List<SponsorshipDTO> sponsorshipDTOList;

    private PayHelper payHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsorship_activity);
        ButterKnife.inject(this);

        bottomSheetLayout.setPeekOnDismiss(true);

        adapter = new SponsorshipListAdapter(this);
        listView.setAdapter(adapter);

        refreshData();
    }

    private void refreshData(){
        requestNewSponsorship();
    }

    private void getNextPage(){

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
        String url = ServerMethod.sponsorship();

        MyPageRequest<SponsorshipDTO> myPageRequest =
                new MyPageRequest<SponsorshipDTO>(Request.Method.GET, url, SponsorshipDTO.class, new Response.Listener<MyResponse<MyPage<SponsorshipDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<SponsorshipDTO>> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            sponsorshipDTOList = response.getContent().getContents();
                            adapter.setData(sponsorshipDTOList);
                        }else {
                            MyErrorHelper.showErrorToast(SponsorshipActivity.this, response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyVolleyErrorHelper.showError(SponsorshipActivity.this, error);
                    }
                });
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void showMoneySheet(final MenuSheetView.MenuType menuType){
        MenuSheetView menuSheetView =
                new MenuSheetView(SponsorshipActivity.this, menuType, getString(R.string.donation), new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String money = "0.01";
                        switch (item.getItemId()){
                            case R.id.menu_donation_1:
                                money = "1.00";
                                break;
                            case R.id.menu_donation_5:
                                money = "5.00";
                                break;
                            case R.id.menu_donation_20:
                                money = "20.00";
                                break;
                            case R.id.menu_donation_50:
                                money = "50.00";
                                break;
                            case R.id.menu_donation_100:
                                money = "100.00";
                                break;
                        }

                        if(payHelper == null){
                            payHelper = new PayHelper(SponsorshipActivity.this);
                        }
                        payHelper.pay(money);

                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.donation_money);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

    @OnClick(R.id.sponsorship_activity_donation)void onDonation(){
        showMoneySheet(MenuSheetView.MenuType.GRID);
    }


}

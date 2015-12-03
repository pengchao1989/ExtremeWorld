package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.SponsorshipListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.SponsorshipDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 12/3/15.
 */
public class SponsorshipActivity extends BaseActivity {

    public static final String TAG = SponsorshipActivity.class.getSimpleName();

    @InjectView(R.id.sponsorship_activity_list_view)
    ListView listView;

    private SponsorshipListAdapter adapter;

    private List<SponsorshipDTO> sponsorshipDTOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsorship_activity);
        ButterKnife.inject(this);

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


}

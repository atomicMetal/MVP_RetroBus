package com.mobotechnology.bipinpandey.retrofit_handdirty.Network;

import android.util.Log;

import com.mobotechnology.bipinpandey.retrofit_handdirty.Contract.MainContract;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Contract.UserContract;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertListResponse;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.NoticeList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bpn on 12/7/17.
 */

public class ApiClient implements MainContract.ApiCaller,UserContract.ApiCaller {

    /**
     *
     * @param onFinishedListener
     */
    @Override
    public void getNoticeArrayList(final String requestTag,final MainContract.ApiCaller.OnFinishedListener onFinishedListener) {
        ApiService service = RetrofitInstance.getRetrofitInstance().create(ApiService.class);

        Call<NoticeList> call = service.getNoticeData();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<NoticeList>() {
            @Override
            public void onResponse(Call<NoticeList> call, Response<NoticeList> response) {
                onFinishedListener.onFinished(requestTag, response.body().getNoticeArrayList());
            }

            @Override
            public void onFailure(Call<NoticeList> call, Throwable t) {
                onFinishedListener.onFailure(requestTag, t);
            }
        });
    }

    @Override
    public void getExpertList(final String requestTag, final MainContract.ApiCaller.OnFinishedListener onFinishedListener) {
        ApiService service = RetrofitInstance.getRetrofitInstance().create(ApiService.class);

        Call<ExpertListResponse> call = service.get_recommended_users();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ExpertListResponse>() {
            @Override
            public void onResponse(Call<ExpertListResponse> call, Response<ExpertListResponse> response) {
                onFinishedListener.onFinished(requestTag, response.body() != null ? response.body().getExpertInfo() : null);
            }

            @Override
            public void onFailure(Call<ExpertListResponse> call, Throwable t) {
                onFinishedListener.onFailure(requestTag,t);
            }
        });
    }

    @Override
    public void getUserList(final String requestTag, final UserContract.ApiCaller.OnFinishedListener onFinishedListener) {
        ApiService service = RetrofitInstance.getRetrofitInstance().create(ApiService.class);

        Call<ExpertListResponse> call = service.get_recommended_users();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ExpertListResponse>() {
            @Override
            public void onResponse(Call<ExpertListResponse> call, Response<ExpertListResponse> response) {
                onFinishedListener.onFinished(requestTag, response.body() != null ? response.body().getExpertInfo() : null);
            }

            @Override
            public void onFailure(Call<ExpertListResponse> call, Throwable t) {
                onFinishedListener.onFailure(requestTag,t);
            }
        });
    }
}



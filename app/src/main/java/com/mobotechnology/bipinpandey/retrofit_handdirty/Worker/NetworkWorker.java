package com.mobotechnology.bipinpandey.retrofit_handdirty.Worker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Contract.MainContract;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Listners.WorkerCallBack;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertListResponse;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Network.ApiService;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Network.RetrofitInstance;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Presenter.Constant;

import androidx.work.Data;
import androidx.work.Worker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkWorker extends Worker {
    Data outPut;
    WorkerCallBack workerCallBack;
    @NonNull
    @Override
    public Result doWork() {
        /*workerCallBack= (WorkerCallBack) getApplicationContext();
            ApiService service = RetrofitInstance.getRetrofitInstance().create(ApiService.class);

            Call<ExpertListResponse> call = service.get_recommended_users();

            Log.wtf("URL Called", call.request().url() + "");

            call.enqueue(new Callback<ExpertListResponse>() {
                @Override
                public void onResponse(Call<ExpertListResponse> call, Response<ExpertListResponse> response) {
                    //onFinishedListener.onFinished(requestTag, response.body() != null ? response.body().getExpertInfo() : null);
                    Data outPut = new Data.Builder()
                            .putString(Constant.WORK_RESULT,Constant.WORK_SUCCESS)
                            .putString(Constant.WORK_RESPONSE, "hello")
                            .build();
                    Log.e("WORKER", "onResponse: "+ response.body().getMsg() );
                    setOutputData(outPut);

                }

                @Override
                public void onFailure(Call<ExpertListResponse> call, Throwable t) {
                    //onFinishedListener.onFailure(requestTag,t);
                    Data outPut = new Data.Builder()
                            .putString(Constant.WORK_RESULT,Constant.WORK_FAILURE)
                            .putString(Constant.WORK_RESPONSE, String.valueOf(t))
                            .build();
                    setOutputData(outPut);
                }
            });*/
        try {
            ApiService service = RetrofitInstance.getRetrofitInstance()
                    .create(ApiService.class);
            Call<ExpertListResponse> call = service.get_recommended_users();
            ExpertListResponse response = call.execute().body();
            Gson gson = new Gson();
            String strJson=gson.toJson(response);
            if(response.isSuccess()){
                Data outPut = new Data.Builder()
                        .putString(Constant.WORK_RESULT,Constant.WORK_SUCCESS)
                        .putString(Constant.WORK_RESPONSE,strJson)
                        .build();
                setOutputData(outPut);
                return Result.SUCCESS;
            }
            else {
                return Result.FAILURE;

            }
        } catch(Exception ex){
            Data outPut = new Data.Builder()
                    .putString(Constant.WORK_RESULT,Constant.WORK_FAILURE)
                    .putString(Constant.WORK_RESPONSE, String.valueOf(ex))
                    .build();
            setOutputData(outPut);
            return Result.FAILURE;
        }
    }

    private void setData() {

    }


}

package com.mobotechnology.bipinpandey.retrofit_handdirty.Presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

import com.mobotechnology.bipinpandey.retrofit_handdirty.Activity.MainActivity;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Contract.MainContract;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Contract.UserContract;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertInfo;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertListResponse;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.Notice;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Worker.NetworkWorker;

import java.io.File;
import java.util.ArrayList;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * Created by bpn on 12/7/17.
 */

public class MainPresenter implements MainContract.presenter,
        MainContract.ApiCaller.OnFinishedListener,
        UserContract.ApiCaller.OnFinishedListener{

    private MainContract.MainView mainView;
    private MainContract.ApiCaller apiCaller;
    private OneTimeWorkRequest getUsersWorkReq;
    private WorkManager workManager;

    public MainPresenter(MainContract.MainView mainView, MainContract.ApiCaller apiCaller) {
        this.mainView = mainView;
        this.apiCaller = apiCaller;
    }


    private Data getWorkerInput(String requestTag) {
        Data.Builder builder = new Data.Builder();
        builder.putString(Constant.REQUEST_TAG,requestTag);
        return builder.build();
    }
    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onRefreshButtonClick(String requestTag) {
        if(mainView != null){
            mainView.showProgress();
        }
        apiCaller.getNoticeArrayList(requestTag,this);
    }

    @Override
    public void getUserList(String expertListRequest) {
        if(mainView != null){
            mainView.showProgress();
        }
        apiCaller.getExpertList(expertListRequest,this);
    }

    @Override
    public void requestDataFromServer() {
        //apiCaller.getNoticeArrayList(requestTag,this);
    }

    @Override
    public void getUsersFromWorker(String expertListRequest) {
        workManager=WorkManager.getInstance();
        OneTimeWorkRequest.Builder encryptionWork =new OneTimeWorkRequest.Builder(NetworkWorker.class);

        getUsersWorkReq=encryptionWork.setInputData(getWorkerInput(expertListRequest))
                .addTag(Constant.WORK_GETUSER)
                .build();

        workManager.enqueue(getUsersWorkReq);
        workManager.getStatusById(getUsersWorkReq.getId()).observe((LifecycleOwner) mainView, workStatus -> {
            if (workStatus != null && workStatus.getState().isFinished()) {
                String status=workStatus.getOutputData().getString(Constant.WORK_RESULT);
                String response=workStatus.getOutputData().getString(Constant.WORK_RESPONSE);
                Log.e("PRESENTER", "getUsersFromWorker: "+response );
                if(status!=null && !status.equalsIgnoreCase("")){
                    if(mainView != null){
                        mainView.hideProgress();
                    }
                }
            }
        });
    }

/*
    @Override
    public void onFinished() {
        if(mainView != null){
            mainView.setDataToRecyclerView(noticeArrayList);
            mainView.hideProgress();
        }
    }
*/

    @Override
    public void onFinished(String requestTag, Object o) {

        switch (requestTag){
            case MainActivity.EXPERT_LIST_REQUEST:
                if(mainView != null){
                    mainView.setExpertToRecyclerView((ArrayList<ExpertInfo>) o);
                    mainView.hideProgress();
                }
            break;
            case MainActivity.NOTICE_LIST_REQUEST:
                if(mainView != null){
                    mainView.setDataToRecyclerView((ArrayList<Notice>) o);
                    mainView.hideProgress();
                }
                break;
        }

    }

    @Override
    public void onFailure(String expertListRequest, Throwable t) {
        if(mainView != null){
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}

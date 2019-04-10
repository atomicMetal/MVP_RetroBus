package com.mobotechnology.bipinpandey.retrofit_handdirty.Contract;

import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertInfo;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.Notice;
import java.util.ArrayList;

/**
 * Created by bpn on 12/6/17.
 */

public interface MainContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */
    interface presenter{

        void onDestroy();

        void onRefreshButtonClick(String requestTag);

        void getUserList(String requestTag);

        void requestDataFromServer();

        void getUsersFromWorker(String expertListRequest);
    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(ArrayList<Notice> noticeArrayList);

        void setExpertToRecyclerView(ArrayList<ExpertInfo> expertToRecyclerView);

        void onResponseFailure(Throwable throwable);

    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface ApiCaller {

        interface OnFinishedListener <T> {
            void onFinished(String requestTag, T t);
            void onFailure(String requestTag, Throwable t);
        }
        void getNoticeArrayList(String expertListRequest,OnFinishedListener onFinishedListener);
        void getExpertList(String expertListRequest, OnFinishedListener onFinishedListener);

    }

}

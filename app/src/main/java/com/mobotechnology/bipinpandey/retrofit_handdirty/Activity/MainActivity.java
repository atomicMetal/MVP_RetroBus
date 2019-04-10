package com.mobotechnology.bipinpandey.retrofit_handdirty.Activity;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Adapter.ChatLIstingAdapter;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Contract.MainContract;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Listners.RecyclerClickListener;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Listners.RecyclerItemClickListener;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertInfo;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.ExpertListResponse;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Network.ApiClient;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Presenter.Constant;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Presenter.MainPresenter;

import com.mobotechnology.bipinpandey.retrofit_handdirty.Adapter.NoticeAdapter;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Model.Notice;
import com.mobotechnology.bipinpandey.retrofit_handdirty.R;
import com.mobotechnology.bipinpandey.retrofit_handdirty.Worker.NetworkWorker;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {
    public static final String LOGTAG="MainActivity";
    public static final String EXPERT_LIST_REQUEST=LOGTAG+".ExpertListRequest";
    public static final String NOTICE_LIST_REQUEST=LOGTAG+".NoticeListRequest";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MainContract.presenter presenter;
    private OneTimeWorkRequest getUsersWorkReq;
    private WorkManager workManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbarAndRecyclerView();
        initProgressBar();
        presenter = new MainPresenter(this, new ApiClient());
        //presenter.requestDataFromServer();
        //presenter.getUserList(EXPERT_LIST_REQUEST);
        //presenter.getUsersFromWorker(EXPERT_LIST_REQUEST);
        callApi();
    }

    private void callApi() {
        workManager= WorkManager.getInstance();
        OneTimeWorkRequest.Builder encryptionWork =new OneTimeWorkRequest.Builder(NetworkWorker.class);

        getUsersWorkReq=encryptionWork.setInputData(getWorkerInput(EXPERT_LIST_REQUEST))
                .addTag(Constant.WORK_GETUSER)
                .build();

        workManager.enqueue(getUsersWorkReq);

        workManager.getStatusById(getUsersWorkReq.getId()).observe(this, workStatus -> {
            if (workStatus != null && workStatus.getState().isFinished()) {
                String status = workStatus.getOutputData().getString(Constant.WORK_RESULT);
                String response = workStatus.getOutputData().getString(Constant.WORK_RESPONSE);

                Log.e("ACTIVITY", "getUsersFromWorker: " + response);
                if (status != null && !status.equalsIgnoreCase("")) {
                }
                Data outputData = workStatus.getOutputData();
                response = outputData.getString(Constant.WORK_RESPONSE);
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ExpertListResponse posts = gson.fromJson(response,ExpertListResponse.class);

                    Log.e("ACTIVITY:::List", "callApi: " + posts.getExpertInfo().size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Data getWorkerInput(String requestTag) {
        Data.Builder builder = new Data.Builder();
        builder.putString(Constant.REQUEST_TAG,requestTag);
        return builder.build();
    }

    /**
     * Initializing Toolbar and RecyclerView
     */
    private void initializeToolbarAndRecyclerView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view_employee_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }


    /**
     * Initializing progressbar programmatically
     * */
    private void initProgressBar() {
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }


    /**
     * RecyclerItem click event listener
     * */
    private RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClick(Notice notice) {

            Toast.makeText(MainActivity.this,
                    "List title:  " + notice.getTitle(),
                    Toast.LENGTH_LONG).show();

        }
    };

    private RecyclerClickListener clickListener = new RecyclerClickListener() {
        @Override
        public void onItemClick(Notice notice) {

            Toast.makeText(MainActivity.this,
                    "Expert Name:  " + notice.getTitle(),
                    Toast.LENGTH_LONG).show();

        }
    };


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void setDataToRecyclerView(ArrayList<Notice> noticeArrayList) {

        NoticeAdapter adapter = new NoticeAdapter(noticeArrayList , recyclerItemClickListener);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void setExpertToRecyclerView(ArrayList<ExpertInfo> noticeArrayList) {

        ChatLIstingAdapter chatLIstingAdapter=new ChatLIstingAdapter(noticeArrayList,clickListener);
        recyclerView.setAdapter(chatLIstingAdapter);

    }


    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(MainActivity.this,
                "Something went wrong...Error message: " + throwable.getMessage(),
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            presenter.onRefreshButtonClick(NOTICE_LIST_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }


}


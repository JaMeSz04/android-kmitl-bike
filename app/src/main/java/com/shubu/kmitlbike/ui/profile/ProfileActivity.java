package com.shubu.kmitlbike.ui.profile;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.History;
import com.shubu.kmitlbike.data.model.ProfileHistory;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity implements ProfileMVPView {

    @Inject ProfilePresenter presenter;

    @BindView(R.id.profile_history_list)
    RecyclerView recyclerView;


    private List<ProfileHistory> histories = new ArrayList<>();
    private HistoryRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        this.initiateRecyclerView();
        presenter.attachView(this);
        presenter.loadUserHistories();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initiateRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoryRecyclerViewAdapter(this.histories);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onHistoryListLoad(List<ProfileHistory> histories) {
        this.histories = histories;
        if (this.adapter == null)
            adapter = new HistoryRecyclerViewAdapter(this.histories);
        adapter.updateHistoryList(histories);

    }

    @Override
    public void onHistoryLoad(History history) {

    }
}

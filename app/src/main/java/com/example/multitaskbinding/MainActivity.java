package com.example.multitaskbinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.multitaskbinding.list.HomeAdapter;
import com.example.multitaskbinding.list.ImageInfo;
import com.example.multitaskbinding.list.Presenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new Presenter();
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new HomeAdapter(this, mPresenter, mPresenter.getDatas(20)));
    }


    @Override
    protected void onDestroy() {
        mPresenter.finish();
        super.onDestroy();
    }
}

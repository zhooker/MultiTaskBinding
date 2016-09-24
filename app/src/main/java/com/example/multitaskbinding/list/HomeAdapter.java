package com.example.multitaskbinding.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.multitaskbinding.databinding.ListItemBinding;
import com.example.multitaskbinding.R;

import java.util.List;

/**
 * Created by zhuangsj on 16-9-24.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.BindingHolder> {

    private List<ImageInfo> mDatas;
    private Context mContext;
    private Presenter mPresenter;

    public HomeAdapter(Context context, Presenter presenter, List<ImageInfo> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item,
                parent,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ImageInfo info = mDatas.get(position);
        holder.getBinding().setInfo(info);
        holder.getBinding().setPresenter(mPresenter);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class BindingHolder extends RecyclerView.ViewHolder {
        private ListItemBinding binding;

        public BindingHolder(View view) {
            super(view);
        }

        public ListItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ListItemBinding binding) {
            this.binding = binding;
        }
    }
}

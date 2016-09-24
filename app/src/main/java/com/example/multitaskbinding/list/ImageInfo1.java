package com.example.multitaskbinding.list;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by zhuangsj on 16-9-24.
 * 使用BaseObservable方法的时候，注意要在setter中调用notifyPropertyChanged(BR.progress);
 * 还要在getter中注解@Bindable, 在XML上的使用一样。
 */

public class ImageInfo1 extends BaseObservable {
    public int progress;
    public Presenter.State state;
    public String name;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.progress);
    }

    @Bindable
    public Presenter.State getState() {
        return state;
    }

    public void setState(Presenter.State state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "name='" + name + '\'' +
                ", progress=" + progress +
                ", state=" + state +
                '}';
    }
}

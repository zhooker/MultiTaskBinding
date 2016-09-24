package com.example.multitaskbinding.list;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by zhuangsj on 16-9-24.
 * 使用ObservableField的形式时，只能声明为public，但是不能有getter.
 */

public class ImageInfo {
    public ObservableInt progress = new ObservableInt();
    public ObservableField<Presenter.State> state = new ObservableField<>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgress(int progress) {
        this.progress.set(progress);
    }

    public int getPProgress() {
        return this.progress.get();
    }

    public Presenter.State getSState() {
        return state.get();
    }

    public void setState(Presenter.State state) {
        this.state.set(state);
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

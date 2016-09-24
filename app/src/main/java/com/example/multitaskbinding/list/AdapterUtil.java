package com.example.multitaskbinding.list;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.Button;

/**
 * Created by zhuangsj on 16-9-24.
 * 这里是公共函数，主要用来设置Buton在不同状态时的背景图片。
 * 使用BindingAdapter时，函数一定要是static,第一个参数是对应的View类
 */

public class AdapterUtil {

    @BindingAdapter("update")
    public static void updateText(Button btn, Presenter.State state) {
        Log.d("zhuangsj", "updateText: " + state);
        btn.setText("" + state);
    }
}

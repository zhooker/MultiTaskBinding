package com.example.multitaskbinding.list;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuangsj on 16-9-24.
 */

public class Presenter {

    public static final String TAG = "zhuangsj";
    private HashMap<String, TaskRunnable> maps = new HashMap<>();

    public enum State {
        IDLE, DOWNLOADING, PAUSE, FINISH;
    }

    public List<ImageInfo> getDatas(int size) {
        List<ImageInfo> mDatas = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageInfo info = new ImageInfo();
            info.setName("任务" + i);
            info.setState(Presenter.State.IDLE);
            info.setProgress(i * 13 % 100);
            mDatas.add(info);
        }
        return mDatas;
    }

    public void startTask(ImageInfo info) {
        Log.d(TAG, "startTask: " + info);
        if (info.getSState() == State.IDLE || info.getSState() == State.PAUSE) {
            TaskRunnable task = new TaskRunnable(info);
            info.setState(State.DOWNLOADING);
            startTaskRunnable(task);
            maps.put(info.getName(), task);
        } else if (info.getSState() == State.DOWNLOADING) {
            cancleTaskRunnable(info.getName());
        }
    }

    public void finish() {
        for (TaskRunnable task : maps.values()) {
            task.setCancle(true);
        }
    }

    private void startTaskRunnable(TaskRunnable task) {
        new Thread(task).start();
    }

    private void cancleTaskRunnable(String name) {
        TaskRunnable task = maps.remove(name);
        if (task != null) {
            task.setCancle(true);
        }
    }

    private static class TaskRunnable implements Runnable {
        private ImageInfo info;
        private boolean cancle;

        public TaskRunnable(ImageInfo info) {
            this.info = info;
        }

        @Override
        public void run() {
            int i = info.getPProgress();
            for (; i < 100 && !cancle; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                info.setProgress(i + 1);
                Log.d(TAG, info.getName() + " progress=" + info.getPProgress() + ",cancle=" + cancle);
            }
            if (info.getPProgress() == 100)
                info.setState(State.FINISH);
            else
                info.setState(State.PAUSE);
        }

        public void setCancle(boolean cancle) {
            this.cancle = cancle;
        }
    }
}

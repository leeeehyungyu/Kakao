package com.example.kakao;

import android.util.Log;

public class HttpThread implements Runnable {
    final private String TAG = HttpThread.class.getSimpleName();
    ImgList imgList;

    public HttpThread(ImgList imgList) {
        this.imgList = imgList;
    }

    @Override
    public void run() {
        imgList.getImgList();
        Log.e(TAG,((Integer)imgList.getData().size()).toString());
    }
}

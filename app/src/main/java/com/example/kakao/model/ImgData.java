package com.example.kakao.model;

import android.os.Handler;
import android.util.Log;

import com.example.kakao.presenter.MainContract;

import java.util.ArrayList;


public class ImgData  {
    private final String TAG = ImgData.class.getSimpleName();

    private static ImgData imgData;
    private HttpThread ht;
    private ArrayList<String> imgList;
    private MainContract.Presenter presenter;
    private Handler handler;

    private ImgData(MainContract.Presenter presenter){
        this.presenter = presenter;
        this.imgList = new ArrayList<>();
    }

    public static ImgData getInstance(MainContract.Presenter presenter) {
        if (imgData == null) {
            imgData = new ImgData(presenter);
        }
        return imgData;
    }

    public boolean getImages() {

        if(ht!=null&&(ht.getState()!=Thread.State.TERMINATED)) return true; //실행중인 스레드가 있음

        Log.e(TAG,"Thread 생성요청");
        handler = new Handler();

        //thread를 생성해 이미지 url을 가져온다.
        ht = new HttpThread(presenter,handler);
        ht.start();

        return false;
    }

}

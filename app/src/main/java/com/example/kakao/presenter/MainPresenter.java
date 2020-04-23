package com.example.kakao.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kakao.model.ImgData;
import com.example.kakao.adapter.AdapterContract;

import java.util.ArrayList;


public class MainPresenter implements MainContract.Presenter {

    private final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.View view;

    private AdapterContract.View adapterView;
    private AdapterContract.Model adapterModel;

    private ImgData imgData;

    public MainPresenter() { }

    public void setAdapterView(AdapterContract.View adapterView) {
        this.adapterView = adapterView;
    }

    public void setAdapterModel(AdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setImgData(ImgData imgData) {
        this.imgData = imgData;
    }


    @Override
    public void loadItems( boolean isClear) { //모델에서 데이터를 가져온다.

        Log.e(TAG,"loadItems, isClear:"+isClear);

        view.showText(false); //로딩중 표시
        if(isClear) adapterModel.clearItem(); //초기화

        boolean isLoading = imgData.getImages(); //이미지 로딩하기

        if(isLoading) view.makeToast(); //이미 로딩중인경우 토스트출력
    }


    @Override
    public void adapterUpdate(@NonNull ArrayList<String> addList){ //adapter 갱신하기

        Log.e(TAG,"loadItems, data size:"+addList.size());
        view.hideText();
        adapterModel.addItems(addList); //아이템추가
        adapterView.notifyRange(addList.size());
    }

    @Override
    public void haveNoImg() {
        view.showText(true);
        adapterModel.clearItem();
    }
}

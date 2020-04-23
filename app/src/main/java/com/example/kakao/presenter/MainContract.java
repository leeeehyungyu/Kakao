package com.example.kakao.presenter;

import androidx.annotation.NonNull;

import com.example.kakao.model.ImgData;

import java.util.ArrayList;

public interface MainContract {
    interface View{
        void hideText();
        void showText(boolean isEmptyData);
        void makeToast();
    }

    interface  Presenter{
        void attachView(View view);
        void detachView();
        void setImgData(ImgData imageData);
        void loadItems(boolean isClear);
        void adapterUpdate(@NonNull ArrayList<String> addList);
        void haveNoImg();
    }

}

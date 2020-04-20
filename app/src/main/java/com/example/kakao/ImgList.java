package com.example.kakao;

import java.util.ArrayList;

public class ImgList {
    final static String url = "https://www.gettyimagesgallery.com/collection/sasha/";
    private ArrayList<String> data;

    public ImgList() {
        this.data = new ArrayList<>();
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void getImgList(){
        //http 통신으로 리스트 가져오기
        data.add("https://www.gettyimagesgallery.com/wp-content/uploads/2018/12/GettyImages-3360502.jpg");
    }
}

package com.example.kakao.model;

import android.util.Log;

import android.os.Handler;

import com.example.kakao.BuildConfig;
import com.example.kakao.presenter.MainContract;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpThread extends Thread {

    final private static String TAG = HttpThread.class.getSimpleName();
    final private static String URL = "https://www.gettyimagesgallery.com/collection/sasha/";
    final private static String FIND = "data-src=\"";

    private ArrayList<String> imgList;
    private MainContract.Presenter presenter;
    private Handler handler;
    private int cnt;


    public HttpThread(MainContract.Presenter presenter, Handler handler) {
        this.presenter = presenter;
        this.imgList = new ArrayList<>();
        this.handler = handler;
        this.cnt=0;
    }

    //단위 끊어서 보내기
    @Override
    public void run() {
        if(BuildConfig.DEBUG) Log.e(TAG,"run");

        try {
            getImgList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(BuildConfig.DEBUG) Log.e(TAG,"total img :"+cnt);
        if(cnt==0) postNoImg(); //이미지가 없는경우
    }


    private void getImgList() throws Exception {
        if(BuildConfig.DEBUG) Log.e(TAG,"getImgList");

        java.net.URL targetUrl = new URL(URL);
        final HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();

        try (AutoCloseable a = () -> connection.disconnect()){
            connection.setRequestMethod("GET"); //전송방식
            connection.setDoInput(true);        //데이터를 읽어올지 설정
            try (final BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"))){

                ArrayList<String> tempList = new ArrayList<>();
                String result ;
                boolean isFirst = false;
                while ((result = br.readLine()) != null) {

                    //img url을 가지고 있는지 확인
                    int start = result.indexOf(FIND);
                    if (start == -1) continue;
                    start += FIND.length();

                    tempList.add(result.substring(start, result.indexOf("\"", start + 1)));

                    //처음 이미지 5개 로드시 main thread에 넘겨줌, 이후에는 30개씩 전달
                    if((isFirst || tempList.size() <5) && tempList.size()<30) continue;
                    isFirst =true;
                    postImgList(tempList);
                    cnt+=tempList.size();
                    tempList.clear();
                }//while

                if(!tempList.isEmpty()) { //나머지 전달
                    postImgList(tempList);
                    cnt+=tempList.size();
                }
            }
        }
    }


    private void postImgList(ArrayList<String> tossList){
        //이미지 리스트 전달
        if(BuildConfig.DEBUG) Log.e(TAG,"postImgList, list size:"+tossList.size());

        if(!imgList.isEmpty()) imgList.clear();
        imgList.addAll(tossList);

        handler.post(() -> {
            presenter.adapterUpdate(imgList);
        });

    }


    private void postNoImg (){
        //이미지가 없음
        if(BuildConfig.DEBUG) Log.e(TAG,"postNoImg");

        handler.post(() -> {
            presenter.haveNoImg();
        });

    }
}

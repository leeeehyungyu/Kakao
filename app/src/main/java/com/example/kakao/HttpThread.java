package com.example.kakao;

import android.os.Looper;
import android.util.Log;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpThread extends Thread {
    final private String TAG = HttpThread.class.getSimpleName();
    final static String URL = "https://www.gettyimagesgallery.com/collection/sasha/";
    final static String FIND = "data-src=\"";

    private ArrayList<String> imgList;
    private RvAdapter adapter;
    private Handler handler;
    private TextView textView;

    public HttpThread(ArrayList<String> imgList, RvAdapter adapter, Handler handler, TextView textView) {
        this.imgList = imgList;
        this.adapter = adapter;
        this.handler = handler;
        this.textView = textView;
    }

    @Override
    public void run() {
        if(BuildConfig.DEBUG) Log.e(TAG,"run");
        getImgList();
    }

    private void getImgList() {
        if(BuildConfig.DEBUG) Log.e(TAG,"getImgList");

        //try-with-resource로 바꾸기
        try {
            java.net.URL targetUrl = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET"); //전송방식
            connection.setDoInput(true);        //데이터를 읽어올지 설정
            InputStream is = connection.getInputStream();
//            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String result;
            boolean isPosted = false;
            while ((result = br.readLine()) != null) {

                int start = result.indexOf(FIND);
                if (start == -1) continue;
                start += FIND.length();
                imgList.add(result.substring(start, result.indexOf("\"", start + 1)));
                if(!isPosted &&imgList.size()>5) {
                    postImgList();
                    isPosted =true;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(BuildConfig.DEBUG) Log.e(TAG, "로드된 이미지 개수: " + ((Integer) imgList.size()).toString());
    }

    private void postImgList(){
        if(BuildConfig.DEBUG) Log.e(TAG,"postImgList, imgList.size() : "+((Integer)imgList.size()).toString());

        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }
}

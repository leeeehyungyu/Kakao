package com.example.kakao;

import android.os.Looper;
import android.util.Log;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpThread extends Thread {

    final private static String TAG = HttpThread.class.getSimpleName();
    final private static String URL = "https://www.gettyimagesgallery.com/collection/sasha/";
    final private static String FIND = "data-src=\"";
    final private static String NO_IMG = "표시할 이미지가 없습니다.";

    private ArrayList<String> imgList;
    private RvAdapter adapter;
    private Handler handler;
    private TextView textView;

    private int prevSize, loadNum = 30;

    public HttpThread(ArrayList<String> imgList, RvAdapter adapter, Handler handler, TextView textView) {
        this.imgList = imgList;
        this.adapter = adapter;
        this.handler = handler;
        this.textView = textView;
    }

    //단위 끊어서 보내기
    @Override
    public void run() {
        if(BuildConfig.DEBUG) Log.e(TAG,"run");

        prevSize = imgList.size();

        try {
            getImgList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(BuildConfig.DEBUG) Log.e(TAG,"최종 imgList size :"+imgList.size());
        if(imgList.size()==0) postNoImgList();   //로드된 이미지가 없으면
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

                String result ;
                boolean isPosted = false;
                while ((result = br.readLine()) != null) {

                    //img url을 가지고 있는지 확인
                    int start = result.indexOf(FIND);
                    if (start == -1) continue;

                    start += FIND.length();
                    imgList.add(result.substring(start, result.indexOf("\"", start + 1)));

                    //이미지 url 5개 로드시 mainthread에 넘겨줌
                    if (!isPosted && imgList.size() > 4){
                        isPosted =true;
                        postImgList();
                    }
                    if(prevSize +loadNum <= imgList.size()) postImgList();
                }//while
                if(prevSize < imgList.size()) postImgList(); //남은 데이터가 있다면 호출
            }
        }
    }

    private void postImgList(){
        //main thread에 요청
        int cnt = imgList.size()-prevSize;

        if(BuildConfig.DEBUG) Log.e(TAG,"postImgList, adapter 변경 위치:"+prevSize+", 변경할 개수:"+cnt);

        handler.post(() -> {
            textView.setVisibility(View.GONE);

            //adapter 데이터 변경요청
            adapter.notifyItemRangeInserted(prevSize, cnt);
        });

        prevSize = imgList.size();
    }

    private void postNoImgList(){
        //이미지가 없는경우
        if(BuildConfig.DEBUG) Log.e(TAG,"postNoImgList");

        handler.post(() -> textView.setText(NO_IMG));
    }
}

//    private void getImgList() throws IOException {
//        if(BuildConfig.DEBUG) Log.e(TAG,"getImgList");
//        //명확히 닫아줘야할꺼, connection , br, is
//        //try-with-resource로 바꾸기
//        try {
//            java.net.URL targetUrl = new URL(URL);
//            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
//            connection.setRequestMethod("GET"); //전송방식
//            connection.setDoInput(true);        //데이터를 읽어올지 설정
//            InputStream is = connection.getInputStream();
////            StringBuilder sb = new StringBuilder();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//
//            String result;
//            boolean isPosted = false;
//            while ((result = br.readLine()) != null) {
//
//                //img url을 가지고 있는지 확인
//                int start = result.indexOf(FIND);
//                if (start == -1) continue;
//
//                start += FIND.length();
//                imgList.add(result.substring(start, result.indexOf("\"", start + 1)));
//
//                if (isPosted || imgList.size() < 6) continue;
//                isPosted =true;
//                postImgList();
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

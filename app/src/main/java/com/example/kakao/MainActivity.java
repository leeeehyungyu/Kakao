package com.example.kakao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private final  String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rv;
    private RvAdapter adapter;
    private ArrayList<String>imgList;

    private TextView textView;
    private Handler mHandler;
    private HttpThread ht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(BuildConfig.DEBUG) Log.e(TAG,"onStart");
        if(imgList.size()==0 && ht==null){
            //새로고침을 넣어야하나
            ht = new HttpThread(imgList,adapter,mHandler,textView);
            ht.setDaemon(true);
            ht.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(ht!=null) ht.
    }

    private void init(){

        imgList = new ArrayList<>();

        textView = findViewById(R.id.textView);

        mHandler = new Handler();
        //recycler view
        rv = findViewById(R.id.recycler_view);
        rv.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        rv.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RvAdapter(imgList,this);
        rv.setAdapter(adapter);
    }


}



package com.example.kakao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
        loadImgList();
    }

    private void loadImgList(){
        if(BuildConfig.DEBUG) Log.e(TAG,"refreshImgList");

        if(ht!=null && ht.isAlive()) return; //실행중인 스레드가있음

        if(ht==null||(Thread.State.TERMINATED==ht.getState())){

            if(BuildConfig.DEBUG) Log.e(TAG,"load new img");

            imgList.clear();
            adapter.notifyDataSetChanged();

            ht = new HttpThread(imgList,adapter,mHandler,textView);
            ht.setDaemon(true);
            ht.start();

        }else{
            //쓰레드가 이미 실행중이면
            Toast.makeText(getBaseContext(),"이미지를 불러오는 중입니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btn:
                loadImgList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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



package com.example.kakao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ImgList imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

       new Thread(new Runnable() {
            @Override
            public void run() {
                imgList.getImgList();
            }
        }).run();

    }

    private void init(){
        imgList = new ImgList();


        //recycler view
        rv = findViewById(R.id.recylcer_view);
        rv.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new RvAdapter(imgList.getData(), this));
    }
}



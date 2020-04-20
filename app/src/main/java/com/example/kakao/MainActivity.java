package com.example.kakao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImgList imgList = new ImgList();

        //비동기 처리하기
        imgList.getImgList();

        //recycler view
        rv = findViewById(R.id.recylcer_view);
        rv.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rv.setAdapter(new RvAdapter(imgList.getData(), this));
    }
}



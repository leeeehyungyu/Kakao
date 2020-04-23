package com.example.kakao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakao.model.ImgData;
import com.example.kakao.adapter.RvAdapter;
import com.example.kakao.presenter.MainContract;
import com.example.kakao.presenter.MainPresenter;


public class MainActivity extends AppCompatActivity implements MainContract.View {
    private final  String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rv;
    private RvAdapter adapter;
    private TextView textView;
    private MainPresenter mainPresenter;

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
        mainPresenter.loadItems(false);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        mainPresenter.detachView();
    }

    @Override
    public void makeToast() {
        Toast.makeText(getBaseContext(),"로딩중 입니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideText() { //이미지불러왔을때 text 안보이게
        textView.setVisibility(View.GONE);
    }

    @Override
    public void showText(boolean isEmptyData){ //text 표시 , 이미지 로딩중, 로딩실패
        textView.setVisibility(View.VISIBLE);
        textView.setText(isEmptyData? "표시할 이미지가 없습니다." : "이미지 로딩중입니다.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //새로고침
        if(item.getItemId()==R.id.refresh_btn){
            mainPresenter.loadItems(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        textView = findViewById(R.id.textView);

        //recycler view
        rv = findViewById(R.id.recycler_view);
        rv.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        rv.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RvAdapter(this);
        rv.setAdapter(adapter);

        //presenter
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        mainPresenter.setAdapterModel(adapter);
        mainPresenter.setAdapterView(adapter);
        mainPresenter.setImgData(ImgData.getInstance(mainPresenter));
    }

}



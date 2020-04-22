package com.example.kakao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private ArrayList<String> data;
    private Context context;

    RvAdapter(ArrayList<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new RvAdapter.ViewHolder(inflater.inflate(R.layout.item_img, parent, false));
    }

    @Override
    public int getItemCount() { // 아이템의 개수 반환
        return data.size() ;
    }

    @Override
    public void onBindViewHolder(RvAdapter.ViewHolder holder, int position) {
        //Glide를 쓰냐 아니면 직접 만드냐?
        // glide를 썻을때 캐시를 구현할까?
        Glide.with(context)
                .load(data.get(position))
                .override(512)
                .skipMemoryCache(false)//memory cache 사용
                .diskCacheStrategy(DiskCacheStrategy.ALL)//disk cache 사용하지 않음;
                .error(R.drawable.no_img)
                .into(holder.item_view);

    }

    // ViewHolder 클래스 정의를 통해 Adapter에서 사용할 뷰들을 연결
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_view;

        ViewHolder(View itemView) {
            super(itemView);
            item_view = itemView.findViewById(R.id.itemView);
        }
    }
}

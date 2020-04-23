package com.example.kakao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kakao.R;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> implements AdapterContract.Model,AdapterContract.View {

    private ArrayList<String> data;
    private Context context;
    private int prevSize;


    public RvAdapter( Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        prevSize=0;
    }

    @Override
    public void notifyRange(int cnt) {
        notifyItemRangeChanged(prevSize,cnt);
    }

    @Override
    public void addItems(ArrayList<String> addData) {
        prevSize = data.size();
        this.data.addAll(addData);
    }

    @Override
    public void clearItem() {
        if(data!=null) data.clear();
        notifyDataSetChanged();
    }


    @Override
    public RvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new RvAdapter.ViewHolder(inflater.inflate(R.layout.item_img, parent, false));
    }

    @Override
    public int getItemCount() {
        return data !=null ? data.size() : 0 ;
    }

    @Override
    public void onBindViewHolder(RvAdapter.ViewHolder holder, int position) {

        //glide로 이미지 불러오기
        Glide.with(context)
                .load(data.get(position))
                .override(512)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_img)
                .into(holder.item_view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_view;

        ViewHolder(View itemView) {
            super(itemView);
            item_view = itemView.findViewById(R.id.itemView);
        }
    }
}

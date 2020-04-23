package com.example.kakao.adapter;

import java.util.ArrayList;

public interface AdapterContract {
    interface View {
        void notifyRange(int cnt);
    }
    interface Model {
        void addItems(ArrayList<String> imageItems);
        void clearItem();
    }
}

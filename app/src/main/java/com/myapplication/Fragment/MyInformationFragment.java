package com.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.myapplication.R;
import com.myapplication.activity.LoginActivity;
import com.myapplication.widget.ParallaxScollListView;

public class MyInformationFragment extends Fragment {


//    private ParallaxScollListView mListView;
    private ImageView mImageView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
//        mListView = (ParallaxScollListView) rootView.findViewById(R.id.layout_listview);
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
        mImageView = (ImageView) rootView.findViewById(R.id.img_my_head);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
//        mListView.setZoomRatio(ParallaxScollListView.ZOOM_X2);
////        mListView.setParallaxImageView(mImageView);
//        mListView.addHeaderView(header);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_expandable_list_item_1,
//                new String[]{
//                        "First Item",
//                        "Second Item",
//                        "Third Item",
//                        "Fifth Item",
//                        "Sixth Item",
//                        "Seventh Item",
//                        "Eighth Item",
//                        "Ninth Item",
//                        "Tenth Item",
//                        "....."
//                }
//        );
//        mListView.setAdapter(adapter);

        return rootView;
    }
}

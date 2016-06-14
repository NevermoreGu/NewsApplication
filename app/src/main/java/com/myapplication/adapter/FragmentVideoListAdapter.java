package com.myapplication.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.myapplication.R;
import com.myapplication.base.BaseListAdapter;
import com.myapplication.bean.VideoList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentVideoListAdapter extends BaseListAdapter<VideoList> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder vh;
//        if (convertView == null || convertView.getTag() == null) {
//            convertView = getLayoutInflater(parent.getContext()).inflate(
//                    R.layout.item_fragment_video, parent, false);
//            vh = new ViewHolder(convertView);
//            convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder) convertView.getTag();
//        }
//        VideoList news = mDatas.get(position);
//        if (news != null) {
//            vh.video.setVideoURI(Uri.parse(news.getVideopath()));
//            setText(vh.tvVideoTitle, news.getTitle());
//            setText(vh.tvVideoDesc, news.getDesc());
//            vh.imgStart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    vh.video.start();
//                }
//            });
//        }
//
       return convertView;
    }

    class ViewHolder {

        @InjectView(R.id.vv_item_fragment_video)
        VideoView video;
        @InjectView(R.id.img_item_fragment_start)
        ImageView imgStart;
        @InjectView(R.id.tv_item_fragment_video_title)
        TextView tvVideoTitle;
        @InjectView(R.id.tv_item_fragment_video_desc)
        TextView tvVideoDesc;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

package com.myapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.R;
import com.myapplication.base.BaseListAdapter;
import com.myapplication.bean.NewsList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsListAdapter extends BaseListAdapter<NewsList> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.item_news_list_frament, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        NewsList news = mListPageInfo.getDataList().get(position);
        if (news != null) {
            setDraweeImage(vh.imgNews, news.getIndexpic());
            setText(vh.tvNewsTitle, news.getTitle());
            setText(vh.tvNewsSubTitle, news.getDesc());
            setText(vh.tvNewsThread, news.getComment() + "跟帖");
        }

        return convertView;
    }

    class ViewHolder {

        @InjectView(R.id.img_item_news)
        SimpleDraweeView imgNews;
        @InjectView(R.id.tv_item_news_title)
        TextView tvNewsTitle;
        @InjectView(R.id.tv_item_news_subtitle)
        TextView tvNewsSubTitle;
        @InjectView(R.id.tv_item_news_thread)
        TextView tvNewsThread;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

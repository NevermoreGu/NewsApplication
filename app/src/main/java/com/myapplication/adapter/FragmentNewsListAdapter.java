package com.myapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.R;
import com.myapplication.base.BaseListAdapter;
import com.myapplication.bean.NewsList;
import com.myapplication.util.StringUtils;

public class FragmentNewsListAdapter extends BaseListAdapter<NewsList> {

    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_Three = 2;

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null) {
            NewsList newsList = getItem(position);
            if (newsList.getShowtype() != null && newsList.getShowtype().equals("image")) {
                return TYPE_TWO;
            }
            if (newsList.getAdpic() != null && !StringUtils.isImgUrl(newsList.getIndexpic())) {
                return TYPE_Three;
            }
            if (StringUtils.isEmpty(newsList.getIndexpic())
                    && !StringUtils.isImgUrl(newsList.getIndexpic())) {
                return TYPE_ONE;
            }

            return TYPE_ONE;
        }
        return TYPE_ONE;
    }


    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        NewsList news = mListPageInfo.getDataList().get(position);
        if (getItemViewType(position) == TYPE_ONE) {
            ViewHolder _holder;
            if (convertView == null || convertView.getTag() == null) {
                _holder = new ViewHolder();
                convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.item_news_list_frament, parent, false);
                _holder.tvNewsTitle = (TextView) convertView
                        .findViewById(R.id.tv_item_news_title);
                _holder.tvNewsSubTitle = (TextView) convertView
                        .findViewById(R.id.tv_item_news_subtitle);
                _holder.tvNewsThread = (TextView) convertView
                        .findViewById(R.id.tv_item_news_thread);
                _holder.imgNews = (SimpleDraweeView) convertView
                        .findViewById(R.id.img_item_news);
                convertView.setTag(_holder);
            } else {
                _holder = (ViewHolder) convertView.getTag();
            }
            if (news != null) {
                setWifiDraweeImage(_holder.imgNews, news.getIndexpic());
                setText(_holder.tvNewsTitle, news.getTitle());
                setText(_holder.tvNewsSubTitle, news.getDesc());
                setText(_holder.tvNewsThread, news.getComment() + "跟帖");
            }
        }
        if (getItemViewType(position) == TYPE_TWO) {
            ViewHolder _holder;
            if (convertView == null) {
                _holder = new ViewHolder();
                convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.item_news_list_frament_three_image, parent, false);
                _holder.tvNewsThreeTitle = (TextView) convertView
                        .findViewById(R.id.tv_item_news_three_title);
                _holder.tvNewsThreeThread = (TextView) convertView.findViewById(R.id.tv_item_news_three_thread);
                _holder.imgNewsThreeOne = (SimpleDraweeView) convertView.findViewById(R.id.img_item_news_three_one);
                _holder.imgNewsThreeTwo = (SimpleDraweeView) convertView.findViewById(R.id.img_item_news_three_two);
                _holder.imgNewsThreeThree = (SimpleDraweeView) convertView.findViewById(R.id.img_item_news_three_three);
                _holder.imgNewsThreeOne.setAspectRatio(1.5f);
                _holder.imgNewsThreeTwo.setAspectRatio(1.5f);
                _holder.imgNewsThreeThree.setAspectRatio(1.5f);
                convertView.setTag(_holder);
            } else {
                _holder = (ViewHolder) convertView.getTag();
            }
            if (news != null) {
                setWifiDraweeImage(_holder.imgNewsThreeOne, news.getArticlepic().get(0));
                setWifiDraweeImage(_holder.imgNewsThreeTwo, news.getArticlepic().get(1));
                setWifiDraweeImage(_holder.imgNewsThreeThree, news.getArticlepic().get(2));
                setText(_holder.tvNewsThreeTitle, news.getTitle());
                setText(_holder.tvNewsThreeThread, news.getComment() + "跟帖");
            }
        }
        if (getItemViewType(position) == TYPE_Three) {
            ViewHolder _holder;
            if (convertView == null) {
                _holder = new ViewHolder();
                convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.item_news_list_frament_large_image, parent, false);
                _holder.tvNewsLargeTitle = (TextView) convertView
                        .findViewById(R.id.tv_item_news_large_title);
                _holder.tvNewsLargeThread = (TextView) convertView.findViewById(R.id.tv_item_news_large_thread);
                _holder.imgNewsLarge = (SimpleDraweeView) convertView.findViewById(R.id.img_item_news_large);
                _holder.imgNewsLarge.setAspectRatio(2f);
                convertView.setTag(_holder);
            } else {
                _holder = (ViewHolder) convertView.getTag();
            }
            if (news != null) {
                setWifiDraweeImage(_holder.imgNewsLarge, news.getAdpic());
                setText(_holder.tvNewsLargeTitle, news.getTitle());
                setText(_holder.tvNewsLargeThread, news.getComment() + "跟帖");
            }
        }
        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView imgNews;
        TextView tvNewsTitle;
        TextView tvNewsSubTitle;
        TextView tvNewsThread;

        TextView tvNewsThreeTitle;
        TextView tvNewsThreeThread;
        SimpleDraweeView imgNewsThreeOne;
        SimpleDraweeView imgNewsThreeTwo;
        SimpleDraweeView imgNewsThreeThree;

        TextView tvNewsLargeTitle;
        TextView tvNewsLargeThread;
        SimpleDraweeView imgNewsLarge;
    }

    public ViewHolder getViewHolderByView(int position, ListView listView) {
        int wantedPosition = position - listView.getHeaderViewsCount();
        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
        int wantedChild = wantedPosition - firstPosition;

        if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
            return null;
        }

        View wantedView = listView.getChildAt(wantedChild);
        ViewHolder holder = (ViewHolder) wantedView
                .getTag();
        return holder;
    }
}

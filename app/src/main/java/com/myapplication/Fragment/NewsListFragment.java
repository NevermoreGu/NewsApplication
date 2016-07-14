package com.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.myapplication.R;
import com.myapplication.activity.ParallaxToolbarScrollViewActivity;
import com.myapplication.adapter.FragmentNewsListAdapter;
import com.myapplication.adapter.NewsAutoScrollPagerAdapter;
import com.myapplication.api.MyApi;
import com.myapplication.base.BaseAbsListFragment;
import com.myapplication.base.BaseListAdapter;
import com.myapplication.base.Constants;
import com.myapplication.bean.NewArticleListDao;
import com.myapplication.bean.NewsList;
import com.myapplication.ui.loadmore.LoadMoreContainer;
import com.myapplication.ui.loadmore.LoadMoreHandler;
import com.myapplication.ui.loadmore.LoadMoreListViewContainer;
import com.myapplication.util.GsonUtil;
import com.myapplication.util.StringUtils;
import com.myapplication.util.TDevice;
import com.myapplication.widget.viewpager.autoscrollviewpager.AutoScrollViewPager;
import com.myapplication.widget.viewpager.autoscrollviewpager.CirclePageIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class NewsListFragment extends BaseAbsListFragment<NewsList, NewArticleListDao, ListView> {

    @InjectView(R.id.load_more_container)
    LoadMoreListViewContainer loadMoreContainer;

    private AutoScrollViewPager vpNews;
    private CirclePageIndicator pageIndicator;
    private String id = "";
    private String urlPart = "";
    private List<NewsList> newsLists;
    private int headDataCount = 0;
    private FragmentNewsListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_list;
    }

    @Override
    protected View initHeadView() {
        View header = LayoutInflater.from(getActivity()).inflate(
                R.layout.view_news_auto_scroll_head, null);
        vpNews = (AutoScrollViewPager) header.findViewById(R.id.vp_news);
        ViewGroup.LayoutParams layoutParams = vpNews.getLayoutParams();
        float screen = TDevice.getScreenWidth();
        layoutParams.height = (int) (screen / 2);
        vpNews.setLayoutParams(layoutParams);
        pageIndicator = (CirclePageIndicator) header.findViewById(R.id.pi_news_indicator);
        absListView.addHeaderView(header);
        return header;
    }

    @Override
    protected void initRootView() {
        loadMoreContainer.setAutoLoadMore(true);
        loadMoreContainer.useDefaultFooter();
        loadMoreContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                mCurrentPage++;
                requestListData();
            }
        });
    }

    @Override
    protected BaseListAdapter<NewsList> getListAdapter() {
        adapter = new FragmentNewsListAdapter();
        return adapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "news";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            id = args.getString("id") == null ? "" : args.getString("id");
            urlPart = args.getString("urlPart") == null ? "" : Constants.HTTP_HEAD_PREFIX + args.getString("urlPart");
        }
    }

    @Override
    protected NewArticleListDao parseData(String is) throws Exception {
        NewArticleListDao newArticleListDao = GsonUtil.toBean(is, NewArticleListDao.class);
        if (newArticleListDao != null) {
            newsLists = new ArrayList<>();
//            List<NewsList> mMews = new ArrayList<>();
//            for (NewsList news : newArticleListDao.getNormal()) {
//                if ((news.getShowtype() != null && news.getShowtype().equals("image")) || (StringUtils.isEmpty(news.getIndexpic())
//                        && !StringUtils.isImgUrl(news.getIndexpic()))) {
//                    mMews.add(news);
//                }
//            }
            newsLists.addAll(newArticleListDao.getNormal());
        }
        if (newArticleListDao.getOrders() != null) {
            if (newArticleListDao.getOrders().size() > 0) {
                headDataCount = 1;
            } else {
                headDataCount = 0;
            }
        }
        return newArticleListDao;
    }

    @Override
    protected List<NewsList> parseList() throws Exception {
        return newsLists;
    }

    @Override
    protected int getHeadDataCount() {
        return headDataCount;
    }

    @Override
    protected void executeDataSuccess(NewArticleListDao data) {
        List<NewsList> lbList = new ArrayList<>();
        if (data.getNormal() != null) {
            List<NewsList> noListtemp = new ArrayList<>();

            noListtemp.addAll(data.getNormal());
            int size = 0;
            for (int i = 0; i < noListtemp.size(); i++) {
                if (!StringUtils
                        .isEmpty(noListtemp.get(i).getIndexpic())) {
                    lbList.add(noListtemp.get(i));
                    size++;
                    if (size == 4) {
                        break;
                    }
                }
            }
        }
        List<String> urls = new ArrayList<>();
        for (NewsList newsList : lbList) {
            urls.add(newsList.getIndexpic());
        }
        vpNews.setAdapter(new NewsAutoScrollPagerAdapter(urls, getActivity()));
        pageIndicator.setViewPager(vpNews);
        pageIndicator.setSnap(true);
        vpNews.startAutoScroll(0);
    }

    @Override
    protected NewArticleListDao readData(Serializable serializable) {
        return (NewArticleListDao) serializable;
    }

    @Override
    protected void sendListRequestData() {
        try {
            MyApi.newsList(urlPart, id, mCurrentPage, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(getActivity(), ParallaxToolbarScrollViewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onListViewScrollStop() {
        int firstVisiblePosition = absListView.getFirstVisiblePosition();
        int lastVisiblePosition = absListView.getLastVisiblePosition();
        NewsList newsList = getListAdapter().getItem(firstVisiblePosition);
//        ToastUtil.toast(getActivity(), firstVisiblePosition);
//        adapter.getViewHolderByView(firstVisiblePosition, mListView).;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

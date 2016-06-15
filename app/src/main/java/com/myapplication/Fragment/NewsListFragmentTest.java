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
import com.myapplication.base.BaseListAdapter;
import com.myapplication.base.BaseNewHaveHeadListFragment;
import com.myapplication.base.Constants;
import com.myapplication.bean.NewArticleListDao;
import com.myapplication.bean.NewsHeadListDataModel;
import com.myapplication.bean.NewsList;
import com.myapplication.model.list.ListPageInfo;
import com.myapplication.model.list.PagedListDataModel;
import com.myapplication.ui.loadmore.LoadMoreContainer;
import com.myapplication.ui.loadmore.LoadMoreHandler;
import com.myapplication.ui.loadmore.LoadMoreListViewContainer;
import com.myapplication.util.StringUtils;
import com.myapplication.util.TDevice;
import com.myapplication.widget.autoscrollviewpager.AutoScrollViewPager;
import com.myapplication.widget.autoscrollviewpager.CirclePageIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class NewsListFragmentTest extends BaseNewHaveHeadListFragment<NewsList, NewArticleListDao, ListView> {

    @InjectView(R.id.load_more_container)
    LoadMoreListViewContainer loadMoreContainer;

    private AutoScrollViewPager vpNews;
    private CirclePageIndicator pageIndicator;
    private String id = "";
    private String urlPart = "";
    private int headDataCount = 0;
    private FragmentNewsListAdapter adapter;
    private NewsHeadListDataModel model;

    @Override
    protected BaseListAdapter<NewsList> getListAdapter() {
        adapter = new FragmentNewsListAdapter();
        adapter.setListPageInfo(model.getListPageInfo());
        return adapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_list;
    }

    @Override
    protected View initHeaderView() {
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
                model.queryNextPage();
            }
        });
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
        url = urlPart + "?key=" + id;
        model = new NewsHeadListDataModel(Constants.PAGE_SIZE, url);
        model.setPageListDataHandler(new PagedListDataModel.PagedListDataHandler() {
            @Override
            public void onPageDataLoaded(ListPageInfo<?> listPageInfo) {

            }
        });
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
    protected void updateView(boolean emptyResult, boolean hasMore) {
        loadMoreContainer.loadMoreFinish(emptyResult, hasMore);
    }

    @Override
    protected void updateErrorView(int errorCode, String errorMessage) {
        loadMoreContainer.loadMoreError(0, errorMessage);
    }

    @Override
    protected void sendListRequestData() {
        model.queryFirstPage();
    }

    @Override
    protected NewArticleListDao readData(Serializable serializable) {
        return (NewArticleListDao) serializable;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(getActivity(), ParallaxToolbarScrollViewActivity.class);
        startActivity(intent);
    }

//    @Override
//    protected void onListViewScrollStop() {
//        int firstVisiblePosition = absListView.getFirstVisiblePosition();
//        int lastVisiblePosition = absListView.getLastVisiblePosition();
//        NewsList newsList = getListAdapter().getItem(firstVisiblePosition);
////        ToastUtil.toast(getActivity(), firstVisiblePosition);
////        adapter.getViewHolderByView(firstVisiblePosition, mListView).;
//    }

}

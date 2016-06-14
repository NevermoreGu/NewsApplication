package com.myapplication.Fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapplication.R;
import com.myapplication.base.BaseApplication;
import com.myapplication.base.BaseFragment;
import com.myapplication.bean.AppConfig;
import com.myapplication.interf.OnTabReselectListener;
import com.myapplication.util.StringUtils;
import com.myapplication.widget.Smarttablayout.SmartTabLayout;
import com.myapplication.widget.Smarttablayout.v4.FragmentPagerItems;
import com.myapplication.widget.Smarttablayout.v4.FragmentStatePagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsFragment extends BaseFragment implements
        OnTabReselectListener {

    @InjectView(R.id.viewpagertab)
    SmartTabLayout tabs;
    @InjectView(R.id.pager)
    ViewPager pager;

    private List<String> adapterTitles = new ArrayList<>();
    private List<String> adapterKey = new ArrayList<>();
    private String urlPart = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        initData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(View view) {
        ButterKnife.inject(this, view);
        if (BaseApplication.config != null && BaseApplication.config.getListModules() != null) {
            List<AppConfig.Modules.Item> items = BaseApplication.config.getListModules().getListItem();
            if (items != null && items.size() > 0) {
                for (AppConfig.Modules.Item item : items) {
                    if (item.getClassname() != null && item.getClassname().equals("article")) {
                        urlPart = item.getList_api();
                        if (item.getChannel() != null) {
                            List<AppConfig.Modules.Item.Channel.channelItem> channelItems = item.getChannel().getListItem();
                            if (channelItems != null && channelItems.size() > 0) {
                                for (AppConfig.Modules.Item.Channel.channelItem channelItem : channelItems) {
                                    if (channelItem != null && !StringUtils.isEmpty(channelItem.getKey())) {
                                        adapterKey.add(channelItem.getKey());
                                        adapterTitles.add(channelItem.getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FragmentPagerItems.Creator creator = FragmentPagerItems.with(getActivity());
        for (int position = 0; position < adapterKey.size(); position++) {
            Bundle bundle = new Bundle();
            bundle.putString("id", adapterKey.get(position));
            bundle.putString("urlPart", urlPart);
            creator.add(adapterTitles.get(position), NewsListFragmentTest.class, bundle);
        }

        FragmentStatePagerItemAdapter adapter = new FragmentStatePagerItemAdapter(
                getActivity().getSupportFragmentManager(), creator.create());

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onTabReselect() {
        try {
//            int currentIndex = mViewPager.getCurrentItem();
//            Fragment currentFragment = getChildFragmentManager().getFragments()
//                    .get(currentIndex);
//            if (currentFragment != null
//                    && currentFragment instanceof OnTabReselectListener) {
//                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
//                listener.onTabReselect();
//            }
        } catch (NullPointerException e) {

        }
    }

}

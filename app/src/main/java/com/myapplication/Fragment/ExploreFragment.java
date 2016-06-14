package com.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.myapplication.R;
import com.myapplication.activity.SelectPicturesActivity;
import com.myapplication.adapter.ExploreAdapter;
import com.myapplication.base.BaseFragment;
import com.myapplication.base.Constants;
import com.myapplication.bean.Explore;
import com.myapplication.bean.ImageInfo;
import com.myapplication.bean.SelectPicturesInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class  ExploreFragment extends BaseFragment {

    @InjectView(R.id.btn_explore)
    Button button;
    @InjectView(R.id.listView)
    ListView listView;
    private ArrayList<ImageInfo> pic_list;
    private SelectPicturesInfo mSelectPicturesInfo = new SelectPicturesInfo();
    private ExploreAdapter adapter;
    private List<Explore> explores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        explores = new ArrayList<>();
        adapter = new ExploreAdapter(explores);
        listView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                /*for(ImageInfo pic:pic_list){
                    if(!pic.isAddButton()){
                        list.add(pic.getSource_image());
                    }
                }*/
                openCameraSDKPhotoPick(getActivity(), list);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        switch (requestCode) {
            case SelectPicturesInfo.TAKE_PICTURE_FROM_GALLERY:
                if (result != null) {
                    getBundle(result.getExtras());
                }
                break;

        }
    }

    private void getBundle(Bundle bundle) {
        if (bundle != null) {
            pic_list = new ArrayList<ImageInfo>();

            mSelectPicturesInfo = (SelectPicturesInfo) bundle.getSerializable(SelectPicturesInfo.EXTRA_PARAMETER);
            ArrayList<String> list = mSelectPicturesInfo.getImage_list();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    ImageInfo img = new ImageInfo();
                    img.setSource_image(list.get(i));
                    pic_list.add(img);
                }

            }
            if (pic_list.size() < Constants.MAXSELECTPICTURES) {
                ImageInfo item = new ImageInfo();
                item.setIsAddButton(true);
                pic_list.add(item);
            }
//            mImageGridAdapter.setList(pic_list);
        }
    }

    //本地相册选择
    public void openCameraSDKPhotoPick(Activity activity, ArrayList<String> list) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SelectPicturesActivity.class);
        Bundle b = new Bundle();

        b.putSerializable(SelectPicturesInfo.EXTRA_PARAMETER, mSelectPicturesInfo);
        intent.putExtras(b);
        startActivityForResult(intent, SelectPicturesInfo.TAKE_PICTURE_FROM_GALLERY);

    }

}

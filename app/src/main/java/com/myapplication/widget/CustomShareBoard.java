package com.myapplication.widget;

import android.app.Activity;
import android.widget.PopupWindow;

//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.bean.StatusCode;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners;

public class CustomShareBoard extends PopupWindow {

//    private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
//    private Activity mActivity;
//    private List<Share> sharePlatForms = new ArrayList<>();
//    private int[] drawables = {R.mipmap.sns_weixin_icon, R.mipmap.sns_weixin_timeline_icon, R.mipmap.sns_qqfriends_icon, R.mipmap.sns_qzone_icon, R.mipmap.sns_sina_icon};
//    private SHARE_MEDIA[] medias = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA};

    public CustomShareBoard(Activity activity) {
        super(activity);
//        this.mActivity = activity;
        initData();
        initView(activity);
    }

    private void initData() {
//        for (int i = 0; i < drawables.length; i++) {
//            Share share = new Share(drawables[i], medias[i]);
//            sharePlatForms.add(share);
//        }
    }

    private void initView(final Activity context) {
//        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_board, null);
//        GridView gridView = (GridView) rootView.findViewById(R.id.gv_custom_board);
//        setContentView(rootView);
//        KJAdapter<Share> adapter = new KJAdapter<Share>(gridView, sharePlatForms, R.layout.item_share) {
//            @Override
//            public void convert(AdapterHolder adapterHolder, Share share, boolean b) {
//                adapterHolder.setImageResource(R.id.img_item_share, share.getDrawable());
//            }
//        };
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SHARE_MEDIA platform = ((Share) parent.getAdapter().getItem(position)).getPlatform();
//                performShare(platform);
//            }
//        });
//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        setFocusable(true);
//        setBackgroundDrawable(new BitmapDrawable());
//        setTouchable(true);
//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                TDevice.changeWindowAlpha(context, 1.0f);
//            }
//        });
    }

//    private void performShare(SHARE_MEDIA platform) {
//        mController.postShare(mActivity, platform, new SocializeListeners.SnsPostListener() {
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                String showText = platform.toString();
//                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//                    showText += "平台分享成功";
//                } else {
//                    showText += "平台分享失败";
//                }
//                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
//                dismiss();
//            }
//        });
//    }

}

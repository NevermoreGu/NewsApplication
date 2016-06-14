package com.myapplication.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyPositionActivity extends Activity implements AMap.OnCameraChangeListener, LocationSource,
        AMapLocationListener {

    public final static int Task_Period_Time = 15000;

    MapView mapview;
    @InjectView(R.id.btn_positionme)
    Button btnPositionme;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.et_address)
    AutoCompleteTextView etAddress;
    @InjectView(R.id.btn_search)
    Button btnSearch;
    @InjectView(R.id.btn_send)
    Button btnSend;
    @InjectView(R.id.navi_mark)
    ImageView naviNark;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @InjectView(R.id.ll_send)
    RelativeLayout llSend;


    private AMap aMap;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient = null;
    private OnLocationChangedListener mListener;
    private double lng, lat;
    private LatLng resPos;
    private GeocodeSearch eocoderSearch;
    private boolean flag, isShow;//重新定位标签
    private boolean initMyPos = true;   //是否是点击了位置然后才

    Marker mCarMarker;
    Timer mTimer;
    TimerTask mTask;

    ArrayList<BitmapDescriptor> giflist;
    private Boolean searchNotReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myposition);
        ButterKnife.inject(this);
        mapview = (MapView) findViewById(R.id.mapFragment);
        mapview.onCreate(null);// 此方法必须重写
        initMap();
        eocoderSearch = new GeocodeSearch(this);
        eocoderSearch.setOnGeocodeSearchListener(geoListener);
        mTimer = new Timer(true);

    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapview.getMap();
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(false);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.setMyLocationEnabled(true);
            aMap.setOnCameraChangeListener(this);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        }

    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                lat = amapLocation.getLatitude();
                lng = amapLocation.getLongitude();

                if (initMyPos) {
                    progressBar.setVisibility(View.GONE);
                    tvAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(amapLocation.getAddress());
                    changeCameraToMyPosition();
                    initMyPos = false;
                    onMyFirstLocation(lat, lng);

                    Point point = aMap.getProjection().toScreenLocation(new LatLng(lat, lng));
                    int x = point.x;
                    int y = point.y;
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    private void onMyFirstLocation(Double doubleLat, Double doubleLng) {
        regeocode(doubleLat, doubleLng);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setInterval(10000);//设置定位间隔
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @OnClick({R.id.btn_positionme, R.id.tv_address, R.id.et_address, R.id.btn_search, R.id.btn_send, R.id.tv_up, R.id.tv_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_positionme:
                changeCameraToMyPosition();
                break;
            case R.id.tv_address:
                break;
            case R.id.et_address:
                break;
            case R.id.btn_search:
                if (mCarMarker != null)
                    mCarMarker.remove();
                break;

            case R.id.btn_send:
                actionsendNaviateToTSP();
                break;
            case R.id.tv_up:
                float curTranslationY = llSend.getTranslationY();
                ObjectAnimator animator = ObjectAnimator.ofFloat(llSend, "translationY", -curTranslationY, curTranslationY);
                animator.setDuration(1000);
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        llSend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                break;
            case R.id.tv_down:
                float mCurTranslationY = llSend.getTranslationY();
                ObjectAnimator animatorDown = ObjectAnimator.ofFloat(llSend, "translationY", mCurTranslationY, -mCurTranslationY);
                animatorDown.setDuration(1000);
                animatorDown.start();
                animatorDown.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        llSend.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


                break;
        }
    }

    public void actionsendNaviateToTSP() {
        String mobile;
//        if (mSearchFlag == SEARCH_LPNO) {
//            mobile = curVehicle != null ? curVehicle.mobile : "";
//        } else {
        mobile = etAddress.getText().toString();
//        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        flag = true;
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (flag && naviNark.getVisibility() == View.VISIBLE) {
            resPos = getViewBottomCenterPosition(naviNark);
            regeocode(resPos.latitude, resPos.longitude);
            flag = false;
        }
    }

    private void regeocode(double lat, double lng) {
        LatLonPoint point = new LatLonPoint(lat, lng);
        RegeocodeQuery query = new RegeocodeQuery(point, 200,
                GeocodeSearch.AMAP);
        progressBar.setVisibility(View.VISIBLE);
        tvAddress.setVisibility(View.GONE);
        eocoderSearch.getFromLocationAsyn(query);
    }

    public LatLng getViewBottomCenterPosition(View view) {
        Point point = new Point();
        point.x = view.getLeft() + view.getWidth() / 2;
        point.y = view.getBottom();
        return aMap.getProjection().fromScreenLocation(point);
    }

    private GeocodeSearch.OnGeocodeSearchListener geoListener = new GeocodeSearch.OnGeocodeSearchListener() {

        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            if (rCode == 1000) {
                if (result != null
                        && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {

                    RegeocodeAddress rs = result.getRegeocodeAddress();

                    String address = rs.getFormatAddress();
                    progressBar.setVisibility(View.GONE);
                    tvAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(address);
                    String shortaddress = getShortFormatAddress(rs);
                    tvAddress.setHint(shortaddress);
                }
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult result, int rCode) {

        }
    };

    private String getShortFormatAddress(RegeocodeAddress rs) {
        if (rs == null)
            return "";
        if (!TextUtils.isEmpty(rs.getBuilding())) {
            return rs.getBuilding();
        }
        String address = rs.getFormatAddress();
        String[] splits1 = address.split(this.getString(
                R.string.area1));
        if (splits1 != null && splits1.length > 1) {
            return splits1[splits1.length - 1];
        }

        String[] splits2 = address.split(this.getString(
                R.string.county));
        if (splits2 != null && splits2.length > 1) {
            return splits2[splits2.length - 1];
        }

        String[] splits3 = address
                .split(this.getString(R.string.city));
        if (splits3 != null && splits3.length > 1) {
            return splits3[splits3.length - 1];
        }
        return address;
    }

    /**
     * 改变镜头位置
     *
     * @param lat
     * @param lng
     */
    protected void changeCamera(double lat, double lng) {
        float zoom = aMap.getCameraPosition().zoom;
        CameraPosition cp = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(zoom).tilt(0).build();
        changeCamera(CameraUpdateFactory.newCameraPosition(cp), null);
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        aMap.animateCamera(update, 1000, callback);
        isShow = flag;
    }

    /**
     * 切换镜头到我的位置
     */
    public void changeCameraToMyPosition() {
        naviNark.setVisibility(View.VISIBLE);
        CameraPosition cp = new CameraPosition.Builder().target(new LatLng(lat, lng))
                .zoom(14).tilt(0).build();
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    /**
     * 把一个view转化成bitmap对象
     */
    private static Bitmap getViewBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public void startTask() {
        if (mTimer != null && mTask != null) {
            mTask.cancel();
        }

        mTask = new TimerTask() {

            @Override
            public void run() {

            }
        };
        mTimer.schedule(mTask, 0, Task_Period_Time);
    }

    public void stopTask() {
        if (mTimer != null && mTask != null) {
            mTask.cancel();
        }
    }

    private Marker myPosMarker;

    public void resetMyPosMarker() {
        if (myPosMarker != null) {
            LatLng myLoc = myPosMarker.getPosition();
            myPosMarker.remove();
            myPosMarker = aMap.addMarker(new MarkerOptions().position(myLoc)
                    .icons(giflist));
        }

    }
}

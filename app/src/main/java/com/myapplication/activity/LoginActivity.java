package com.myapplication.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication.R;
import com.myapplication.base.BaseActivity;
import com.myapplication.bean.UserRequest;
import com.myapplication.util.StringUtils;
import com.myapplication.util.ViewUtil;
import com.myapplication.util.net.NetBuilder;
import com.myapplication.util.net.NetCallBack;
import com.myapplication.util.net.RetrofitNet;
import com.myapplication.widget.DeleteEditText;
import com.myapplication.widget.PassVisibleCheckBox;

import butterknife.InjectView;
import retrofit2.Call;

public class LoginActivity extends BaseActivity implements TextWatcher {

    @InjectView(R.id.et_login_name)
    DeleteEditText etLoginName;
    @InjectView(R.id.et_login_pass)
    DeleteEditText etLoginPass;
    @InjectView(R.id.img_pass_visible)
    PassVisibleCheckBox imgPassVisible;
    @InjectView(R.id.tv_login_forget_pass)
    TextView tvLoginForgetPass;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.img_login_wx)
    ImageView imgLoginWX;
    @InjectView(R.id.img_login_sina)
    ImageView imgLoginSina;
    @InjectView(R.id.img_login_qq)
    ImageView imgLoginQQ;
    @InjectView(R.id.tv_login_register)
    TextView tvLoginRegister;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        etLoginName.addTextChangedListener(this);
        etLoginPass.addTextChangedListener(this);
        tvLoginForgetPass.setOnClickListener(this);
        ViewUtil.addTouchColor(btnLogin, R.color.appBarDark, R.color.appBar);
        imgPassVisible.setPassVisible(etLoginPass);
        btnLogin.setEnabled(false);
        btnLogin.setTextScaleX(1.2f);
        btnLogin.setOnClickListener(this);
        imgLoginWX.setOnClickListener(this);
        imgLoginSina.setOnClickListener(this);
        imgLoginQQ.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
        imgPassVisible.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String name = etLoginName.getText().toString().trim();
                String pass = etLoginPass.getText().toString().trim();
                if (StringUtils.isEmpty(name)) {
                    return;
                }
                if (StringUtils.isEmpty(pass)) {
                    return;
                }
                NetBuilder netBuilder = new NetBuilder.Builder().url("http://58.215.50.61:19080/").callback(new NetCallBack() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onErrorResponse(Exception e) {

                    }
                }).build();
                RetrofitNet retrofitNet = new RetrofitNet(netBuilder);
                UserRequest userRequest = new UserRequest();
                userRequest.cmd = "signin";
                UserRequest.ParamsBean paramsBean = new UserRequest.ParamsBean();
                paramsBean.appVersion = "1.0.3";
                paramsBean.imei = "860887038344759";
                paramsBean.imsi = "460018051209707";
                paramsBean.mapType = "google";
                paramsBean.sdk = "21";
                paramsBean.ua = "Redmi Note 3";
                paramsBean.sdk = "1.0.3";
                paramsBean.userName = name;
                paramsBean.password = pass;
                userRequest.params = paramsBean;

                Call userCall = retrofitNet.getService().login(userRequest);
                retrofitNet.addToRequestQueue(userCall);
                break;
            case R.id.tv_login_forget_pass:

                break;
            case R.id.tv_login_register:

                break;
            case R.id.img_login_wx:

                break;
            case R.id.img_login_sina:

                break;
            case R.id.img_login_qq:

                break;
            case R.id.img_pass_visible:

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String userName = etLoginName.getText().toString().trim();
        String userPass = etLoginPass.getText().toString().trim();
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPass)) {
            btnLogin.setBackgroundColor(getResources().getColor(
                    R.color.appBar));
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setBackgroundColor(getResources().getColor(
                    R.color.white));
            btnLogin.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}

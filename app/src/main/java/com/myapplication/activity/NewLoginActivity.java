package com.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication.R;
import com.myapplication.base.BaseActivity;
import com.myapplication.ui.crop.Crop;
import com.myapplication.widget.DeleteEditText;

import java.io.File;

import butterknife.InjectView;

public class NewLoginActivity extends BaseActivity implements TextWatcher {

    @InjectView(R.id.et_login_name)
    DeleteEditText etLoginName;
    @InjectView(R.id.et_login_pass)
    DeleteEditText etLoginPass;
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

    @InjectView(R.id.result_image)
    ImageView resultView;
    @InjectView(R.id.btn_picture)
    Button btnPicture;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        etLoginName.addTextChangedListener(this);
        etLoginPass.addTextChangedListener(this);
        tvLoginForgetPass.setOnClickListener(this);
        btnLogin.setOnTouchListener(this);
        btnLogin.setEnabled(false);
        btnLogin.setTextScaleX(1.2f);
        imgLoginWX.setOnClickListener(this);
        imgLoginSina.setOnClickListener(this);
        imgLoginQQ.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resultView.setImageDrawable(null);
//                Crop.pickImage(LoginActivity.this);

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

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
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnLogin.setBackgroundColor(getResources().getColor(
                            R.color.deep_red));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    btnLogin.setBackgroundColor(getResources().getColor(
                            R.color.red));
                }
                break;
        }

        return false;
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
                    R.color.red));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

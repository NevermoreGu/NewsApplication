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
import java.lang.reflect.Method;

import butterknife.InjectView;

public class LoginActivity extends BaseActivity implements TextWatcher {

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

                // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                PendingIntent pendingIntent2 = PendingIntent.getActivity(LoginActivity.this, 0,
//                        new Intent(LoginActivity.this, MainActivity.class), 0);
//                Notification notify2 = new Notification.Builder(LoginActivity.this)
//                        .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
//                                // icon)
//                        .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
//                                // bar上显示的提示文字
//                        .setContentTitle("Notification Title")// 设置在下拉status
//                                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
//                        .setContentText("This is the notification message")// TextView中显示的详细内容
//                        .setContentIntent(pendingIntent2) // 关联PendingIntent
//                        .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
//                        .getNotification(); // 需要注意build()是在API level
//                // 16及之后增加的，在API11中可以使用getNotificatin()来代替
//                notify2.flags |= Notification.FLAG_AUTO_CANCEL;
//                manager.notify(1, notify2);
//
//                Class<?> notify = notify2.getClass();
//                String methodName = "getNotificationArrayFromBundle";
//                try {
//                    Method method = notify.getDeclaredMethod(methodName, getParamTypes(notify, methodName));
//                    method.setAccessible(true);
//                    Notification[] ss = (Notification[]) method.invoke(notify2, getParamTypes(notify, methodName));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(LoginActivity.this, MyPositionActivity.class);
                startActivity(intent);

            }
        });
    }


    /**
     * 获取参数类型，返回值保存在Class[]中
     */
    public Class[] getParamTypes(Class cls, String mName) {
        Class[] cs = null;

        /*
         * Note: 由于我们一般通过反射机制调用的方法，是非public方法
         * 所以在此处使用了getDeclaredMethods()方法
         */
        Method[] mtd = cls.getDeclaredMethods();
        for (int i = 0; i < mtd.length; i++) {
            if (!mtd[i].getName().equals(mName)) {    // 不是我们需要的参数，则进入下一次循环
                continue;
            }

            cs = mtd[i].getParameterTypes();
        }
        return cs;
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

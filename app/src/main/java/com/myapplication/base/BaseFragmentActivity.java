package com.myapplication.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.myapplication.R;
import com.myapplication.interf.BaseViewInterface;
import com.myapplication.interf.DialogControl;
import com.myapplication.ui.DialogHelp;
import com.myapplication.util.TDevice;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends FragmentActivity implements
        DialogControl, View.OnClickListener, BaseViewInterface, View.OnTouchListener {

    private boolean _isVisible;
    private ProgressDialog _waitDialog;

    protected LayoutInflater mInflater;

    protected void onBeforeSetContentLayout() {
    }

    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        mInflater = getLayoutInflater();
        ButterKnife.inject(this);
        init(savedInstanceState);
        initView();
        initData();
        _isVisible = true;
    }

    protected View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected void init(Bundle savedInstanceState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //通过包名类名启动activity
    protected void openActivity(Class<?> activityClass) {
        openActivity(activityClass, null);
    }

    protected void openActivity(Class<?> activityClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void openActivityForResult(Class<?> activityClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, activityClass);
        startActivityForResult(intent, requestCode);
    }

    //通过action启动activity
    protected void openActivity(String action) {
        openActivity(action, null);
    }

    protected void openActivity(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void openActivityForResult(String action, int requestCode) {
        Intent intent = new Intent(action);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public ProgressDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelp.getWaitDialog(this, message);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TDevice.hideSoftKeyboard(getCurrentFocus());
        ButterKnife.reset(this);
    }
}

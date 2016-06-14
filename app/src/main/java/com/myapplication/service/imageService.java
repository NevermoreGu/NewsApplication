package com.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.myapplication.Image;
import com.myapplication.ImageAidlInterface;
import com.myapplication.bean.FolderInfo;
import com.myapplication.bean.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class imageService extends Service {

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private boolean hasFolderGened = false;

    private ArrayList<FolderInfo> mResultFolder = new ArrayList<>();// 文件夹数据

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE};

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(imageService.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(imageService.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {

                List<ImageInfo> imageInfos = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {

                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        boolean show_flag = size > 1024 * 10; //是否大于10K
                        ImageInfo imageInfo = new ImageInfo(path, name, dateTime);
                        if (show_flag) {
                            imageInfos.add(imageInfo);
                        }

                        if (!hasFolderGened && show_flag) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            FolderInfo folderInfo = new FolderInfo();
                            folderInfo.name = folderFile.getName();
                            folderInfo.path = folderFile.getAbsolutePath();
                            folderInfo.cover = imageInfo;
                            if (!mResultFolder.contains(folderInfo)) {
                                List<ImageInfo> imageList = new ArrayList<ImageInfo>();
                                imageList.add(imageInfo);
                                folderInfo.imageInfos = imageList;
                                mResultFolder.add(folderInfo);
                            } else {
                                // 更新
                                FolderInfo f = mResultFolder.get(mResultFolder.indexOf(folderInfo));
                                f.imageInfos.add(imageInfo);
                            }
                        }

                    } while (data.moveToNext());

//                    mImageAdapter.setData(imageInfos);
//
//                    // 设定默认选择
//                    if (resultList != null && resultList.size() > 0) {
//                        mImageAdapter.setSelectedList(resultList);
//                    }
//
//                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Cursor data = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] + " DESC");


    }

    ImageAidlInterface.Stub mBinder = new ImageAidlInterface.Stub() {
        @Override
        public List<Image> getImage(int type) throws RemoteException {
            return null;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void getImageBitmap() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });

    }
}

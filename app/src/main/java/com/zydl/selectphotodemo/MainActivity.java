package com.zydl.selectphotodemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.coder.zzq.smartshow.dialog.LoadingDialog;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private ImageGridViewAdapter mAdapter;
    private ArrayList<Bitmap> mBitmaps;//GlidView显示的bitmap
    private ArrayList<String> mImgPaths;//选择的图片的路径
    private ArrayList<File> mFiles;//压缩后的文件，上传时上传压缩后的文件就行

    private Context mContext;
    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog().large().withMsg(true);
        }
        getPermission();
        mGridView = findViewById(R.id.gridViewImg);
        initGridView();
        initListener();
    }

    private void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mBitmaps.size() - 1) {//点击了加号
                    selectPhoto();
                } else {//点击了前边的图片
                    showPhoto();
                }
            }
        });
        findViewById(R.id.btnUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImg();//压缩
            }
        });
    }

    private void compressImg() {
        mLoadingDialog.message("正在压缩图片").showInActivity((Activity) mContext);
        mFiles.clear();
        Luban.with(mContext)
                .load(mImgPaths)
                .ignoreBy(100)//过滤小于100kb 的文件
                .setTargetDir(PathUtil.getCachePicPath(mContext))//压缩后文件存放路径，
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.e("test", "压缩图片onStart");
                    }

                    @Override
                    public void onSuccess(File file) {
                        mFiles.add(file);
                        Log.e("test", "压缩图片onSuccess:" + mFiles.size());
                        if (mFiles.size() == mImgPaths.size()) {
                            mLoadingDialog.dismiss();
                            uploadEvent();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("test", "压缩图片onError" + e.toString());

                    }
                }).launch();
    }

    private void uploadEvent() {
        mLoadingDialog.message("正在上传").showInActivity((Activity) mContext);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.dismiss();
                        int result = (int) (Math.random() * 10);//随机生成0-10的数，模拟上报成功或者失败
                        if (result % 2 == 0) {
                            SmartToast.success("上报成功");
                            //删除压缩后的文件
                            deleteAllFiles(new File(PathUtil.getCachePicPath(mContext)));
                        } else {
                            SmartToast.fail("上报失败");
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 删除文件夹下所有文件
     * @param root
     */
    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    private void showPhoto() {

    }

    private void selectPhoto() {
        Album.image(mContext)
                .multipleChoice()
                .columnCount(3)//相册中列数
                .selectCount(5)//最多选择图片数
                .camera(true)//是否显示相机
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        for (AlbumFile albumFile : result) {
                            mImgPaths.add(albumFile.getPath());
                            mBitmaps.add(BitmapFactory.decodeFile(albumFile.getPath()));
                            //更换顺序，加号始终在最后一个
                            Collections.swap(mBitmaps, mBitmaps.size() - 1, mBitmaps.size() - 2);
                        }
                        mAdapter.notifyDataSetChanged(mBitmaps);
                    }
                }).start();
    }

    private void initGridView() {
        mBitmaps = new ArrayList<>();
        mImgPaths = new ArrayList<>();
        mFiles = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.add_photo);
        mBitmaps.add(bitmap);
        mAdapter = new ImageGridViewAdapter(mBitmaps, mContext);
        mGridView.setAdapter(mAdapter);
    }

    private void getPermission() {
        AndPermission.with(mContext).runtime().permission(Permission.Group.STORAGE, Permission.Group.CAMERA).start();
    }
}

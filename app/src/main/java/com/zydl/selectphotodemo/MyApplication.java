package com.zydl.selectphotodemo;

import android.app.Application;

import com.coder.zzq.smartshow.core.SmartShow;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

/**
 * Created by Sch.
 * Date: 2020/5/14
 * description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SmartShow.init(this);
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .build()
        );
    }
}

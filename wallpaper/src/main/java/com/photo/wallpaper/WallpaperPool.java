package com.photo.wallpaper;

import android.content.Context;
import android.os.Environment;
import android.util.ArrayMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class WallpaperPool {

    private Context context;

    private File storeDir;

    private File store2FileDir;


    private ArrayMap<String, Disposable> missTask = new ArrayMap<>();

    public WallpaperPool(Context context) {
        this.context = context;
        storeDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        store2FileDir = new File(storeDir.getPath() + File.separator + "Wallpaper_video");
        if(!store2FileDir.exists()){
            store2FileDir.mkdirs();
        }
    }

    private OkHttpClient builder = new  OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .callTimeout(15, TimeUnit.SECONDS)
            .build();


    public void download(final String url,final CallBack callBack){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                int index = url.lastIndexOf("/");
                String filename =  url.substring(index);
                File file = new File(store2FileDir.getPath() + File.separator + filename);
                if(file.exists()){
                    file.delete();
                    file.createNewFile();
                }else {
                    file.createNewFile();
                }
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                ResponseBody ins = builder.newCall(request).execute().body();

                BufferedInputStream bis = new BufferedInputStream(ins.source().inputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                int size = 0;
                byte[] buffer=new byte[1024 * 10];
                while((size=bis.read(buffer))!=-1){
                    bos.write(buffer, 0, size);
                }
                bos.flush();
                bis.close();
                bos.close();
                emitter.onNext(file);
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                callBack.onSuccess(file);
            }
        }).subscribe();

        missTask.put(url,disposable);
    }

    public void stopDownLoad(String url){
        Disposable disposable =   missTask.get(url);
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
            missTask.remove(url);
        }
    }


}


package com.whitehedge.multithreaddemo;


import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;


public class MainActivity extends AppCompatActivity {
    Observable<String> observable;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data loading");
        observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                Log.v("MainActivity", Thread.currentThread().getName());
                observableEmitter.onNext("Mahesh");
                Thread.sleep(1000);
                observableEmitter.onNext("Pramod");
                Thread.sleep(1000);
                observableEmitter.onNext("Chakkarwar");
                Thread.sleep(1000);
                observableEmitter.onComplete();
            }
        })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void handleOnClick(View view) {
//        observable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.v("MainActivity","Accept:-"+s);
//                Log.v("MainActivity",Thread.currentThread().getName());
//            }
//
//        });

        progressDialog.show();
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.v("MainActivity", "" + value);
                progressDialog.setMessage(value);
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
            }

            @Override
            public void onComplete() {
                progressDialog.dismiss();
            }
        });
    }
}

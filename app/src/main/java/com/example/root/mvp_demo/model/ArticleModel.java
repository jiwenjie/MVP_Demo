package com.example.root.mvp_demo.model;

import com.example.root.mvp_demo.bean.ArticleData;
import com.example.root.mvp_demo.utils.RetrofitManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author:Jiwenjie
 * email:Jiwenjie97@gmail.com
 * time:2018/10/21
 * desc: MVP 中的 model
 * version:1.0
 */
public class ArticleModel {

    // 获取首页技术文章的 model
    public Observable<ArticleData> getArtData(int page) {
        return RetrofitManager.getApiService().getArticle(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

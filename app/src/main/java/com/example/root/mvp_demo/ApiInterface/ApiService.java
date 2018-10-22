package com.example.root.mvp_demo.ApiInterface;

import com.example.root.mvp_demo.bean.ArticleData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * author:Jiwenjie
 * email:Jiwenjie97@gmail.com
 * time:2018/10/21
 * desc: 有关 Retrofit 和 RxJava 结合的功能接口
 * version:1.0
 */
public interface ApiService {
    @GET("/article/list/{page}/json")
    Observable<ArticleData> getArticle(@Path("page") int page);
}

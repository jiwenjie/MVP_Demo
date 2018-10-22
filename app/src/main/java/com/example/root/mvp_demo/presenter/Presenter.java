package com.example.root.mvp_demo.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.root.mvp_demo.model.ArticleModel;
import com.example.root.mvp_demo.bean.ArticleData;
import com.example.root.mvp_demo.contact.ArtContact;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * author:Jiwenjie
 * email:Jiwenjie97@gmail.com
 * time:2018/10/21
 * desc: 实际的 Presenter，在这里需要持有 View 和 Model 的引用，
 * 通过 model 获取数据，把获取的数据显示在 Model 中
 * version:1.0
 */
public class Presenter implements ArtContact.ArtPresenter {

    private Context mContext;
    private ArticleModel mModel;
    private ArtContact.ArtView mView;

    // 防止内存泄漏部分
    protected Disposable disposable;

    // 初始化部分
    public Presenter(Context context, ArtContact.ArtView view) {
        this.mContext = context;
        this.mView = view;
        mModel = new ArticleModel();
    }

    @Override
    public void requestData() {
        destroy();
        disposable = mModel.getArtData(0)
                .subscribe(new Consumer<ArticleData>() {
                    @Override
                    public void accept(ArticleData articleData) throws Exception {
                        Toast.makeText(mContext, "数据获取成功", Toast.LENGTH_SHORT).show();
                        mView.setData(articleData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void start() {
        Log.i("Presenter", "开始获取数据");
    }

    @Override
    public void destroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}

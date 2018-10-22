package com.example.root.mvp_demo.contact;

import com.example.root.mvp_demo.base.BasePresenter;
import com.example.root.mvp_demo.base.BaseView;
import com.example.root.mvp_demo.bean.ArticleData;

/**
 * author:Jiwenjie
 * email:Jiwenjie97@gmail.com
 * time:2018/10/21
 * desc:首页文章的契约类
 * version:1.0
 */
public class ArtContact {

    public interface ArtView extends BaseView {
        void setData(ArticleData data);
    }

    public interface ArtPresenter extends BasePresenter {
        void requestData();
    }

}







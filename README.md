说实话，MVP 这种模式或者说设计思想也已经出来很久了，现在最新的使用的是 MVVM 设计模式，不断对于萌新来说，还是需要一步一步的向前走。毕竟，人不能一口吃成一个大胖子是吧。

本人大四，目前正在实习，其实接触研究 MVP 也有很久了，期间找过网上的老哥们帮忙写过 MVPDemo，也从 GitHub 上看过很多项目源码。也看过他们的设计思想等。不过一直对于 MVP 中的实现引用啊，数据怎么传递过去的很迷。也看过 Google 的官方 Demo，看完后感觉还不如其他的简单 Demo 容易明白。所以我这次结合自己写的小 Demo 和理解，完整的讲述下我眼中的 MVP。希望能帮到大家，如果有什么错误还希望各位大佬多多指教。


其实对于 MVP 的好处我也不多和大家说了，说来说去也就那么几点：

1，松耦合

2，方便单元测试

3，代码整洁，容易修改

4，等等。。


ApiService 目录：里面放了请求接口的方法

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
    

这里大家应该都可以理解，因为我请求网络使用的是 RxJava + Retrofit，所以这里的开头是 Observable，如果只是单纯的 Retrofit 的话那就改成 Call 就可以了，其他不必变化。

tips：这里有一个注意点就是 @Path 和 @Query 的区别。前者是在 URL 中间添加参数，上面代码有表示，需要用 @Path 代替的参数在链接中使用 { } 包裹起来，注意名称需要对应；  而后者是属于拼接参数，即 URL 输入完成后，在最后通过 ？和  & 连接起来的参数。相信我说的很明白了，不能明白的话就去看下你们经常写的 URL 格式你也就会明白。



base 目录：这里存放了 BaseView 和 BasePresenter，这是大家的习惯啦，我正常会把 BaseActivity，BaseFragment 等都放在 Base 目录下

    package com.example.root.mvp_demo.base;
    
    /**
     * author:Jiwenjie
     * email:Jiwenjie97@gmail.com
     * time:2018/10/21
     * desc:
     * version:1.0
     */
    public interface BasePresenter {
        void start();
        void destroy();
    }
    

BaseView

    package com.example.root.mvp_demo.base;
    
    /**
     * author:Jiwenjie
     * email:Jiwenjie97@gmail.com
     * time:2018/10/21
     * desc:
     * version:1.0
     */
    public interface BaseView {
    
    }
    

我这里给 BaseView 定义了空实现，给 Presenter 定义了 onStart 和 onDestroy 方法。这些不用多说，相信大家都能够看懂。



bean 目录：存放实体类



contact 目录：契约目录，这里存放契约类，一般 APP 的一个界面就是一个契约类，这也是 Google 的推荐写法

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

顾名思义，就像签订契约，哪一个 View 和 哪一个 Presenter 相对应起来。注意 View 很重要，它是纽带。在具体使用的时候就需要 Activity 或者 Fragment 实现该 View，然后把参数传递给 Presenter。在Presenter 中做操作。



utils 目录：再看 Model 目录前先看 utils 目录，这里封装了网络请求方法

    package com.example.root.mvp_demo.utils;
    
    import com.example.root.mvp_demo.ApiInterface.ApiService;
    
    import java.util.concurrent.TimeUnit;
    
    import okhttp3.OkHttpClient;
    import okhttp3.logging.HttpLoggingInterceptor;
    import retrofit2.CallAdapter;
    import retrofit2.Converter;
    import retrofit2.Retrofit;
    import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
    import retrofit2.converter.gson.GsonConverterFactory;
    
    /**
     * author:Jiwenjie
     * email:Jiwenjie97@gmail.com
     * time:2018/10/21
     * desc: 有关网络请求的 Util
     * version:1.0
     */
    public class RetrofitManager {
    
        private static ApiService apiService;
    
        private static OkHttpClient okHttpClient;
        private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
        private static CallAdapter.Factory rxJaveCallAdapterFactory = RxJava2CallAdapterFactory.create();
        private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    
        private static OkHttpClient getClient() {
            // 可以设置请求过滤的水平，body，basic，headers
    
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    
            return okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)    // 日志，所有请求响应都看到
                    .connectTimeout(60L, TimeUnit.SECONDS)
                    .readTimeout(60L, TimeUnit.SECONDS)
                    .writeTimeout(60L, TimeUnit.SECONDS)
                    .build();
        }
    
        public static ApiService getApiService() {
            if (apiService == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .client(getClient())
                        .baseUrl("http://www.wanandroid.com/")
                        .addConverterFactory(gsonConverterFactory)
                        .addCallAdapterFactory(rxJaveCallAdapterFactory)
                        .build();
    
                apiService = retrofit.create(ApiService.class);
            }
            return apiService;
        }
    }
    

相信对于 Retrofit 有些了解的童鞋都知道其实 Retrofit 的实现就是 OkHttp 外面包裹了一层能够和 RxJava 连接的部分而已，所以这里的 Client 使用的是 okHttpClient。接口使用的是鸿洋大神的 玩 Android 接口。话说鸿洋老哥去头条工作了，，，大写的羡慕。都知道头条待遇好要求高。



大家可以根据我写的在做扩展。PS：其实这里有关获取 APiService 就可以修改扩展，使用 泛型和 Class 来处理代替。这样在 Model 调用的时候直接传入参数就可以。如果照我这样写，每个接口都要实现一个方法太多重复了。



Model 目录：存放 Model 用来具体操作数据的部分

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
    

其实不论在 MVP 或者 MVC 模式中，Model 都是在操作数据的部分，这一点万年不变。MVVM 我还没看，不太了解，只知道好像是增加了一个 ViewModel，所以应该也是一样。

这上面从 22 行到 24 行代码是 RxJava 的功能体现，链式调用，可以说是牛逼了。他的意思是被观察者获取数据请求网络的时候在 IO 线程，所以被观察者解绑的时候也在 IO 线程。而观察者运行在 UI 线程。这里就体现了线程一行代码切换的强大。随便切，且不过来算我输，，，

如果对于这些不了解的童鞋可以去网上搜一搜，

[01]: http://gank.io/post/560e15be2dca930e00da1083	"RxJava 详解"

这篇文章写的很好。



Presenter 目录：存放具体的 Prsenter 实现

大家都知道在 Activity 或者 Fragment 中需要持有 Presenter 的引用，在 Presenter 中对数据和 View 进行操作，所以我们还需要定义一个类来实现 Presenter 接口。

    



这里我都写了注释，相信大家都能看懂，如果不明白的话可以私信或者 Google，如果实在不行的话，，，百度也行。注意这里我传递参数是 0，固定写死的，大家实际使用的时候需要修改一下，改成变量的形式就 OK 了。



MainActivity

重点来了，重点来了，前面说的再多都是铺垫，还要具体使用才行。先看布局，没啥好说，一个 TextView 和一个 Button。

    

这里给大家推荐下约束布局，真的很好用，而且上网搜一搜就能会。真的是嵌套好几层的页面他就一层搞定。对于优化性能有要求的话强烈推荐。不过没要求的话也就算了，因为写起来还有有些费事的，起码 Id 你就要想好多不重复的才行。



MainActivity 源码

    package com.example.root.mvp_demo;
    
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    
    import com.example.root.mvp_demo.bean.ArticleData;
    import com.example.root.mvp_demo.contact.ArtContact;
    import com.example.root.mvp_demo.presenter.Presenter;
    
    public class MainActivity extends AppCompatActivity implements ArtContact.ArtView {
    
        private Button btn_start;
        private TextView tv_content;
    
        private Presenter presenter;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    
            initView();
    
            initEvent();
        }
    
        private void initView() {
            btn_start = findViewById(R.id.btn_start);
            tv_content = findViewById(R.id.tv_content);
            presenter = new Presenter(getApplicationContext(), this);
        }
    
        private void initEvent() {
            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.start();
                    presenter.requestData();
                }
            });
        }
    
        @Override
        public void setData(ArticleData data) {
            tv_content.setText(data.getData().getDatas().toString());
        }
    
        @Override
        protected void onDestroy() {
            super.onDestroy();
            presenter.destroy();
        }
    }
    

实现了契约类中 View 的接口，重写了方法，把持了 Presenter 的引用。初始化 Presenter 的时候把 View

 作为参数传递了过去。大致流程就是这样，也许真的是会了不难吧。我一开始总是不明白，现在怎么想都明白。。。希望这篇文章能帮到大家：

简书https://www.jianshu.com/p/b144c9479203 


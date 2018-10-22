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

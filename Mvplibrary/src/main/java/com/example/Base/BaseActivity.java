package com.example.Base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.IBase.IBaseView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Base
 * 创建时间: 2020/11/24 8:38
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView {


    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setUpLayout());
        bind = ButterKnife.bind(this);
        setUpView();
        setUpData();
    }

    /**
     * 行为：设置布局资源
     * @return 布局文件
     */
    protected abstract int setUpLayout();

    /**
     * 行为：初始化布局
     */
    protected abstract void setUpView();

    /**
     * 行为：初始化数据，网络请求
     */
    protected abstract void setUpData();

    @Override
    public void tips(String tip) {

    }

    /**
     * 扩展功能，进度条
     * @param visible
     */
    @Override
    public void loading(int visible) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null){
            bind.unbind();
        }
    }
}

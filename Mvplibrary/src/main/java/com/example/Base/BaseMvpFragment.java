package com.example.Base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.IBase.IBaseModel;
import com.example.IBase.IBaseView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Base
 * 创建时间: 2020/11/24 14:34
 */
public abstract class BaseMvpFragment<V extends IBaseView,P extends BasePresenter<V,M>,M extends IBaseModel> extends RxFragment {

    public P mPresenter;
    public Activity activity;
    public Context context;
    private View rootView;
    private Unbinder bind;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity= (Activity) context;
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(setUpLayout(),container,false);
        }
        return rootView;
    }

    /**
     * 行为：初始化fragment布局资源
     * @return fragment布局资源
     */
    protected abstract int setUpLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter!=null){
            mPresenter.onBindView((V)this);
        }
        bind = ButterKnife.bind(this, view);
        setUpView(view);
        setUpData();
    }

    /**
     * 行为:初始化页面数据
     */
    protected abstract void setUpData();

    /**
     * 行为：初始化页面布局
     * @param view 布局根对象
     */
    protected abstract void setUpView(View view);

    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.unBindView();
        }
        if(rootView!=null && rootView.getParent()!=null){
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }
        if (bind != null){
            bind.unbind();
        }
    }
}

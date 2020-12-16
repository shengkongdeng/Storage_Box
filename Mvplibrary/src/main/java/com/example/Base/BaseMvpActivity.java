package com.example.Base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.IBase.IBaseModel;
import com.example.IBase.IBaseView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Base
 * 创建时间: 2020/11/24 8:45
 */
public abstract class BaseMvpActivity<V extends IBaseView,P extends BasePresenter<V,M>,M extends IBaseModel> extends RxAppCompatActivity implements IBaseView {

    protected P mPresenter;
    private Unbinder bind;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setUpLayout());
        bind = ButterKnife.bind(this);
        mPresenter = createPresenter();
        if(mPresenter != null){
            mPresenter.onBindView((V)this);
        }
        setUpView();
        setUpData();
    }

    /**
     * 行为：创建并绑定P层对象
     * @return P层的对象
     */
    protected abstract P createPresenter();

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

    /**
     * 行为：解绑<P>防止内存泄露
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.unBindView();
        }
        if (bind != null){
            bind.unbind();
        }
    }

    @Override
    public void tips(String tip) {

    }

    /**
     * 行为:扩展功能，进度条
     * @param visible
     */
    @Override
    public void loading(int visible) {

    }

}

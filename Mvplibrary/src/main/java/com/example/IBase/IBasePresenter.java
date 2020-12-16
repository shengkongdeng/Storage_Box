package com.example.IBase;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Presenter
 * 创建时间: 2020/11/24 8:47
 */
public interface IBasePresenter<V extends IBaseView> {
    void onBindView(V v);
    void unBindView();
    void onSuccess(Object data);
    void onError(String msg);
}

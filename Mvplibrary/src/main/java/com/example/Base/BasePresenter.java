package com.example.Base;

import com.example.IBase.IBaseModel;
import com.example.IBase.IBasePresenter;
import com.example.IBase.IBaseView;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: ToyBox
 * 包名: com.example.Base
 * 创建时间: 2020/11/24 8:53
 */
public abstract class BasePresenter<V extends IBaseView,M extends IBaseModel> implements IBasePresenter<V> {


    private WeakReference<V> mWeakReferenceView;
    private WeakReference<M> mWeakReferenceModel;

    private V mView;
    private M mModel;

    /**
     * 绑定<V>层对象以及<M>层对象
     * @param v <V>层的对象
     */
    @Override
    public void onBindView(V v) {
        mWeakReferenceView=new WeakReference<V>(v);
        if(v!=null&&mWeakReferenceView!=null){
            mView=mWeakReferenceView.get();
        }
        if(createModel()!=null){
            mWeakReferenceModel=new WeakReference<M>(createModel());
            mModel=mWeakReferenceModel.get();
        }
    }

    /**
     * 解绑<V>层以及<M>层对象
     */
    @Override
    public void unBindView() {
        if(mWeakReferenceView!=null){
            mWeakReferenceView.clear();
        }
        if(mWeakReferenceModel!=null){
            mWeakReferenceModel.clear();
        }
        if(mView!=null){
            mView=null;
        }
        if(mModel!=null){
            mModel=null;
        }
    }

    /**
     * 使用反射通过<M>动态创建Model层对象
     * @return Model层对象
     */
    private M createModel(){
        M mModel=null;
        Class<? extends BasePresenter> aClass = this.getClass();
        ParameterizedType genericSuperclass = (ParameterizedType) aClass.getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        Class actualTypeArgument = (Class) actualTypeArguments[1];
        if(actualTypeArgument!=null){
            try {
                mModel= (M) actualTypeArgument.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mModel;
    }

    /**
     * 获取View生命周期
     * @return
     */
    public LifecycleProvider getLifecycleProvider(){
        if(mView!=null){
            return (LifecycleProvider) mView;
        }
        return null;
    }

    /**
     * <V>访问接口
     * @return <V>
     */
    public V getView() {
        return mView;
    }

    /**
     * <M>访问接口
     * @return <M>
     */
    public M getModel() {
        return mModel;
    }

}

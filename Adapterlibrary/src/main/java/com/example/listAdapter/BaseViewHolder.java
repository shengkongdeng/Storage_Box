package com.example.listAdapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utils.ImageUtils;

/**
 * 创建者: Administrator
 * 项目名: ChildrenSongsTap
 * 包名: com.example.adapter
 * 创建时间: 2020/11/11 16:02
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private View rootView;
    //存储View的集合
    private SparseArray<View> mViews = new SparseArray<>();
    private Context context;

    public BaseViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        rootView = itemView;
        this.context = context;
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 在获取到view之前要先判断传进来的id于集合中是否存在
     * 如果存在则从集合中获取view如果不存在则通过id寻找view并将其加入集合
     *
     * @param viewId 布局ID
     * @param <T>    布局类型
     * @return view
     */
    public <T extends View> T getView(int viewId) {
        T view = null;
        if (mViews.get(viewId) != null) {
            view = (T) mViews.get(viewId);
        } else {
            view = rootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setText(int viewId, final String text, final OnViewClickListener<String> adapterOnItemClickListener) {
        TextView textView = getView(viewId);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterOnItemClickListener.OnItemClick(text);
            }
        });
        return this;
    }

    public BaseViewHolder setRecyclerView(int viewId, RecyclerView.LayoutManager layoutManager, BaseAdapter adapter, final OnViewClickListener adapterOnItemClickListener) {
        RecyclerView view = getView(viewId);
        view.setLayoutManager(layoutManager);
        view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setAdapterOnItemClickListener(new BaseAdapter.AdapterOnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                adapterOnItemClickListener.OnItemClick(position);
            }
        });
        return this;
    }

    public BaseViewHolder setImage(int viewId, String url, int rounded) {
        ImageView imageView = getView(viewId);
        ImageUtils.loadImage(context, url, imageView, rounded);
        return this;
    }

    public BaseViewHolder setImage(int viewId, String drawable) {
        ImageView imageView = getView(viewId);
        int i = Integer.parseInt(drawable);
        imageView.getDrawable().setLevel(i);
        return this;
    }

    public interface OnViewClickListener<T> {
        void OnItemClick(T t);
    }

}

package com.example.listAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 创建者: Administrator
 * 项目名: ChildrenSongsTap
 * 包名: com.example.adapter
 * 创建时间: 2020/11/11 16:01
 */
public abstract class BaseAdapter<D> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<D> data;
    protected Context context;
    protected LayoutInflater inflater;

    private int layoutId;

    public BaseAdapter(List<D> datas, Context context, int layoutId) {
        this.data = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
    }

    //追加数据
    public void setData(List<D> pData) {
        this.data = pData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(layoutId, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
        bindData(holder, position);
        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(this.getClass().getName(), "onClick:");
                adapterOnItemClickListener.OnItemClick(position);
            }
        });
    }


    public abstract void bindData(BaseViewHolder baseViewHolder, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface AdapterOnItemClickListener {
        void OnItemClick(int position);
    }

    private AdapterOnItemClickListener adapterOnItemClickListener;

    public void setAdapterOnItemClickListener(AdapterOnItemClickListener adapterOnItemClickListener) {
        this.adapterOnItemClickListener = adapterOnItemClickListener;
    }
}

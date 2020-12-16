package com.example.listAdapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 创建者: Administrator
 * 项目名: ChildrenSongsTap
 * 包名: com.example.listAdapter
 * 创建时间: 2020/11/11 16:28
 */
public abstract class MultipleAdapter<D> extends BaseAdapter<D> {
    private CommonType<D> commonType;
    private int commonTypeType;

    public MultipleAdapter(List<D> datas, Context context, int layoutId, CommonType<D> commonType) {
        super(datas, context, layoutId);
        if (layoutId == 0) {
            this.commonType = commonType;
        }
    }

    @Override
    public int getItemViewType(int position) {
        commonTypeType = commonType.getType(position, data.get(position));
        return commonTypeType;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = commonType.getTypeLayoutId(commonTypeType);
        return new BaseViewHolder(inflater.inflate(layoutId, parent, false), context);
    }
}

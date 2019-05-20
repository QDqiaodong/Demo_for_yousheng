package com.demo.youshengdemo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * @author qiaodong
 * @date 18-5-17
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void pause();
}

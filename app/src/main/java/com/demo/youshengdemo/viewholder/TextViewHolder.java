package com.demo.youshengdemo.viewholder;

import android.view.View;
import android.widget.TextView;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.base.BaseViewHolder;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1811:42 AM
 * <p>
 * desc   :
 */
public class TextViewHolder extends BaseViewHolder {

    public TextView mTv;

    public TextViewHolder(View itemView) {
        super(itemView);
        mTv = (TextView) itemView.findViewById(R.id.tv);
    }

    @Override
    public void pause() {

    }
}

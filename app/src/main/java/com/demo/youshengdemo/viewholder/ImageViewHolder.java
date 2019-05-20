package com.demo.youshengdemo.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.base.BaseViewHolder;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1811:41 AM
 * <p>
 * desc   :
 */
public class ImageViewHolder extends BaseViewHolder {
    public ImageView mImageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.iv);
    }

    @Override
    public void pause() {

    }
}

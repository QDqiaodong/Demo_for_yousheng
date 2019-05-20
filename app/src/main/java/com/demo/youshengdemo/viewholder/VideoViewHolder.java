package com.demo.youshengdemo.viewholder;

import android.view.View;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.base.BaseViewHolder;
import com.demo.youshengdemo.view.PlayerVideoView;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1811:42 AM
 * <p>
 * desc   :
 */
public class VideoViewHolder extends BaseViewHolder {

    public PlayerVideoView mVideoView;

    public VideoViewHolder(View itemView) {
        super(itemView);
        mVideoView = itemView.findViewById(R.id.video_view);
    }

    @Override
    public void pause() {
    }
}

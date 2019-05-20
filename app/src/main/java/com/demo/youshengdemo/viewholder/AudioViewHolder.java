package com.demo.youshengdemo.viewholder;

import android.view.View;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.base.BaseViewHolder;
import com.demo.youshengdemo.view.PlayerAudioView;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1811:42 AM
 * <p>
 * desc   :
 */
public class AudioViewHolder extends BaseViewHolder {

    public PlayerAudioView mAudioView;

    public AudioViewHolder(View itemView) {
        super(itemView);
        mAudioView = itemView.findViewById(R.id.audio_view);
    }

    @Override
    public void pause() {
        if (mAudioView != null) {
            mAudioView.playerPause();
        }
    }

}

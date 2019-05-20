package com.demo.youshengdemo.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.demo.youshengdemo.R;

import java.io.IOException;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1812:07 PM
 * <p>
 * desc   :
 */
public class PlayerAudioView extends FrameLayout {

    private Context mContext;
    private boolean isInit = false;
    private MediaPlayer mPlayer;
    private ImageView mIvControl;
    private int mPath;
    private int curPosition = -1;

    public PlayerAudioView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PlayerAudioView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PlayerAudioView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.audio_layout, this, true);
        mIvControl = viewGroup.findViewById(R.id.iv_control);
        mIvControl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer == null) {
                    mIvControl.setImageResource(R.mipmap.start);
                }
                startPlayerOrPause();
            }
        });
    }

    //进度清空
    public void setAudioPath(@RawRes int path) {
        if (path != -1) {
            mPath = path;
            mPlayer = new MediaPlayer();
            AssetFileDescriptor file = mContext.getResources().openRawResourceFd(path);
            try {
                if (!mPlayer.isPlaying()) {
                    mPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                    file.close();
                    mPlayer.prepare();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            isInit = true;
        }
    }

    public void startPlayerOrPause() {
        if (isInit) {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mIvControl.setImageResource(R.mipmap.start);
                } else {
                    mPlayer.start();
                    mIvControl.setImageResource(R.mipmap.stop);
                }


            }
        }
    }

    public void recordCurPosition() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            curPosition = mPlayer.getCurrentPosition();
        }
    }

    public void playerPause() {
        if (isInit) {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mIvControl.setImageResource(R.mipmap.start);
                }
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Log.d("qd", "PlayerAudioView onDetachedFromWindow");
        releasePlayer();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Log.d("qd", "PlayerAudioView onAttachedToWindow curPosition=" + curPosition);
        if (mPlayer == null && mPath != -1) {
            setAudioPath(mPath);
            if (curPosition >= 0) {
                mPlayer.seekTo(curPosition);
            }
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        mIvControl.setImageResource(R.mipmap.start);
    }
}

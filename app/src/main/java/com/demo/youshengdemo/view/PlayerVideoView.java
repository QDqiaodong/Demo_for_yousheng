package com.demo.youshengdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.demo.youshengdemo.PlayerApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1812:07 PM
 * <p>
 * desc   :
 */
public class PlayerVideoView extends FrameLayout {

    private VideoView mVideoView;
    private Context mContext;
    private boolean isInit = false;
    private Uri mUri;
    private MediaController mediaController;
    private int mPath;
    private int curPosition = -1;

    public PlayerVideoView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PlayerVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PlayerVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Log.d("qd", "PlayerVideoView initView " + this);
        mContext = context;
        createVideoViewIfNeed();
    }

    private void createVideoViewIfNeed() {
        if (mVideoView == null) {
            mVideoView = new MyVideoView(PlayerApplication.getPlayerApplication());
            addView(mVideoView);
        }
    }

    //进度清空
    public void setVideoPath(@RawRes int path) {
        Log.d("qd", "PlayerVideoView setVideoPath=" + path);
        //step1
        createVideoViewIfNeed();
        if (path != -1 && mVideoView != null) {
            //step1
            mPath = path;
            String packageName = mContext.getPackageName();
            mUri = Uri.parse("android.resource://" + packageName + "/" + path);
            mVideoView.setVideoURI(mUri);
            mediaController = new MediaController(mContext);
            mVideoView.setMediaController(mediaController);
            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                        Toast.makeText(mContext, "当前手机不支持", Toast.LENGTH_SHORT).show();
                    } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                        Toast.makeText(mContext, "当前手机不支持", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            //step2
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            Bitmap initBitmap = createBitmapFromVideoPath(path, displayMetrics.widthPixels, 150);
            mVideoView.setBackground(new BitmapDrawable(initBitmap));
            mediaController.setVisibility(View.VISIBLE);
            isInit = true;

            //step3 去黑屏
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                                mVideoView.setBackgroundColor(Color.TRANSPARENT);
                            return true;
                        }
                    });
                }
            });
        }
    }

    public void startPlayer() {
        if (isInit) {
            mVideoView.requestFocus();
            mVideoView.start();
            mediaController.setVisibility(View.VISIBLE);
        }
    }

    public void hideController() {
        if (mediaController != null) {
            mediaController.hide();
        }
    }

    public void showController() {
        if (mediaController != null) {
            mediaController.show();
        }
    }

    public void pausePlayer() {
        if (isInit) {
            mVideoView.pause();
            mediaController.setVisibility(View.GONE);
        }
    }

    public void recordCurPosition() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            curPosition = mVideoView.getCurrentPosition();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
       // Log.d("qd", "PlayerVideoView onDetachedFromWindow " + this);
        if (mVideoView != null) {
            mVideoView.suspend();
            mVideoView.stopPlayback();
        }
        removeAllViews();
        mVideoView = null;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Log.d("qd", "PlayerVideoView onAttachedToWindow curPosition=" + curPosition + " mPath=" + mPath);
        if (mVideoView == null && mPath != -1) {
            setVideoPath(mPath);
            if (curPosition >= 0) {
                mVideoView.seekTo(curPosition);
                int currentPosition = mVideoView.getCurrentPosition();
                //Log.d("qd", "设置后当前的位置 currentPosition=" + currentPosition);
                //初始播放位置
                curPosition = -1;
            }
        }
    }

    public void saveToSDCard(@RawRes int path) throws Throwable {
        InputStream inStream = mContext.getResources().openRawResource(path);
        File file = new File(Environment.getExternalStorageDirectory(), "temp.mp4");
        FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
        byte[] buffer = new byte[10];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] bs = outStream.toByteArray();
        fileOutputStream.write(bs);
        outStream.close();
        inStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public Bitmap createBitmapFromVideoPath(@RawRes int path, int width, int height) {
        String pathStr = null;
        try {
            saveToSDCard(path);
            File file = new File(Environment.getExternalStorageDirectory(), "temp.mp4");
            if (file != null && file.exists()) {
                pathStr = file.getAbsolutePath();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        if (TextUtils.isEmpty(pathStr)) {
            return null;
        }

        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            retriever.setDataSource(pathStr);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            Log.d("qd", "ex.toString" + ex.toString());
        } catch (RuntimeException ex) {
            Log.d("qd", "ex.toString" + ex.toString());
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

}

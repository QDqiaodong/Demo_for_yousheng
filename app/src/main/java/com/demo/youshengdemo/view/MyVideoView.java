package com.demo.youshengdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/181:38 PM
 * <p>
 * desc   :
 */
public class MyVideoView extends VideoView {
    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
    }
}

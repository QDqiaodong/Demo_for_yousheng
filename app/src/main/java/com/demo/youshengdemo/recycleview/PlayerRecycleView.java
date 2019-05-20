package com.demo.youshengdemo.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.demo.youshengdemo.base.BaseViewHolder;
import com.demo.youshengdemo.viewholder.AudioViewHolder;
import com.demo.youshengdemo.viewholder.ImageViewHolder;
import com.demo.youshengdemo.viewholder.ScrollListViewHolder;
import com.demo.youshengdemo.viewholder.TextViewHolder;
import com.demo.youshengdemo.viewholder.VideoViewHolder;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/182:13 PM
 * <p>
 * desc   :
 */
public class PlayerRecycleView extends CommonRecyclerView {

    private Context mContext;
    private PlayerOnScrollerListener mPlayerOnScrollerListener;
    private int mDy = 0;
    private int mDx = 0;
    private boolean showSave = false;

    public PlayerRecycleView(Context context) {
        super(context);
        init(context);
    }

    public PlayerRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPlayerOnScrollerListener = new PlayerOnScrollerListener();
        this.addOnScrollListener(mPlayerOnScrollerListener);
    }

    class PlayerOnScrollerListener extends RecyclerView.OnScrollListener {
        /**
         * @param recyclerView
         * @param dx           正：左滑
         * @param dy           正：上滑动
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //step1
            mDy = mDy + dy;
            mDx = mDx + dx;

            //step2
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int orientation = layoutManager.getOrientation();
            int itemCount = layoutManager.getItemCount();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int visibleCount = lastVisibleItemPosition - firstVisibleItemPosition + 1;

            Log.d("qd", "itemCount=" + itemCount
                    + " lastVisibleItemPosition=" + lastVisibleItemPosition
                    + " firstVisibleItemPosition=" + firstVisibleItemPosition
                    + " visibleCount=" + visibleCount
                    + " dy=" + dy + " mDy=" + mDy
                    + " dx=" + dx + " mDx=" + mDx
                    + " orientation=" + orientation);

            //step3 第一个可见条目滑动的距离
            View child = recyclerView.getChildAt(firstVisibleItemPosition % visibleCount);
            int firstVisibleItemScrollDistance;
            if (orientation == LinearLayoutManager.VERTICAL) {
                firstVisibleItemScrollDistance = mDy - (firstVisibleItemPosition * child.getHeight());
            } else {
                firstVisibleItemScrollDistance = mDx - (firstVisibleItemPosition * child.getWidth());
            }

            //ste4. 最大滑动距离去保存
            int maxSaveScrollInstance;
            if (orientation == LinearLayoutManager.VERTICAL) {
                maxSaveScrollInstance = (int) (child.getHeight() * 0.95f);
            } else {
                maxSaveScrollInstance = (int) (child.getWidth() * 0.95f);
            }
            Log.d("qd", " hild.getHeight()=" + child.getHeight()
                    + " child.getWidth()=" + child.getWidth()
                    + " firstVisibleItemScrollDistance=" + firstVisibleItemScrollDistance
                    + " maxSaveScrollInstance=" + maxSaveScrollInstance);
            //滑动距离大于整个条目的0.95，并且上下滑动时是向上滑动，左右滑动时是向左滑动。
            if (firstVisibleItemScrollDistance > maxSaveScrollInstance && (dy > 0 || dx > 0)) {
                showSave = true;
            } else {
                showSave = false;
            }

            //step5  隐藏video的控制栏
            for (int i = 0; i < visibleCount; i++) {
                int index = firstVisibleItemPosition + i;
                BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                if (viewHolder instanceof VideoViewHolder) {
                    VideoViewHolder holder = (VideoViewHolder) viewHolder;
                    holder.mVideoView.hideController();

                    if (showSave && i == 0) {
                        holder.mVideoView.recordCurPosition();
                    }
                } else if (viewHolder instanceof ScrollListViewHolder) {
                    ScrollListViewHolder scrollListViewHolder = (ScrollListViewHolder) viewHolder;
                    scrollListViewHolder.toScroll();
                } else if (viewHolder instanceof AudioViewHolder) {
                    AudioViewHolder holder = (AudioViewHolder) viewHolder;
                    if (showSave && i == 0) {
                        holder.mAudioView.recordCurPosition();
                    }
                }
            }
        }


    }
}

package com.demo.youshengdemo.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.demo.youshengdemo.R;


/**
 * @author qiaodong
 * @date 18-5-18
 * <p>
 * 1.可以添加头部和尾部
 * 2.数据为空时显示空布局
 * 3.显示加载布局
 * 4.显示错误布局
 */
public class CommonRecyclerView extends FrameLayout {

    private static final String TAG = CommonRecyclerView.class.getSimpleName();
    protected ViewGroup mProgressView;
    protected ViewGroup mEmptyView;
    protected ViewGroup mErrorView;
    private Context mContext;
    /**
     * 包裹了一层的头部底部Adapter
     */
    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    /**
     * 这个是列表数据的Adapter
     */
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycleView;
    private RecycleViewDataOberver mDataObserver;

    public CommonRecyclerView(Context context) {
        this(context, null);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private View root;

    private void initView(Context context) {
        if (isInEditMode()) {
            return;
        }
        mContext = context;
        root = LayoutInflater.from(mContext).inflate(R.layout.common_recyclerview, null);
        mRecycleView = root.findViewById(R.id.recycle_view);
        mProgressView = root.findViewById(R.id.progress);
        mEmptyView = root.findViewById(R.id.empty);
        mErrorView = root.findViewById(R.id.error);
        mDataObserver = new RecycleViewDataOberver(this);
        addView(root);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycleView.addItemDecoration(itemDecoration);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycleView.setLayoutManager(manager);
    }

    public boolean isComputingLayout() {
        return mRecycleView.isComputingLayout();
    }

    public RecyclerView.ViewHolder getViewHolder(int position) {
        return mRecycleView.findViewHolderForAdapterPosition(position);
    }

    public void removeOnScollListener(RecyclerView.OnScrollListener listener) {
        if (listener != null) {
            mRecycleView.removeOnScrollListener(listener);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        mAdapter = adapter;

        if (adapter instanceof WrapRecyclerAdapter) {
            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        } else {
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(adapter);
        }
        mRecycleView.setAdapter(mWrapRecyclerAdapter);


        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);
        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(mRecycleView);

    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecycleView.addOnScrollListener(listener);
    }

    public int findFirstVisibleItemPosition() {
        final LinearLayoutManager manager = (LinearLayoutManager) mRecycleView.getLayoutManager();
        return manager.findFirstVisibleItemPosition();
    }

    public int findLastVisibleItemPosition() {
        final LinearLayoutManager manager = (LinearLayoutManager) mRecycleView.getLayoutManager();
        return manager.findLastVisibleItemPosition();
    }

    //目标项是否在最后一个可见项之后
    public boolean mShouldScroll = false;
    //记录目标项位置
    public int mToPosition = -1;

    public void scroll_by(int x,int y){
        mRecycleView.scrollBy(x, y);
    }

    /**
     * 滚动到指定位置
     *
     * @param position
     */
    public int scrollToPosition(final int position) {
        final LinearLayoutManager manager = (LinearLayoutManager) mRecycleView.getLayoutManager();
        int orientation = manager.getOrientation();
        int fir = manager.findFirstVisibleItemPosition(); // 第一个可见位置
        int end = manager.findLastVisibleItemPosition();// 最后一个可见位置
        int lastItem = mRecycleView.getChildLayoutPosition(mRecycleView.getChildAt(mRecycleView.getChildCount() - 1));
        if (fir < 0 || end < 0) {
            return 0;
        }

        if (position < fir) {
            // 第一种可能:跳转位置在第一个可见位置之前
            // mRecycleView.smoothScrollToPosition(position);
            mRecycleView.scrollToPosition(position);
            return 0;
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - fir;
            if (movePosition >= 0 && movePosition < mRecycleView.getChildCount()) {
                int distance;
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    distance = mRecycleView.getChildAt(movePosition).getLeft();
                    mRecycleView.scrollBy(distance, 0);
                    return distance;
                } else {
                    distance = mRecycleView.getChildAt(movePosition).getTop();
                    mRecycleView.scrollBy(0, distance);
                    return distance;
                }

                //mRecycleView.smoothScrollBy(0, top);

            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            // mRecycleView.smoothScrollToPosition(position);
            mRecycleView.scrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
            return 0;
        }
        return 0;
    }

    public void scrollVerticalDistance(int distance) {
        mRecycleView.scrollBy(0, distance);
    }

    public void adjustResize(int position) {
        if (position <= -1 || position >= mRecycleView.getLayoutManager().getItemCount()) {
            return;
        }
        mRecycleView.scrollToPosition(position);
        RecyclerView.LayoutManager layoutManager = mRecycleView.getLayoutManager();


    }

    /**
     * 添加头部
     */
    public void addHeaderView(View view) {
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        }
    }

    /**
     * 添加底部
     */
    public void addFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }

    public void disableOverScroll() {
        mRecycleView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    public void setScrollBarStyle(int style) {
        mRecycleView.setScrollBarStyle(style);
    }

    public void enableOverScroll() {
        mRecycleView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    /**
     * 移除头部
     */
    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    public int getItemCount() {
        if (mWrapRecyclerAdapter != null) {
            return mWrapRecyclerAdapter.getItemCount();
        } else {
            return -1;
        }
    }

    /**
     * 移除底部
     */
    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }

    public void setEmptyView(View emptyView) {
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
    }

    public void setProgressView(View progressView) {
        mProgressView.removeAllViews();
        mProgressView.addView(progressView);
    }

    public void setErrorView(View errorView) {
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
    }

    public void setEmptyView(int emptyView) {
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
    }

    public void setProgressView(int progressView) {
        mProgressView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mProgressView);
    }

    public void setErrorView(int errorView) {
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
    }

    public void showError() {
        if (mErrorView.getChildCount() > 0) {
            hideAll();
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }

    public void showEmpty() {
        if (mEmptyView.getChildCount() > 0) {
            hideAll();
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }


    public void showProgress() {
        if (mProgressView.getChildCount() > 0) {
            hideAll();
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }

    public void showRecycler() {
        hideAll();
        mRecycleView.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        mEmptyView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        mRecycleView.setVisibility(View.INVISIBLE);
    }

    class RecycleViewDataOberver extends RecyclerView.AdapterDataObserver {

        private CommonRecyclerView mNoteBookRecyclerView;

        public RecycleViewDataOberver(CommonRecyclerView noteBookRecyclerView) {
            mNoteBookRecyclerView = noteBookRecyclerView;
        }

        @Override
        public void onChanged() {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);
            }
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            }
            update();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            }
            update();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            }
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) {
                return;
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);
            }
            update();
        }

        private void update() {
            int count = mWrapRecyclerAdapter.getItemCount();
          /*  if (count == 0 && !NetworkUtils.isAvailable(mContext)) {
                showError();
                return;
            }
*/
            if (count == 0) {
                mNoteBookRecyclerView.showEmpty();
            } else {
                mNoteBookRecyclerView.showRecycler();
            }
        }
    }

    ;
}
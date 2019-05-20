package com.demo.youshengdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.base.BaseData;
import com.demo.youshengdemo.base.BaseViewHolder;
import com.demo.youshengdemo.viewholder.AudioViewHolder;
import com.demo.youshengdemo.viewholder.ImageViewHolder;
import com.demo.youshengdemo.viewholder.TextViewHolder;
import com.demo.youshengdemo.viewholder.VideoViewHolder;

import java.util.List;


/**
 * @author qiaodong
 * @date 18-5-16
 */

public class PlayerScrollListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = PlayerScrollListAdapter.class.getSimpleName();
    private static final int ITEM_TYPE_ERROR = -1;
    private static final int ITEM_TYPE_VIDEO = 1;
    private static final int ITEM_TYPE_AUDIO = 2;
    private static final int ITEM_TYPE_TEXT = 3;
    private static final int ITEM_TYPE_SCROLL_LIST = 4;
    private static final int ITEM_TYPE_IMAGE = 5;

    private Context mContext;
    private OnItemClickListener mListener;
    private List<BaseData> mDatas;

    public PlayerScrollListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_VIDEO:
                viewHolder = new VideoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_horizontal, parent, false));
                break;
            case ITEM_TYPE_AUDIO:
                viewHolder = new AudioViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_audio_horizontal, parent, false));
                break;
            case ITEM_TYPE_TEXT:
                viewHolder = new TextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_text_horizontal, parent, false));
                break;
            case ITEM_TYPE_IMAGE:
                viewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_horiziontal, parent, false));
                break;
            case ITEM_TYPE_ERROR:
            default:
                viewHolder = new TextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_text_horizontal, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_VIDEO) {
            if (holder instanceof VideoViewHolder) {
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.mVideoView.setVideoPath((Integer) mDatas.get(position).data);
            }
        } else if (itemViewType == ITEM_TYPE_AUDIO) {
            if (holder instanceof AudioViewHolder) {
                AudioViewHolder audioViewHolder = (AudioViewHolder) holder;
                audioViewHolder.mAudioView.setAudioPath((Integer) mDatas.get(position).data);
            }
        } else if (itemViewType == ITEM_TYPE_TEXT) {
            if (holder instanceof TextViewHolder) {
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.mTv.setText((Integer) mDatas.get(position).data);
            }
        } else if (itemViewType == ITEM_TYPE_IMAGE) {
            if (holder instanceof ImageViewHolder) {
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.mImageView.setImageResource((Integer) mDatas.get(position).data);
            }
        }

    }

    /**
     * 设置数据
     */
    public void setDatas(List<BaseData> datas) {
        mDatas = datas;
        if (mDatas != null && mDatas.size() > 0) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = ITEM_TYPE_ERROR;
        if (mDatas != null && mDatas.size() > 0) {
            BaseData baseData = mDatas.get(position);
            if (baseData.itemType == BaseData.Type.video) {
                itemType = ITEM_TYPE_VIDEO;
            } else if (baseData.itemType == BaseData.Type.audio) {
                itemType = ITEM_TYPE_AUDIO;
            } else if (baseData.itemType == BaseData.Type.text) {
                itemType = ITEM_TYPE_TEXT;
            } else if (baseData.itemType == BaseData.Type.scroll_list) {
                itemType = ITEM_TYPE_SCROLL_LIST;
            } else if (baseData.itemType == BaseData.Type.image) {
                itemType = ITEM_TYPE_IMAGE;
            } else {
                itemType = ITEM_TYPE_ERROR;
            }
        }
        return itemType;
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 条目点击事件
     */
    public interface OnItemClickListener {
        /**
         * 单机
         *
         * @param position
         */
        void onItemClick(int position, long id);

    }
}

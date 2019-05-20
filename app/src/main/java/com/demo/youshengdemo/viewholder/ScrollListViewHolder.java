package com.demo.youshengdemo.viewholder;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.adapter.PlayerScrollListAdapter;
import com.demo.youshengdemo.base.BaseData;
import com.demo.youshengdemo.base.BaseViewHolder;
import com.demo.youshengdemo.recycleview.PlayerRecycleView;
import com.demo.youshengdemo.recycleview.RecyclerViewDivider;

import java.util.List;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1811:42 AM
 * <p>
 * desc   :
 */
public class ScrollListViewHolder extends BaseViewHolder {

    public PlayerRecycleView rv;
    private final PlayerScrollListAdapter playerScrollListAdapter;

    public ScrollListViewHolder(View itemView) {
        super(itemView);
        rv = itemView.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(new RecyclerViewDivider(itemView.getContext(), LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));

        playerScrollListAdapter = new PlayerScrollListAdapter(itemView.getContext());
        rv.setAdapter(playerScrollListAdapter);
    }

    public void setDatas(List<BaseData> datas) {
        if (playerScrollListAdapter != null) {
            playerScrollListAdapter.setDatas(datas);
        }
    }

    public void toScroll(){
        //rv.scroll_by(1,0);
       // rv.scroll_by(-1,0);
    }

    @Override
    public void pause() {

    }
}

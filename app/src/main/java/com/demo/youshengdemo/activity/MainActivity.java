package com.demo.youshengdemo.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.demo.youshengdemo.PermissionUtils;
import com.demo.youshengdemo.R;
import com.demo.youshengdemo.adapter.PlayerListAdapter;
import com.demo.youshengdemo.base.BaseData;
import com.demo.youshengdemo.mananger.DataFactory;
import com.demo.youshengdemo.recycleview.PlayerRecycleView;
import com.demo.youshengdemo.recycleview.RecyclerViewDivider;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayerListAdapter.OnItemClickListener {

    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 100;
    private Context mContext;
    private PlayerRecycleView mRv;
    private PlayerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        requestPermission();
        initView();
        initData();
    }

    private void initView() {
        mRv = findViewById(R.id.rv);
        mAdapter = new PlayerListAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL));
        mRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        List<BaseData> baseData = DataFactory.productDatas();
        if (mAdapter != null) {
            mAdapter.setDatas(baseData);
        }
    }

    @Override
    public void onItemClick(int position, long id) {

    }

    private void requestPermission() {
        boolean hasPermissions = PermissionUtils.isHasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermissions) {
            return;
        }
        PermissionUtils.requestPermissions(mContext, this, REQUEST_PERMISSION_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean isHasPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case REQUEST_PERMISSION_EXTERNAL_STORAGE:
                if (isHasPermission) {
                    Log.d("qd", "权限申请成功");
                } else {
                    Log.d("qd", "权限申请失败");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

}

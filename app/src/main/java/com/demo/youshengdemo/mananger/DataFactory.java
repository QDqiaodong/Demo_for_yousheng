package com.demo.youshengdemo.mananger;

import com.demo.youshengdemo.R;
import com.demo.youshengdemo.base.BaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1811:46 AM
 * <p>
 * desc   :
 */
public class DataFactory {

    private static final int COUNT = 3;
    private static final int[] videoDatas = new int[]{R.raw.video3, R.raw.video1, R.raw.video2};
    private static final int[] audioDatas = new int[]{R.raw.audio1, R.raw.audio2, R.raw.audio3};
    private static final int[] textDatas = new int[]{R.string.str_text_1, R.string.str_text_2, R.string.str_text_3};
    private static final int[] imageDatas = new int[]{R.mipmap.bg_blue, R.mipmap.bg_green, R.mipmap.bg_orange};

    public static List<BaseData> productDatas() {
        List<BaseData> datas = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            BaseData<Integer> videoData = new BaseData<>(videoDatas[i], BaseData.Type.video);
            BaseData<Integer> audioData = new BaseData<>(audioDatas[i], BaseData.Type.audio);
            BaseData<Integer> textData = new BaseData<>(textDatas[i], BaseData.Type.text);
            BaseData<Integer> imageData = new BaseData<>(imageDatas[i], BaseData.Type.image);

            //step1.
            List<BaseData> scrollListDatas = new ArrayList<>();
            scrollListDatas.add(videoData);
            scrollListDatas.add(audioData);
            scrollListDatas.add(textData);
            scrollListDatas.add(imageData);
            BaseData<List<BaseData>> scrollListData = new BaseData<>(scrollListDatas, BaseData.Type.scroll_list);

            datas.add(videoData);
            datas.add(audioData);
            datas.add(textData);
            datas.add(imageData);
            datas.add(scrollListData);
        }

        return datas;
    }
}

package com.demo.youshengdemo.base;

import java.io.Serializable;

/**
 * author : Qiaodong
 * e-mail : qiaodong@okay.cn
 * date   : 2019/5/1810:55 AM
 * <p>
 * desc   :
 */
public class BaseData<T> implements Serializable {

    public Type itemType;

    public T data;

    public BaseData(T t,Type type) {
        this.data = t;
        this.itemType = type;
    }

    public enum Type {

        video, audio, text, scroll_list, image
    }
}

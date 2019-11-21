package com.framework.model.demo;

import android.bluetooth.BluetoothDevice;

import com.chad.library.adapter.base.entity.MultiItemEntity;
/**
 * @author zhangzhiqiang
 * @date 2019/9/18.
 * description：
 */
public class BlueTooThBean implements MultiItemEntity{
    public String name;
    public String address;
    public BluetoothDevice device;

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlueTooThBean that = (BlueTooThBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return address != null ? address.equals(that.address) : that.address == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}

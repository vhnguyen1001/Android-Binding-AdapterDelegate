package hiennguyen.me.bindingadapterdelegate.util;


import android.support.v7.util.ListUpdateCallback;

public interface BaseListUpdateCallback extends ListUpdateCallback {

    void onDataChanged();
}

/*
 *  Copyright Roman Donchenko. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package hiennguyen.me.bindingadaptersample.viewmodel;

import android.content.Context;
import android.view.View;

import hiennguyen.me.bindingadapterdelegate.actionhandler.listener.ActionClickListener;

import java.util.ArrayList;
import java.util.List;

import hiennguyen.me.bindingadapterdelegate.ListConfig;
import hiennguyen.me.bindingadapterdelegate.adapter.BindableAdapter;
import hiennguyen.me.bindingadaptersample.model.ActionType;
import hiennguyen.me.bindingadaptersample.model.BaseModel;
import hiennguyen.me.bindingadaptersample.util.DummyDataProvider;
import hiennguyen.me.bindingadaptersample.util.SimpleCallback;
import hiennguyen.me.bindingadaptersample.viewmodel.delegate.AdvertisementDelegate;
import hiennguyen.me.bindingadaptersample.viewmodel.delegate.UserDelegate;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * Viewmodel for page with User and Advertisement item types in one list
 */
public class UserListViewModel implements ListViewModel, ActionClickListener {

    private ListConfig mListConfig;
    private BindableAdapter<List<BaseModel>, BaseModel> mAdapter;
    private SimpleCallback mCallback;

    public UserListViewModel(Context context, final SimpleCallback callback) {
        mCallback = callback;
        mAdapter = new BindableAdapter<>(new BaseModel.ModelDiffCallBack(),
                new UserDelegate(this)
        );
        mListConfig = new ListConfig.Builder(mAdapter)
                .setDefaultDividerEnabled(true)
                .build(context);
    }

    @Override
    public ListConfig getListConfig() {
        return mListConfig;
    }

    @Override
    public void onDestroy() {
        mCallback = null;
    }

    @Override
    public void loadData() {
        mAdapter.setItems(getDummyData());
    }

    private List<BaseModel> getDummyData() {
        ArrayList<BaseModel> list = new ArrayList<>();
        list.addAll(DummyDataProvider.getUsers());
//        list.add(0, DummyDataProvider.getAdvertisment(1));
//        list.add(6, DummyDataProvider.getAdvertisment(2));
//        list.add(12, DummyDataProvider.getAdvertisment(3));
        return list;
    }

    @Override
    public void onActionClick(final View view, final String actionType, final Object model) {
        switch (actionType) {
            case ActionType.OPEN:
                mCallback.showMessage("Short click by " + model.toString());
                break;
            case ActionType.MENU:
                mCallback.showMessage("Long click by " + model.toString());
                break;
        }
    }
}

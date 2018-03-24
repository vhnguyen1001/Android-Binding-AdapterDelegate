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

import hiennguyen.me.bindingadapterdelegate.actionhandler.ActionHandler;
import hiennguyen.me.bindingadapterdelegate.actionhandler.listener.ActionClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hiennguyen.me.bindingadapterdelegate.ListConfig;
import hiennguyen.me.bindingadapterdelegate.adapter.BindableAdapter;
import hiennguyen.me.bindingadapterdelegate.delegate.ModelActionItemDelegate;
import hiennguyen.me.bindingadaptersample.R;
import hiennguyen.me.bindingadaptersample.BR;
import hiennguyen.me.bindingadaptersample.model.ActionType;
import hiennguyen.me.bindingadaptersample.model.Advertisement;
import hiennguyen.me.bindingadaptersample.model.BaseModel;
import hiennguyen.me.bindingadaptersample.model.Location;
import hiennguyen.me.bindingadaptersample.model.User;
import hiennguyen.me.bindingadaptersample.util.DummyDataProvider;
import hiennguyen.me.bindingadaptersample.viewmodel.action.ShowToastAction;

/**
 * Viewmodel for page with All item types in one list
 */
public class AllInOneListViewModel implements ListViewModel {

    private ListConfig mListConfig;
    private BindableAdapter<List<BaseModel>, BaseModel> mAdapter;

    public AllInOneListViewModel(Context context) {

        final ActionClickListener actionHandler = new ActionHandler.Builder()
                .addAction(ActionType.OPEN, new ShowToastAction())
                .addAction(ActionType.MENU, new ShowToastAction())
                //.addAction(null, new TrackAction()) // fires for any actionType
                .build();

        //noinspection unchecked
        mAdapter = new BindableAdapter<>(new BaseModel.ModelDiffCallBack(),
                // new UserDelegate(actionHandler), you do not need even create custom delegate
                new ModelActionItemDelegate<>(actionHandler, User.class, R.layout.item_user, BR.user),
                new ModelActionItemDelegate<>(actionHandler, Location.class, R.layout.item_location, BR.location),
                new ModelActionItemDelegate<>(actionHandler, Advertisement.class, R.layout.item_advertisment, BR.advertisment)
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
    public void onDestroy() {}

    @Override
    public void loadData() {
        mAdapter.setItems(getDummyData());
    }

    private List<BaseModel> getDummyData() {
        ArrayList<BaseModel> list = new ArrayList<>();
        list.addAll(DummyDataProvider.getLocations());
        list.addAll(DummyDataProvider.getUsers());

        Collections.shuffle(list);

        list.add(0, DummyDataProvider.getAdvertisment(1));
        list.add(6, DummyDataProvider.getAdvertisment(2));
        list.add(12, DummyDataProvider.getAdvertisment(3));
        list.add(22, DummyDataProvider.getAdvertisment(4));
        list.add(30, DummyDataProvider.getAdvertisment(5));
        return list;
    }
}

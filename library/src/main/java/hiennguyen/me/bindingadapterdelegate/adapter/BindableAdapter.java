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

package hiennguyen.me.bindingadapterdelegate.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.DiffCallback;

import hiennguyen.me.bindingadapterdelegate.base.AdapterDelegate;
import hiennguyen.me.bindingadapterdelegate.base.AdapterDelegatesManager;

import java.util.List;

import hiennguyen.me.bindingadapterdelegate.util.ComputingAdapterChangedHelper;

/**
 * RecyclerView Adapter for using with data binding. Uses List of items as dataset.
 * Based on AdapterDelegates Library by Hannes Dorfmann https://github.com/sockeqwe/AdapterDelegates
 *
 * @param <Model> The type of the datasoure / items
 */

public class BindableAdapter<ModelList extends List<Model>, Model> extends BaseBindableAdapter<ModelList> {

    private final ComputingAdapterChangedHelper<Model> mHelper;

    public BindableAdapter(@NonNull DiffCallback<Model> diffCallback) {
        mHelper = new ComputingAdapterChangedHelper<>(this, diffCallback);
    }

    public BindableAdapter(@NonNull DiffCallback<Model> diffCallback, @NonNull final AdapterDelegatesManager<ModelList> delegatesManager) {
        super(delegatesManager);
        mHelper = new ComputingAdapterChangedHelper<>(this, diffCallback);
    }

    public BindableAdapter(@NonNull DiffCallback<Model> diffCallback, AdapterDelegate<ModelList>... adapterDelegates) {
        super(adapterDelegates);
        mHelper = new ComputingAdapterChangedHelper<>(this, diffCallback);
    }

    public BindableAdapter(@NonNull DiffCallback<Model> diffCallback, final ModelList items,
                           final AdapterDelegatesManager<ModelList> delegatesManager) {
        super(items, delegatesManager);
        mHelper = new ComputingAdapterChangedHelper<>(this, diffCallback);
        mHelper.setList(items);
    }

    public BindableAdapter(@NonNull DiffCallback<Model> diffCallback, final ModelList items,
                           AdapterDelegate<ModelList>... adapterDelegates) {
        super(items, adapterDelegates);
        mHelper = new ComputingAdapterChangedHelper<>(this, diffCallback);
        mHelper.setList(items);
    }

    @Override
    public int getItemCount() {
        return mHelper.getItemCount();
    }

    @Override
    public ModelList getItems() {
        return (ModelList) mHelper.getList();
    }

    @Nullable
    protected Model getItem(int position) {
        return mHelper.getItem(position);
    }


    @Override
    public void setItems(ModelList items) {
        mHelper.setList(items);
    }



}

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
import android.support.v7.widget.RecyclerView;

import hiennguyen.me.bindingadapterdelegate.base.AbsDelegationAdapter;
import hiennguyen.me.bindingadapterdelegate.base.AdapterDelegate;
import hiennguyen.me.bindingadapterdelegate.base.AdapterDelegatesManager;

import hiennguyen.me.bindingadapterdelegate.delegate.IdHolder;

/**
 * RecyclerView Adapter for using with data binding. Dataset can be any type, not only List of items.
 * Based on AdapterDelegates Library by Hannes Dorfmann https://github.com/sockeqwe/AdapterDelegates
 *
 * @param <T> The type of the datasoure / items
 */
public abstract class BaseBindableAdapter<T> extends AbsDelegationAdapter<T> {

    /**
     * Creates Adapter with empty adapter delegates manager
     */
    public BaseBindableAdapter() {
    }

    public BaseBindableAdapter(@NonNull AdapterDelegatesManager<T> delegatesManager) {
        super(delegatesManager);
    }

    public BaseBindableAdapter(AdapterDelegate<T>... delegates) {
        super(new AdapterDelegatesManager<>());
        for (AdapterDelegate<T> delegate : delegates) delegatesManager.addDelegate(delegate);
    }

    public BaseBindableAdapter(T items, AdapterDelegatesManager<T> delegatesManager) {
        super(delegatesManager);
        setItems(items);
    }

    public BaseBindableAdapter(T items, AdapterDelegate<T>... delegates) {
        this(delegates);
        setItems(items);
    }

    /**
     * Get item id if specific AdapterDelegate implement IdHolder interface
     *
     * @param position position of item in data source
     * @return the item id
     */
    @Override
    public long getItemId(int position) {
        final int viewType = delegatesManager.getItemViewType(getItems(), position);
        final AdapterDelegate<T> delegate = delegatesManager.getDelegateForViewType(viewType);
        //noinspection unchecked
        return delegate instanceof IdHolder ? ((IdHolder) delegate).getItemId(getItems(), position) :
                RecyclerView.NO_ID;
    }

}

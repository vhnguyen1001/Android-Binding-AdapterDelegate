package hiennguyen.me.bindingadapterdelegate.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.List;


/**
 * This class is modified from {@link android.arch.paging.PagedListAdapter} for presenting data list from {@link List}
 * in {@link RecyclerView}
 * <p>
 * This class is a convenience wrapper around {@link AsyncDifferConfig} that implements common default
 * behavior for item getting, and listening to List update callbacks in main or background thread.
 * <p>
 *
 * @param <T> Type of the List this helper will receive.
 * @param <VH> A class that extends ViewHolder that will be used by the adapter.
 */
public abstract class UnBindableAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>  {

    private final AsyncListDiffer<T> mHelper;


    /**
     * Creates a PagedListAdapter with default threading and
     * {@link android.support.v7.util.ListUpdateCallback}.
     *
     * Convenience for {@link #UnBindableAdapter(AsyncDifferConfig)}, which uses default threading
     * behavior.
     *
     * @param diffCallback The {@link android.support.v7.util.DiffUtil.ItemCallback} instance to compare items in the list.
     */
    protected UnBindableAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        mHelper = new AsyncListDiffer<>(this, diffCallback);
    }

    @SuppressWarnings("unused, WeakerAccess")
    protected UnBindableAdapter(@NonNull AsyncDifferConfig<T> config) {
        mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), config);
    }

    /**
     * Set the new list to be displayed.
     * <p>
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    public void setList(List<T> list) {
        mHelper.submitList(list);
    }

    @Nullable
    protected T getItem(int position) {
        return mHelper.getCurrentList().get(position);
    }

    @Override
    public int getItemCount() {
        return mHelper.getCurrentList().size();
    }

    /**
     * Returns the list currently being displayed by the Adapter.
     * <p>
     * This is not necessarily the most recent list passed to {@link #setList(List)}, because a
     * diff is computed asynchronously between the new list and the current list before updating the
     * currentList value.
     *
     * @return The list currently being displayed.
     */
    @Nullable
    public List<T> getList() {
        return mHelper.getCurrentList();
    }
}

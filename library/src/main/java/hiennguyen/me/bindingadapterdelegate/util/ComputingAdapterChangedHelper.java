package hiennguyen.me.bindingadapterdelegate.util;

import android.support.annotation.RestrictTo;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.recyclerview.extensions.ListAdapterConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Helper object for displaying a List in {@link RecyclerView.Adapter RecyclerView.Adapter}, it is modified from
 * {@link android.support.v7.recyclerview.extensions.ListAdapterHelper} which signals the adapter of changes when the List is changed by
 * computing changes with DiffUtil in the background.
 * This {@link ComputingAdapterChangedHelper} customize computing change with DiffUtil in main or background thread
 */
public class ComputingAdapterChangedHelper<T> {

    private final ListUpdateCallback mUpdateCallback;
    private final ListAdapterConfig<T> mConfig;


    @SuppressWarnings("WeakerAccess")
    public ComputingAdapterChangedHelper(RecyclerView.Adapter adapter, DiffCallback<T> diffCallback) {
        mUpdateCallback = new AdapterCallback(adapter);
        ListAdapterConfig.Builder builder = new ListAdapterConfig.Builder<T>()
                .setDiffCallback(diffCallback);
        mConfig = builder.build();
    }

    @SuppressWarnings("WeakerAccess")
    public ComputingAdapterChangedHelper(ListUpdateCallback listUpdateCallback, ListAdapterConfig<T> config) {
        mUpdateCallback = listUpdateCallback;
        mConfig = config;
    }

    /**
     * Default ListUpdateCallback that dispatches directly to an adapter. Can be replaced by a
     * custom ListUpdateCallback if e.g. your adapter has a header in it, and so has an offset
     * between list positions and adapter positions.
     *
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static class AdapterCallback implements ListUpdateCallback {
        private final RecyclerView.Adapter mAdapter;

        public AdapterCallback(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onInserted(int position, int count) {
            mAdapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            mAdapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            mAdapter.notifyItemRangeChanged(position, count, payload);
        }
    }

    private List<T> mList;

    // Max generation of currently scheduled runnable
    private int mMaxScheduledGeneration;


    /**
     * Get the item from the current List at the specified index.
     *
     * @param index Index of item to get, must be >= 0, and &lt; {@link #getItemCount()}.
     * @return The item at the specified List position.
     */
    @SuppressWarnings("WeakerAccess")
    public T getItem(int index) {
        if (mList == null) {
            throw new IndexOutOfBoundsException("Item count is zero, getItem() call is invalid");
        }

        return mList.get(index);
    }

    public List<T> getList() {
        return mList;
    }

    /**
     * Get the number of items currently presented by this AdapterHelper. This value can be directly
     * returned to {@link RecyclerView.Adapter#getItemCount()}.
     *
     * @return Number of items being presented.
     */
    @SuppressWarnings("WeakerAccess")
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * Pass a new List to the AdapterHelper. Adapter updates will be computed on a background
     * thread.
     * <p>
     * If a List is already present, a diff will be computed asynchronously on a background thread.
     * When the diff is computed, it will be applied (dispatched to the {@link ListUpdateCallback}),
     * and the new List will be swapped in.
     *
     * @param newList The new List.
     */
    @SuppressWarnings("WeakerAccess")
    public void setList(final List<T> newList) {
        if (newList == mList) {
            // nothing to do
            return;
        }

        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++mMaxScheduledGeneration;

        if (newList == null) {
            mUpdateCallback.onRemoved(0, mList.size());
            mList = null;
            return;
        }

        if (mList == null) {
            // fast simple first insert
            mUpdateCallback.onInserted(0, newList.size());
            mList = newList;
            return;
        }

        final List<T> oldList = mList;
        mConfig.getBackgroundThreadExecutor().execute(() -> {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mConfig.getDiffCallback().areItemsTheSame(
                            oldList.get(oldItemPosition), newList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mConfig.getDiffCallback().areContentsTheSame(
                            oldList.get(oldItemPosition), newList.get(newItemPosition));
                }
            });

            mConfig.getMainThreadExecutor().execute(() -> {
                if (mMaxScheduledGeneration == runGeneration) {
                    latchList(newList, result);
                }
            });
        });
    }

    private void latchList(List<T> newList, DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(mUpdateCallback);
        mList = newList;
    }
}

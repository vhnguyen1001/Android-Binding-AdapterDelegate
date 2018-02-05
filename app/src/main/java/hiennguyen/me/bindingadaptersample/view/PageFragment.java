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
package hiennguyen.me.bindingadaptersample.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hiennguyen.me.bindingadaptersample.R;
import hiennguyen.me.bindingadaptersample.databinding.FragmentPageBinding;
import hiennguyen.me.bindingadaptersample.util.SimpleCallback;
import hiennguyen.me.bindingadaptersample.viewmodel.AllInOneListViewModel;
import hiennguyen.me.bindingadaptersample.viewmodel.ListViewModel;
import hiennguyen.me.bindingadaptersample.viewmodel.LocationListViewModel;
import hiennguyen.me.bindingadaptersample.viewmodel.UserListViewModel;

public class PageFragment extends Fragment implements SimpleCallback {

    private static final String ARG_PAGE = "page";

    private ListViewModel mViewModel;
    private FragmentPageBinding mBinding;

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int page = 0;
        if (getArguments() != null) page = getArguments().getInt(ARG_PAGE, 0);

        switch (page) {
            case 0:
                mViewModel = new UserListViewModel(getContext(), this);
                break;
            case 1:
                mViewModel = new LocationListViewModel(getContext());
                break;
            case 2:
            default:
                mViewModel = new AllInOneListViewModel(getContext());
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_page, container, false);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        mViewModel.onDestroy();
        super.onDestroy();
    }

    public static int getPageTitleResId(final int page) {
        switch (page) {
            case 0: return R.string.page_users;
            case 1: return R.string.page_locations;
            case 2:
            default: return R.string.page_all_in_one;
        }
    }

    @Override
    public void showMessage(final String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}

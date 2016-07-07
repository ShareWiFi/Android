/**
 * The MIT License (MIT) Copyright (c) 2016 Timo Bähr
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or s
 * ubstantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tbaehr.sharewifi.android.features.networkList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbaehr.sharewifi.android.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NetworkListFragment extends Fragment {

    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_networklist);

        Bundle bundleInRange = new Bundle();
        bundleInRange.putBoolean(NetworksTab.BUNDLE_EXTRA_IN_RANGE, true);
        mTabHost.addTab(mTabHost.newTabSpec("inRange").setIndicator(getContext().getString(R.string.networklist_inrange)),
                NetworksTab.class, bundleInRange);

        Bundle bundleOutOfRange = new Bundle();
        bundleOutOfRange.putBoolean(NetworksTab.BUNDLE_EXTRA_IN_RANGE, false);
        mTabHost.addTab(mTabHost.newTabSpec("outOfRange").setIndicator(getContext().getString(R.string.networklist_outofrange)),
                NetworksTab.class, bundleOutOfRange);

        return mTabHost;
    }
}
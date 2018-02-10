package com.m68476521.mymexico.fragmentmain;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.databinding.FragmentMainSmallBinding;
import com.m68476521.mymexico.fragmentpager.SimpleFragmentPagerAdapter;

/**
 * Main Fragment
 */

public class FragmentMainSmall extends Fragment {
    private FragmentMainSmallBinding fragmentMainSmallBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMainSmallBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_small, container,false);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getContext(), getFragmentManager());
        fragmentMainSmallBinding.viewpager.setAdapter(adapter);
        fragmentMainSmallBinding.slidingTabs.setupWithViewPager(fragmentMainSmallBinding.viewpager);
        return fragmentMainSmallBinding.getRoot();
    }
}
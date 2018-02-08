package com.m68476521.mymexico.fragmenttrick;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mymexico.R;
import com.m68476521.mymexico.databinding.FragmentTrickBinding;

public class FragmentTrick extends Fragment {

    private FragmentTrickBinding fragmentTrickBinding;

    public FragmentTrick() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTrickBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trick, container, false);
        return fragmentTrickBinding.getRoot();
    }
}

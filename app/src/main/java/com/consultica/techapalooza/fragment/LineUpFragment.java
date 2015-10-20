package com.consultica.techapalooza.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;


public class LineUpFragment extends Fragment{

    public static final String TAG = "com.consultica.techapalooza.fragment.LineUpFragment";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_up, container, false);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.line_up_fragment_container, new LineUpGalleryFragment(), LineUpGalleryFragment.TAG);
        transaction.commit();

        return view;
    }
}

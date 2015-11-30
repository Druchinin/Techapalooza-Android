package com.consultica.techapalooza.ui.fragments.lineup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.consultica.techapalooza.database.DBMaster;


public class LineUpFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.LineUpFragment";

    private View view;

    private static LineUpFragment instance;

    public static LineUpFragment getInstance() {
        if (instance == null)
            instance = new LineUpFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_up, container, false);

        setup();

        return view;
    }

    private void setup() {
        if (DBMaster.getInstance(getActivity()).getAllBands().size() > 0) {

            if (getActivity().getSupportFragmentManager().findFragmentByTag(LineUpGalleryFragment.TAG) != null) {

                if (!getActivity().getSupportFragmentManager().findFragmentByTag(LineUpGalleryFragment.TAG).isAdded()) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.line_up_fragment_container, LineUpGalleryFragment.getInstance(), LineUpGalleryFragment.TAG);
                    transaction.commit();
                } else {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.line_up_fragment_container, LineUpGalleryFragment.getInstance(), LineUpGalleryFragment.TAG);
                    transaction.commit();
                }
            } else {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.line_up_fragment_container, LineUpGalleryFragment.getInstance(), LineUpGalleryFragment.TAG);
                transaction.commit();
            }

        }
    }

}

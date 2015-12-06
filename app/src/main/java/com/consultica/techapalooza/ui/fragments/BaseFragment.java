package com.consultica.techapalooza.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.Field;

/**
 * Created by dimadruchinin on 21.11.15.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void show(final FragmentManager fm) {
        show(fm, addToBackStack());
    }

    public void show(FragmentManager fm, boolean addToBackStack) {
        replaceFragment(fm, addToBackStack);
    }

    protected void replaceFragment(FragmentManager fm, boolean addToBackStack) {
        FragmentTransaction t = fm.beginTransaction();
        t.replace(getContainer(), this, getName());
        if (addToBackStack)
            t.addToBackStack(getName());
        t.commitAllowingStateLoss();
    }

    public void addFragment(FragmentManager fm, final boolean addToBackStack) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(getContainer(), this, getName());
        if (addToBackStack)
            ft.addToBackStack(getName());
        ft.commit();
    }

    public abstract String getName();

    public abstract int getContainer();

    protected boolean addToBackStack() {
        return false;
    }

}


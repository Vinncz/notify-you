package com.vinapp.notifyyou.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;

public class NewFragment extends Fragment {

    private View xmlReference;
    private TileItemViewModel vm;

    public NewFragment withViewModel (TileItemViewModel _vm) {
        this.vm = _vm;
        return this;
    }

    public NewFragment () {}

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(TileItemViewModel.class);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        xmlReference = inflater.inflate(R.layout.fragment_new, container, false);

        return xmlReference;
    }
}

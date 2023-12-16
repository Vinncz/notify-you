package com.vinapp.notifyyou.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.adapters.TileItemAdapter;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.models.TileItem;

import java.util.List;

public class HomeFragment extends Fragment {

    private TileItemViewModel vm;

    private List<TileItem> pinnedPendingUpdate;
    private List<TileItem> unpinnedPendingUpdate;

    public HomeFragment withViewModel (TileItemViewModel vm) {
        this.vm = vm;
        return this;
    }

    public HomeFragment () {}

    @Override
    public void onCreate (Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(TileItemViewModel.class);
    }

    @Override
    public View onCreateView (LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        View xmlReference = _inflater.inflate(R.layout.fragment_home, _container, false);

        RecyclerView pinnedTileItems   = xmlReference.findViewById(R.id.pinnedTileItems);
        RecyclerView unpinnedTileItems = xmlReference.findViewById(R.id.tileItems);

        TileItemAdapter pinnedAdapter   = new TileItemAdapter(this.vm);
        TileItemAdapter unpinnedAdapter = new TileItemAdapter(this.vm);

        pinnedTileItems.setAdapter(pinnedAdapter);
        unpinnedTileItems.setAdapter(unpinnedAdapter);

        pinnedTileItems.setLayoutManager(new LinearLayoutManager(xmlReference.getContext(), LinearLayoutManager.VERTICAL, false));
        unpinnedTileItems.setLayoutManager(new LinearLayoutManager(xmlReference.getContext(), LinearLayoutManager.VERTICAL, false));

        /*
         * Check for data that is waiting for a front-end update.
         * TL: Ada data baru yang masuk saat recyclerview lagi update layout gak? Kalo ada, bikin recycler view update datanya pas dia sempet.
         */
        pinnedTileItems.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                if (pinnedPendingUpdate != null) {
                    pinnedAdapter.setData(pinnedPendingUpdate);
                    pinnedPendingUpdate = null;
                }
            });
        unpinnedTileItems.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                if ( unpinnedPendingUpdate != null) {
                    unpinnedAdapter.setData(unpinnedPendingUpdate);
                    unpinnedPendingUpdate = null;
                }
            });

        /*
         * Observe for LiveData changes.
         */
        vm.getPinnedLiveData().observe(getViewLifecycleOwner(), newDataset -> {
            if ( getResources().getString(R.string.debug).equals("true") ) {
                System.out.println("pinned adapter's dataset:");
                for (TileItem ti : newDataset) {
                    System.out.println("id: " + ti.getId() + " alarm? " + ti.getAlarmIsActive());
                }
            }

            boolean newDatasetIsAvailable = newDataset != null && !containsTheSameData(newDataset, pinnedAdapter.getData());

            if ( newDatasetIsAvailable ) {
                if ( !pinnedTileItems.isComputingLayout() ) {
                    pinnedTileItems.post(() -> pinnedAdapter.setData(newDataset));
                } else {
                    pinnedPendingUpdate = newDataset;
                }
            }
        });
        vm.getUnpinnedLiveData().observe(getViewLifecycleOwner(), newDataset -> {
            if ( getResources().getString(R.string.debug).equals("true") ) {
                System.out.println("unpinned adapter's dataset:");
                for (TileItem ti : newDataset) {
                    System.out.println("id: " + ti.getId() + " alarm? " + ti.getAlarmIsActive());
                }
            }

            boolean newDatasetIsAvailable = newDataset != null && !containsTheSameData(newDataset, unpinnedAdapter.getData());

            if ( newDatasetIsAvailable ) {
                Toast.makeText(getContext(), "ada data baru!", Toast.LENGTH_SHORT).show();
                if ( !unpinnedTileItems.isComputingLayout() ) {
                    unpinnedTileItems.post(() -> unpinnedAdapter.setData(newDataset));
                } else {
                    unpinnedPendingUpdate = newDataset;
                }
            }
        });

        return xmlReference;
    }

    public boolean containsTheSameData (List<TileItem> _dataA, List<TileItem> _dataB) {
        if (_dataA == null && _dataB == null) return true;
        if (_dataA == null || _dataB == null) return false;
        if (_dataA.isEmpty() && _dataB.isEmpty()) return true;
        if (_dataA.isEmpty() || _dataB.isEmpty()) return false;
        if (_dataA.size() != _dataB.size()) return false;

        boolean isTheSame = true;
        for (int i = 0; i < _dataA.size(); i++) {
            if (_dataA.get(i).getId() != _dataB.get(i).getId()) {
                isTheSame = false;

            }
        }

        return isTheSame;
    }

}

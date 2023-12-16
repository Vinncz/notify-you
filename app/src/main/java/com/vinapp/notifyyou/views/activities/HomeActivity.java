package com.vinapp.notifyyou.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.utilities.permission_requestors.NotificationPermissionRequestor;
import com.vinapp.notifyyou.views.fragments.HomeFragment;
import com.vinapp.notifyyou.views.fragments.NewFragment;
import com.vinapp.notifyyou.views.fragments.PreferenceFragment;

public class HomeActivity extends AppCompatActivity {

    private Context activityContext;
    private TileItemViewModel vm;
    private Fragment homeFrag, newFrag, prefFrag;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityContext = this;

        NotificationPermissionRequestor npr = new NotificationPermissionRequestor(this);
        npr.requestPermission();

        vm = new ViewModelProvider(this).get(TileItemViewModel.class);

        FragmentManager manager = getSupportFragmentManager();
        displayInitialFragment(manager, new HomeFragment().withViewModel(vm));
        handleBottomNavInitialization(manager);
    }

    @Override
    protected void onResume () {
        super.onResume();
        vm = new ViewModelProvider(this).get(TileItemViewModel.class);
    }

    @Override
    protected void onStart () {
        super.onStart();
        vm = new ViewModelProvider(this).get(TileItemViewModel.class);
    }

    public TileItemViewModel getViewModel () {
        return this.vm;
    }

    private void displayInitialFragment (FragmentManager manager, Fragment _f) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragmentContainerView, _f);
        transaction.commit();
    }

    private void handleBottomNavInitialization (FragmentManager manager) {
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnItemSelectedListener(
                item -> {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.home) {
                        homeFrag = homeFrag == null ? new HomeFragment().withViewModel(vm) : homeFrag;
                        selectedFragment = homeFrag;

                    } else if (itemId == R.id.newTileItem) {
                        newFrag = newFrag == null ? new NewFragment().withViewModel(vm) : newFrag;
                        selectedFragment = newFrag;

                    } else if (itemId == R.id.settings) {
                        prefFrag = prefFrag == null ? new PreferenceFragment() : prefFrag;
                        selectedFragment = prefFrag;

                    }

                    if (selectedFragment != null) {
                        FragmentTransaction transaction1 = manager.beginTransaction();
                        transaction1.replace(R.id.fragmentContainerView, selectedFragment);
                        transaction1.commit();
                    }

                    return true;
                }
        );
    }
}

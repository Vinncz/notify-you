package com.vinapp.notifyyou.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;

public class HomeFragment extends Fragment {

    private View xmlReference;
    private TileItemViewModel vm;
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
        xmlReference = _inflater.inflate(R.layout.fragment_home, _container, false);

        Button timeButton = xmlReference.findViewById(R.id.timeButton);
        timeButton.setOnClickListener(view -> {
            MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H);

            MaterialTimePicker materialTimePicker = builder.build();

            materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                int hour = materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();

                String selectedTime = hour + ":" + minute;
                TextView selectedTimeTextView = xmlReference.findViewById(R.id.selectedTimeTextView);
                selectedTimeTextView.setText(selectedTime);
            });

            materialTimePicker.show(requireActivity().getSupportFragmentManager(), "tag");
        });

        attachAdapterIntoRecyclerViews();

        return xmlReference;
    }

    private void attachAdapterIntoRecyclerViews () {
        System.out.println(vm.getLiveData());
        xmlReference.findViewById(R.id.textView);
    }
}

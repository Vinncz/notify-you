package com.vinapp.notifyyou.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.factories.TileItemFactory;
import com.vinapp.notifyyou.models.TileItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewFragment extends Fragment {

    private class Time {
        public int hour = 0;
        public int min  = 0;
    }

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

        com.google.android.material.button.MaterialButton submit = xmlReference.findViewById(R.id.NotificationButton);
        EditText title = xmlReference.findViewById(R.id.CustomNotificationHead);
        EditText body = xmlReference.findViewById(R.id.CustomNotificationBody);
        com.google.android.material.materialswitch.MaterialSwitch useAlarm = xmlReference.findViewById(R.id.useAlarm);

        Time time = new Time();
        useAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if ( isChecked ) {
                MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H);

                MaterialTimePicker materialTimePicker = builder.build();

                materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                    time.hour = materialTimePicker.getHour();
                    time.min = materialTimePicker.getMinute();

                    String selectedTime = time.hour + ":" + time.min;
                    TextView selectedTimeTextView = xmlReference.findViewById(R.id.selectedTimeTextView);
                    selectedTimeTextView.setText(selectedTime);
                });

                materialTimePicker.show(requireActivity().getSupportFragmentManager(), "tag");
            }
        });

        submit.setOnClickListener(view -> {
            String notificationTitle = title.getText().toString();
            String notificationBody = body.getText().toString();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());

            String completeDateTime = currentDate + " " + String.format(Locale.getDefault(), "%02d:%02d", time.hour, time.min);
            String alarmTime = completeDateTime.substring(11);

            TileItem ti = TileItemFactory.make(notificationTitle, notificationBody, alarmTime, false, false);
            vm.insert(ti);

            title.setText("");
            body.setText("");

            Toast.makeText(getContext(), "Successfully created TileItem!", Toast.LENGTH_SHORT).show();
        });

        return xmlReference;
    }
}

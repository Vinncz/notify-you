package com.vinapp.notifyyou.views.fragments;

import static java.lang.String.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.card.MaterialCardView;
import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.adapters.TileItemAdapter;
import com.vinapp.notifyyou.controllers.TileItemController;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.factories.TileItemFactory;
import com.vinapp.notifyyou.models.TileItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewFragment extends Fragment {

    private class Time {
        public Integer hour = 0;
        public Integer min  = 0;
    }

    private TileItemViewModel vm;
    private MaterialButton  submit;
    private TextInputEditText title, body;
    private MaterialSwitch useAlarm, immidiatelyPin;
    private MaterialCardView alarmInput, selectedTimeContainer;
    private MaterialTextView selectedTimeTextView;
    private Time selectedTimeValue = new Time();
    private TileItemController tiController = new TileItemController();

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
        View xmlReference = inflater.inflate(R.layout.fragment_new, container, false);
        initializeElements(xmlReference);

        Calendar calendar = Calendar.getInstance();

        selectedTimeTextView.setText(format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        selectedTimeContainer.setVisibility(View.GONE);

        useAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int currentHour   = selectedTimeValue.hour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = selectedTimeValue.min  = calendar.get(Calendar.MINUTE);

            if ( isChecked ) {
                attachExpandingAnimation();
                selectedTimeContainer.setOnClickListener(v -> {
                    MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                            .setHour(currentHour)
                            .setMinute(currentMinute + 1);

                    MaterialTimePicker materialTimePicker = builder.build();

                    materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                        int selectedHour = selectedTimeValue.hour = materialTimePicker.getHour();
                        int selectedMinute = selectedTimeValue.min = materialTimePicker.getMinute();

                        showToastForTimeDifference(currentHour, currentMinute, selectedHour, selectedMinute);

                        this.selectedTimeTextView.setText(format("%02d:%02d", selectedHour, selectedMinute));
                    });

                    materialTimePicker.show(requireActivity().getSupportFragmentManager(), "tag");
                });

            } else {
                attachCollapsingAnimation();
            }
        });

        submit.setOnClickListener(view -> {
            String notificationTitle = title.getText().toString();
            String notificationBody = body.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());

            String completeDateTime = currentDate + " " + format(Locale.getDefault(), "%02d:%02d", selectedTimeValue.hour, selectedTimeValue.min);
            String alarmTime = completeDateTime.substring(11);

            boolean isAlarmActive = useAlarm.isChecked();
            boolean pinByDefault = immidiatelyPin.isChecked();

            TileItem ti = TileItemFactory.make(notificationTitle, notificationBody, alarmTime, pinByDefault, isAlarmActive);
            TileItemController.ValidationReturnStatement validationReport = tiController.validate(ti);
            if ( validationReport.isSuccessful == false ) {
                Toast.makeText(getContext(), validationReport.message, Toast.LENGTH_SHORT).show();
                return;
            }

            long generatedId = vm.insert(ti);
            ti.setId((int) generatedId);

            if (isAlarmActive) tiController.activateAlarm(ti);
            if (pinByDefault) tiController.notify(ti);

            resetInputElementsBack(calendar);
            Toast.makeText(getContext(), "Successfully created TileItem!", Toast.LENGTH_SHORT).show();
        });

        return xmlReference;
    }

    private void resetInputElementsBack (Calendar calendar) {
        title.setText("");
        body.setText("");

        useAlarm.setChecked(false);
        immidiatelyPin.setChecked(false);
        selectedTimeValue.hour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedTimeValue.min  = calendar.get(Calendar.MINUTE);

        selectedTimeTextView.setText(format("%02d:%02d", selectedTimeValue.hour, selectedTimeValue.min));
    }

    private void initializeElements (View xmlReference) {
        this.submit               = xmlReference.findViewById(R.id.NotificationButton);
        this.title                = xmlReference.findViewById(R.id.ET_title);
        this.body                 = xmlReference.findViewById(R.id.ET_body);
        this.useAlarm             = xmlReference.findViewById(R.id.useAlarm);
        this.immidiatelyPin       = xmlReference.findViewById(R.id.immidiatelyPin);
        this.alarmInput           = xmlReference.findViewById(R.id.CV_alarmContainer);
        this.selectedTimeContainer= xmlReference.findViewById(R.id.CV_selectedTimeContainer);
        this.selectedTimeTextView = xmlReference.findViewById(R.id.TV_selectedTime);
    }

    private void showToastForTimeDifference (int currentHour, int currentMinute, int selectedHour, int selectedMinute) {
        int hourDifference = selectedHour - currentHour;
        int minuteDifference = selectedMinute - currentMinute;

        if (hourDifference < 0 || (hourDifference == 0 && minuteDifference < 0)) {
            hourDifference += 24;
        }

        minuteDifference = (hourDifference * 60 + selectedMinute - currentMinute) % 60;

        String differenceText;
        if (hourDifference < 2) {
            int totalMinutes = hourDifference * 60 + minuteDifference;
            differenceText = String.format("Alarm set for %d minutes from now", totalMinutes);
        } else {
            differenceText = String.format("Alarm set for %d hours and %d minutes from now", hourDifference, minuteDifference);
        }

        Toast.makeText(getContext(), differenceText, Toast.LENGTH_SHORT).show();
    }

    private void attachExpandingAnimation () {
        selectedTimeContainer.setVisibility(View.VISIBLE);
        selectedTimeContainer.setAlpha(0f);
        selectedTimeContainer.animate()
                .alpha(1f)
                .setDuration(50)
                .start();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) selectedTimeContainer.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        selectedTimeContainer.setLayoutParams(layoutParams);

        TransitionManager.beginDelayedTransition(
                (ViewGroup) selectedTimeContainer.getParent().getParent().getParent(),
                new AutoTransition()
        );
    }

    private void attachCollapsingAnimation () {
        selectedTimeContainer.animate()
                .alpha(0f)
                .setDuration(50)
                .withEndAction(() -> {
                    selectedTimeContainer.setVisibility(View.GONE);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) selectedTimeContainer.getLayoutParams();
                    layoutParams.height = 0;
                    selectedTimeContainer.setLayoutParams(layoutParams);

                    TransitionManager.beginDelayedTransition(
                            (ViewGroup) selectedTimeContainer.getParent().getParent().getParent(),
                            new AutoTransition()
                    );
                })
                .start();
    }

}

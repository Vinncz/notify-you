package com.vinapp.notifyyou.views.activities;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.controllers.TileItemController;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.factories.TileItemFactory;
import com.vinapp.notifyyou.models.TileItem;
import com.vinapp.notifyyou.views.fragments.NewFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTileitemActivity extends AppCompatActivity {

    private class Time {
        public Integer hour = 0;
        public Integer min  = 0;
    }

    private TileItemViewModel vm;
    private MaterialButton submit;
    private TextInputEditText title, body;
    private MaterialSwitch useAlarm;
    private MaterialCardView alarmInput;
    private MaterialTextView selectedTimeTextView;
    private Time selectedTimeValue = new Time();

    private TileItemController tiController = new TileItemController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tileitem);

        Bundle extras = getIntent().getExtras();
        vm = new ViewModelProvider(this).get(TileItemViewModel.class);

        if (extras != null) {
            Integer tileItemId = extras.getInt("tileItemId");
            LiveData<TileItem> tileItem = vm.getById(tileItemId);

            initializeElements();

            Calendar calendar = Calendar.getInstance();

            selectedTimeTextView.setText(format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
            alarmInput.setVisibility(View.GONE);

            useAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int currentHour   = selectedTimeValue.hour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = selectedTimeValue.min  = calendar.get(Calendar.MINUTE);

                if ( isChecked ) {
                    alarmInput.setVisibility(View.VISIBLE);
                    alarmInput.setOnClickListener(v -> {
                        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_24H)
                                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                                .setHour(currentHour)
                                .setMinute(currentMinute);

                        MaterialTimePicker materialTimePicker = builder.build();

                        materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                            int selectedHour = selectedTimeValue.hour = materialTimePicker.getHour();
                            int selectedMinute = selectedTimeValue.min = materialTimePicker.getMinute();

                            showToastForTimeDifference(currentHour, currentMinute, selectedHour, selectedMinute);

                            this.selectedTimeTextView.setText(format("%02d:%02d", selectedHour, selectedMinute));
                        });

                        materialTimePicker.show(this.getSupportFragmentManager(), "tag");
                    });

                } else {
                    alarmInput.setVisibility(View.GONE);
                }
            });

            submit.setOnClickListener(view -> {
                String notificationTitle = title.getText().toString();
                String notificationBody = body.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = dateFormat.format(calendar.getTime());

                String completeDateTime = currentDate + " " + format(Locale.getDefault(), "%02d:%02d", selectedTimeValue.hour, selectedTimeValue.min);
                String alarmTime = completeDateTime.substring(11);

                TileItem ti = tileItem.getValue();
                ti.setTitle(notificationTitle);
                ti.setBody(notificationBody);
                TileItemController.ValidationReturnStatement validationReport = tiController.validate(ti);

                if ( validationReport.isSuccessful == false ) {
                    Toast.makeText(this, validationReport.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                vm.update(ti);

                resetInputElementsBack(calendar);
                Toast.makeText(this , "Successfully created TileItem!", Toast.LENGTH_SHORT).show();
            });


        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
        }

    }

    private void resetInputElementsBack (Calendar calendar) {
        title.setText("");
        body.setText("");

        useAlarm.setChecked(false);
        selectedTimeValue.hour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedTimeValue.min  = calendar.get(Calendar.MINUTE);

        selectedTimeTextView.setText(format("%02d:%02d", selectedTimeValue.hour, selectedTimeValue.min));
    }

    private void initializeElements () {
        this.submit               = findViewById(R.id.NotificationButton);
        this.title                = findViewById(R.id.ET_title);
        this.body                 = findViewById(R.id.ET_body);
        this.useAlarm             = findViewById(R.id.useAlarm);
        this.alarmInput           = findViewById(R.id.CV_alarmContainer);
        this.selectedTimeTextView = findViewById(R.id.TV_selectedTime);
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

        Toast.makeText(this , differenceText, Toast.LENGTH_SHORT).show();
    }


}
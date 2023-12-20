package com.vinapp.notifyyou.views.activities;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.vinapp.notifyyou.models.TileItem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTileitemActivity extends AppCompatActivity {

    private static class Time {
        public Integer hour = 0;
        public Integer min  = 0;
    }

    private TileItemViewModel vm;
    private MaterialButton  submit;
    private TextInputEditText title, body;
    private MaterialSwitch useAlarm, immidiatelyPin;
    private MaterialCardView selectedTimeContainer;
    private MaterialTextView selectedTimeTextView;
    private EditTileitemActivity.Time selectedTimeValue = new EditTileitemActivity.Time();
    private TileItemController tiController = new TileItemController();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tileitem);

        vm = new ViewModelProvider(this).get(TileItemViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int tileItemId = extras.getInt("tileItemId");
            vm.getById(tileItemId).observe(this, fetchedData -> {
                if ( fetchedData != null ) {
                    initializeElements(fetchedData);
                    doYourJob(fetchedData);
                }
            });

        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void doYourJob (TileItem ti) {
        Calendar calendar = Calendar.getInstance();

        useAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if ( isChecked ) {
                attachExpandingAnimation();
                attachEditTimeOnClickListener();

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

            ti.setTitle(notificationTitle);
            ti.setBody(notificationBody);
            ti.setAlarmTime(alarmTime);
            ti.setPinned(pinByDefault);
            ti.setAlarmIsActive(isAlarmActive);
            ti.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            TileItemController.ValidationReturnStatement validationReport = tiController.validate(ti);

            if ( validationReport.isSuccessful == false ) {
                Toast.makeText(this, validationReport.message, Toast.LENGTH_SHORT).show();
                return;

            }

            vm.update(ti);
            if (isAlarmActive) tiController.activateAlarm(ti);
            else tiController.cancelAlarm(ti);

            Toast.makeText(this, "Successfully edited TileItem!", Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    private void attachEditTimeOnClickListener () {
        selectedTimeContainer.setOnClickListener(v -> {
            MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setHour(this.selectedTimeValue.hour)
                    .setMinute(this.selectedTimeValue.min + 1);

            MaterialTimePicker materialTimePicker = builder.build();

            materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
                int selectedHour = materialTimePicker.getHour();
                int selectedMinute = materialTimePicker.getMinute();

                showToastForTimeDifference(this.selectedTimeValue.hour, this.selectedTimeValue.min, selectedHour, selectedMinute);

                this.selectedTimeValue.hour = selectedHour;
                this.selectedTimeValue.min  = selectedMinute;

                this.selectedTimeTextView.setText(format("%02d:%02d", selectedHour, selectedMinute));
            });

            materialTimePicker.show(this.getSupportFragmentManager(), "tag");
        });
    }

    private void initializeElements (TileItem ti) {
        this.submit                = findViewById(R.id.BT_editButton);
        this.title                 = findViewById(R.id.ET_title);
        this.body                  = findViewById(R.id.ET_body);
        this.useAlarm              = findViewById(R.id.useAlarm);
        this.immidiatelyPin        = findViewById(R.id.immidiatelyPin);
        this.selectedTimeContainer = findViewById(R.id.CV_selectedTimeContainer);
        this.selectedTimeTextView  = findViewById(R.id.TV_selectedTime);

        this.title.setText(ti.getTitle());
        this.body.setText(ti.getBody());
        this.useAlarm.setChecked(ti.getAlarmIsActive());
        this.immidiatelyPin.setChecked(ti.getPinned());
        if (ti.getAlarmIsActive()) {
            this.selectedTimeContainer.setVisibility(View.VISIBLE);
        } else {
            this.selectedTimeContainer.setVisibility(View.GONE);
        }
        this.selectedTimeTextView.setText(ti.getAlarmTime());

        this.selectedTimeValue.hour = Integer.parseInt(ti.getAlarmTime().substring(0, 2));
        this.selectedTimeValue.min  = Integer.parseInt(ti.getAlarmTime().substring(3, 5));

        attachEditTimeOnClickListener();
        this.selectedTimeTextView.setText(format("%02d:%02d", this.selectedTimeValue.hour, this.selectedTimeValue.min));
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
            differenceText = String.format("Alarm set for %d minutes from last saved value", totalMinutes);
        } else {
            differenceText = String.format("Alarm set for %d hours and %d minutes from last saved value", hourDifference, minuteDifference);
        }

        Toast.makeText(this, differenceText, Toast.LENGTH_SHORT).show();
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
                ( ViewGroup ) selectedTimeContainer.getParent().getParent().getParent(),
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

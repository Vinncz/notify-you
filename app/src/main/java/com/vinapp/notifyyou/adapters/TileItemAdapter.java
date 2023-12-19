package com.vinapp.notifyyou.adapters;

import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.button.MaterialButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.application_starter.GlobalValueHolder;
import com.vinapp.notifyyou.controllers.TileItemController;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.models.TileItem;
import com.vinapp.notifyyou.views.activities.EditTileitemActivity;

import java.util.ArrayList;
import java.util.List;

public class TileItemAdapter extends RecyclerView.Adapter<TileItemAdapter.ViewHolder> {

    private List<TileItem> data;
    private final TileItemViewModel vm;
    private final TileItemController tic;

    public TileItemAdapter (TileItemViewModel _vm) {
        this.data = new ArrayList<>();
        this.vm = _vm;
        this.tic = new TileItemController();
    }

    /**
     * Sets the local data that the adapter will be using.
     * @apiNote After calling this method, the adapter will refresh.
     * @param _newData The new data which will replace the existing adapter's data
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData (List<TileItem> _newData) {
        data = _newData;
        notifyDataSetChanged();
    }

    /**
     * Returns the local data that the adapter is using.
     * Typically used for validation.
     * @return Retrieves the dataset which the holder is using to display data.
     */
    public List<TileItem> getData () {
        return this.data;
    }

    @Override
    public int getItemCount () {
        boolean dataIsEmpty = data == null || data.isEmpty();

        if ( dataIsEmpty )
            return 1; // to facilitate the showing of an empty placeholder
        else
            return data.size();
    }

    @Override
    public int getItemViewType (int position) {
        boolean dataIsEmpty = data == null || data.isEmpty();

        if ( dataIsEmpty )
            return R.layout.layout_empty_tile_item_placeholder;
        else
            return R.layout.layout_tile_item;
    }

    @NonNull
    @Override
    public TileItemAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup _parent, int _viewType) {
        View xmlReference = LayoutInflater.from(_parent.getContext()).inflate(_viewType, _parent, false);
        return new ViewHolder(xmlReference);
    }

    @Override
    public void onBindViewHolder (@NonNull TileItemAdapter.ViewHolder _holder, int _position) {
        if ( getItemViewType(_position) == R.layout.layout_empty_tile_item_placeholder ) {
            _holder.bindEmpty();

        } else {
            TileItem currentData = data.get(_position);
            _holder.bind(currentData);

            _holder.pinButton.setOnClickListener(e -> {
                currentData.setPinned(true);
                tic.notify(currentData);
                vm.update(currentData);
                notifyItemChanged(_position);
            });
            _holder.unpinButton.setOnClickListener(e -> {
                currentData.setPinned(false);
                tic.cancel(currentData);
                vm.update(currentData);
                notifyItemChanged(_position);
            });
            _holder.deleteButton.setOnClickListener(e -> {
                currentData.setPinned(false);
                tic.cancel(currentData);
                tic.cancelAlarm(currentData);
                vm.delete(currentData);
                notifyItemRemoved(_position);
            });
            _holder.alarmSwitch.setOnCheckedChangeListener((buttonReference, isActive) -> {
                currentData.setAlarmIsActive(isActive);
                if ( isActive )
                    tic.activateAlarm(currentData);
                else
                    tic.cancelAlarm(currentData);

                vm.updateForAlarmRelatedThings(currentData);
            });
            _holder.xmlReference.setOnClickListener(view -> {
                boolean bodyIsVisible = _holder.body.getVisibility() == View.VISIBLE;

                if ( bodyIsVisible )
                    attachCollapsingAnimation(_holder);
                else
                    attachExpandingAnimation(_holder);

            });

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View xmlReference;
        public MaterialTextView title, body, id, isPinned, alarmTime;
        public MaterialSwitch alarmSwitch;
        public MaterialButton pinButton, unpinButton, deleteButton , expandToggle;

        public ViewHolder (@NonNull View _xmlReference) {
            super(_xmlReference);
            this.xmlReference = _xmlReference;
            initializeAttribute(_xmlReference);
        }

        public void bind (TileItem _object) {
            id.setText(_object.getId().toString());
            isPinned.setText(_object.getPinned().toString());

            title.setText(_object.getTitle());
            body.setText(_object.getBody());

            if ( _object.getPinned()) {
                unpinButton.setVisibility(View.VISIBLE);
                pinButton.setVisibility(View.GONE);
                if ( !tic.notificationIsCurrentlyDisplayed(_object) )
                    tic.notify(_object);
            } else {
                pinButton.setVisibility(View.VISIBLE);
                unpinButton.setVisibility(View.GONE);
                if ( tic.notificationIsCurrentlyDisplayed(_object) )
                    tic.cancel(_object);
            }

            /*
             * Here's the very important lesson I learned:
             * NEVER, EVER, FORGET TO NULL-IZE OR CHANGE ONCLICK LISTENERS INTO A DUMMY.
             */
            pinButton.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Pin button is pressed", Toast.LENGTH_SHORT).show();
            });
            unpinButton.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Unpin button is pressed", Toast.LENGTH_SHORT).show();
            });
            deleteButton.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Delete button is pressed", Toast.LENGTH_SHORT).show();
            });
            /* YOU TOOK HALF OF MY DAY, $#!+#3@D */
            alarmSwitch.setOnCheckedChangeListener(null);

            alarmTime.setText(_object.getAlarmTime());
            alarmSwitch.setChecked(_object.getAlarmIsActive());

            expandToggle.setOnClickListener(e -> {
                Intent intent = new Intent(e.getContext(), EditTileitemActivity.class);

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("tileItemId", _object.getId());

                intent.putExtras(dataBundle);

                e.getContext().startActivity(intent);
            });
        }

        public void bindEmpty () {
            this.title.setText(this.xmlReference.getResources().getString(R.string.empty_tile_item_placeholder_title));
            this.body.setText(this.xmlReference.getResources().getString(R.string.empty_tile_item_placeholder_body));
            this.xmlReference.setOnClickListener(e -> {
                GlobalValueHolder.getAppMainActivityInstance().goTo(R.id.newTileItem);
            });
        }

        public void initializeAttribute (View _xmlReference) {
            this.id           = _xmlReference.findViewById(R.id.TV_id);
            this.isPinned     = _xmlReference.findViewById(R.id.TV_isPinned);
            this.expandToggle = _xmlReference.findViewById(R.id.BT_expandTile);

            this.title        = _xmlReference.findViewById(R.id.TV_title);
            this.body         = _xmlReference.findViewById(R.id.TV_body);

            this.pinButton    = _xmlReference.findViewById(R.id.BT_pin);
            this.unpinButton  = _xmlReference.findViewById(R.id.BT_unpin);
            this.deleteButton = _xmlReference.findViewById(R.id.BT_delete);

            this.alarmTime    = _xmlReference.findViewById(R.id.TV_alarm);
            this.alarmSwitch  = _xmlReference.findViewById(R.id.SW_alarmToggle);
        }
    }

    private static void attachExpandingAnimation (@NonNull ViewHolder _holder) {
        _holder.body.setVisibility(View.VISIBLE);
        _holder.body.setAlpha(0f);
        _holder.body.animate()
                .alpha(1f)
                .setDuration(10)
                .start();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _holder.body.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        _holder.body.setLayoutParams(layoutParams);

        TransitionManager.beginDelayedTransition(
            (ViewGroup) _holder.xmlReference.getParent(),
            new AutoTransition()
        );
    }

    private static void attachCollapsingAnimation (@NonNull ViewHolder _holder) {
        _holder.body.animate()
                .alpha(0f)
                .setDuration(10)
                .withEndAction(() -> {
                    _holder.body.setVisibility(View.GONE);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _holder.body.getLayoutParams();
                    layoutParams.height = 0;
                    _holder.body.setLayoutParams(layoutParams);

                    TransitionManager.beginDelayedTransition(
                        (ViewGroup) _holder.xmlReference.getParent(),
                        new AutoTransition()
                    );
                })
                .start();
    }

}

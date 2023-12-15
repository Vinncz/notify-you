package com.vinapp.notifyyou.adapters;

import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.button.MaterialButton;

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
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.models.TileItem;

import java.util.ArrayList;
import java.util.List;


public class TileItemAdapter extends RecyclerView.Adapter<TileItemAdapter.ViewHolder> {

    private enum State {
        NO
    }

    private List<TileItem> data;
    private final TileItemViewModel vm;

    public TileItemAdapter (TileItemViewModel _vm) {
        this.data = new ArrayList<>();
        this.vm = _vm;
    }

    public void setData (List<TileItem> _newData) {
        data = _newData;
        notifyDataSetChanged();
    }

    public List<TileItem> getData () {
        return this.data;
    }

    @Override
    public int getItemCount () {
        boolean dataIsEmpty = data == null || data.isEmpty();

        if ( dataIsEmpty )
            return 1; // to facilitate the showing of empty placeholder
        else
            return data.size();
    }

//    @Override
//    public int getItemCount () {
//        return data.size();
//    }

    @Override
    public int getItemViewType(int position) {
        boolean dataIsEmpty = data == null || data.isEmpty();

        if ( dataIsEmpty )
            return R.layout.layout_empty_tile_item_placeholder;
        else
            return R.layout.layout_tile_item;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return R.layout.layout_tile_item;
//    }

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
                vm.update(currentData);
                notifyItemChanged(_position);
            });
            _holder.unpinButton.setOnClickListener(e -> {
                currentData.setPinned(false);
                vm.update(currentData);
                notifyItemChanged(_position);
            });
            _holder.deleteButton.setOnClickListener(e -> {
                currentData.setPinned(true);
                vm.delete(currentData);
                notifyItemRemoved(_position);
            });
            _holder.xmlReference.setOnClickListener(view -> {
                boolean isBodyVisible = _holder.body.getVisibility() == View.VISIBLE;

                if (isBodyVisible) {
                    _holder.body.animate()
                            .alpha(0f)
                            .setDuration(50)
                            .withEndAction(() -> {
                                _holder.body.setVisibility(View.GONE);

                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _holder.body.getLayoutParams();
                                layoutParams.height = 0;
                                _holder.body.setLayoutParams(layoutParams);

                                TransitionManager.beginDelayedTransition((ViewGroup) _holder.xmlReference.getParent(), new AutoTransition());
                            }).start();
                } else {
                    _holder.body.setVisibility(View.VISIBLE);
                    _holder.body.setAlpha(0f);
                    _holder.body.animate()
                            .alpha(1f)
                            .setDuration(50)
                            .start();

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _holder.body.getLayoutParams();
                    layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    _holder.body.setLayoutParams(layoutParams);

                    TransitionManager.beginDelayedTransition((ViewGroup) _holder.xmlReference.getParent(), new AutoTransition());
                }

                float rotationDegree = isBodyVisible ? 0f : -180f;
                _holder.expandToggle.animate()
                        .rotation(rotationDegree)
                        .setDuration(50)
                        .start();
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

            } else {
                pinButton.setVisibility(View.VISIBLE);
                unpinButton.setVisibility(View.GONE);

            }

            pinButton.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Pin button is pressed", Toast.LENGTH_SHORT).show();
            });
            unpinButton.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Unpin button is pressed", Toast.LENGTH_SHORT).show();
            });
            deleteButton.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Delete button is pressed", Toast.LENGTH_SHORT).show();
            });

            alarmTime.setText(_object.getAlarmTime());
            alarmSwitch.setChecked(_object.getAlarmIsActive());

            expandToggle.setOnClickListener(e -> {
                Toast.makeText(e.getContext(), "Expand toggle is pressed", Toast.LENGTH_SHORT).show();
            });
        }
        public void bindEmpty () {
            this.title.setText("It's so empty here!");
            this.body.setText("Let's create new TileItem, shall we?");
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

//            pinButton.setVisibility  (View.GONE);
//            unpinButton.setVisibility(View.GONE);
        }
    }
}

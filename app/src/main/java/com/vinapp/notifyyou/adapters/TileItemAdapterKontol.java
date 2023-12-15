package com.vinapp.notifyyou.adapters;

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


public class TileItemAdapterKontol extends RecyclerView.Adapter<TileItemAdapterKontol.ViewHolder> {

    private List<TileItem> data;
    private View xmlReference;
    private final TileItemViewModel vm;

    public TileItemAdapterKontol (TileItemViewModel _vm) {
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

    @Override
    public int getItemViewType(int position) {
        boolean dataIsEmpty = data == null || data.isEmpty();

        if ( dataIsEmpty )
            return R.layout.layout_empty_tile_item_placeholder;
        else
            return R.layout.layout_tile_item;

    }

    @NonNull
    @Override
    public TileItemAdapterKontol.ViewHolder onCreateViewHolder (@NonNull ViewGroup _parent, int _viewType) {
        xmlReference = LayoutInflater.from(_parent.getContext()).inflate(_viewType, _parent, false);
        return new ViewHolder(xmlReference);
    }

    @Override
    public void onBindViewHolder (@NonNull TileItemAdapterKontol.ViewHolder _holder, int _position) {
        if ( getItemViewType(_position) == R.layout.layout_empty_tile_item_placeholder ) {
            _holder.title = xmlReference.findViewById(R.id.TV_title);
            _holder.body = xmlReference.findViewById(R.id.TV_body);

            _holder.title.setText("It's so empty here!");
            _holder.body.setText("Let's create new TileItem, shall we?");

        } else {
            // masih access list yang lama, dengan posisi 11 yang baru
            TileItem ti = data.get(_position);
            xmlReference.setOnClickListener(v -> {
    //            Intent i = new Intent(v.getContext(), EditTileItem.class);
    //            i.putExtra("tile_item", ti);
    //            itemView.getContext().startActivity(i);
                Toast.makeText(v.getContext(), "Kepencet di id: " + ti.getId(), Toast.LENGTH_SHORT).show();
            });

            initializeViewHolder(_holder, ti, _position);

            _holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if ( ti.getAlarmIsActive() && !isChecked ) {
                    ti.setAlarmIsActive(false);
                    vm.updateForAlarmRelatedThings(ti);

                } else if ( !ti.getAlarmIsActive() && isChecked ) {
                    ti.setAlarmIsActive(true);
                    vm.updateForAlarmRelatedThings(ti);

                }

    //            tic.checkAlarm(ti, xmlReference.getContext());
            });

            if ( ti.getPinned() ) {
                _holder.unpinToggle.setOnClickListener(v -> {
                    ti.setPinned(false);
                    data.remove(_position);
                    vm.update(ti);
                    notifyItemRemoved(_position);
                    _holder.itemView.invalidate();
                });

            } else {
                _holder.pinToggle.setOnClickListener(v -> {
                    ti.setPinned(true);
                    data.remove(_position);
                    vm.update(ti);
                    notifyItemRemoved(_position);
                    _holder.itemView.invalidate();
                });

            }

            _holder.deleteToggle.setOnClickListener(v -> {
    //            tic.cancelAlarm(ti, itemView.getContext());
                data.remove(_position);
                vm.delete(ti);
                notifyItemRemoved(_position);
                _holder.itemView.invalidate();
//                notifyDataSetChanged();
            });
        }
    }

    private void initializeViewHolder (@NonNull ViewHolder _holder, TileItem ti, int _position) {
        if ( ti.getPinned() ) {
            System.out.println("pinned adapter is binding TileItem with id of: " + ti.getId());
        } else {
            System.out.println("unpinned adapter is binding TileItem with id of: " + ti.getId());
        }

        _holder.title= xmlReference.findViewById(R.id.TV_title);
        _holder.body = xmlReference.findViewById(R.id.TV_body);
        _holder.id = xmlReference.findViewById(R.id.TV_id);
        _holder.isPinned = xmlReference.findViewById(R.id.TV_isPinned);
        _holder.alarm = xmlReference.findViewById(R.id.TV_alarm);
        _holder.alarmSwitch = xmlReference.findViewById((R.id.SW_alarmToggle));
        _holder.pinToggle = xmlReference.findViewById(R.id.BT_pin);
        _holder.unpinToggle = xmlReference.findViewById(R.id.BT_unpin);
        _holder.deleteToggle = xmlReference.findViewById(R.id.BT_delete);
        _holder.dropDownToggle = xmlReference.findViewById(R.id.BT_expandTile);

        _holder.dropDownToggle.setOnClickListener(view -> {
            // Check if the body is currently invisible (gone)
            boolean isBodyVisible = _holder.body.getVisibility() == View.VISIBLE;

            // Set visibility and animate bodyView
            if (isBodyVisible) {
                // Body is visible, hide it
                _holder.body.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .withEndAction(() -> {
                            _holder.body.setVisibility(View.GONE);

                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _holder.body.getLayoutParams();
                            layoutParams.height = 0;
                            _holder.body.setLayoutParams(layoutParams);

                            TransitionManager.beginDelayedTransition((ViewGroup) xmlReference.getParent(), new AutoTransition());
                            // Notify item range changed for smooth transition of subsequent items
//                            notifyItemRangeChanged(_position + 1, getItemCount());
                        })
                        .start();
            } else {
                _holder.body.setVisibility(View.VISIBLE);
                _holder.body.setAlpha(0f);
                _holder.body.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start();

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) _holder.body.getLayoutParams();
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                _holder.body.setLayoutParams(layoutParams);

                TransitionManager.beginDelayedTransition((ViewGroup) xmlReference.getParent(), new AutoTransition());
//                notifyItemRangeChanged(_position + 1, getItemCount());
            }

            // Animate rotation of dropDownToggle
            float rotationDegree = isBodyVisible ? 0f : -180f;
            _holder.dropDownToggle.animate()
                    .rotation(rotationDegree)
                    .setDuration(500)
                    .start();
        });

        _holder.id.setText(ti.getId().toString());
        _holder.isPinned.setText(ti.getPinned().toString());
        _holder.title.setText(ti.getTitle());
        _holder.body.setText(ti.getBody());

        _holder.alarm.setText("⏰" + ti.getAlarmTime());

        if ( ti.getPinned()) {
            _holder.unpinToggle.setVisibility(View.VISIBLE);
            _holder.pinToggle.setVisibility(View.GONE);

        } else {
            _holder.pinToggle.setVisibility(View.VISIBLE);
            _holder.unpinToggle.setVisibility(View.GONE);

        }

        _holder.alarmSwitch.setChecked(ti.getAlarmIsActive());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public com.google.android.material.textview.MaterialTextView title, body, id, isPinned, alarm;
        public com.google.android.material.materialswitch.MaterialSwitch alarmSwitch;
        public com.google.android.material.button.MaterialButton pinToggle, unpinToggle, deleteToggle , dropDownToggle;
        public ViewHolder (@NonNull View itemView) {
            super(itemView);


        }
    }
}

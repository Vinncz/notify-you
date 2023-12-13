package com.vinapp.notifyyou.data_access_and_storage.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.vinapp.notifyyou.data_access_and_storage.data_access_objects.TileItemDAO;
import com.vinapp.notifyyou.data_access_and_storage.database_access.TileItemDatabase;
import com.vinapp.notifyyou.models.TileItem;

import java.util.List;

public class TileItemViewModel extends ViewModel {

    private SavedStateHandle savedStateHandle;
    private TileItemDAO dao;
    private LiveData<List<TileItem>> allTileItems;

    public TileItemViewModel (SavedStateHandle _savedStateHandle) {
        this.dao = TileItemDatabase.getInstance().dao();
        this.allTileItems = dao.getAll();
        this.savedStateHandle = _savedStateHandle;
    }

    public LiveData<List<TileItem>> getLiveData () {
        return this.allTileItems;
    }

}

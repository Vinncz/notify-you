package com.vinapp.notifyyou.data_access_and_storage.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.vinapp.notifyyou.data_access_and_storage.data_access_objects.TileItemDAO;
import com.vinapp.notifyyou.data_access_and_storage.database_access.TileItemDatabase;
import com.vinapp.notifyyou.models.TileItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TileItemViewModel extends ViewModel {

    private SavedStateHandle savedStateHandle;
    private ExecutorService executor;
    private TileItemDAO dao;
    private LiveData<List<TileItem>> allTileItems;
    private LiveData<List<TileItem>> pinnedTileItems;
    private LiveData<List<TileItem>> unpinnedTileItems;

    public TileItemViewModel (SavedStateHandle _savedStateHandle) {
        this.dao = TileItemDatabase.getInstance().dao();
        this.allTileItems = dao.getAll();
        this.savedStateHandle = _savedStateHandle;
        this.pinnedTileItems = dao.getPinned();
        this.unpinnedTileItems = dao.getUnpinned();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<TileItem>> getAllLiveData () {
        return dao.getAll();
    }

    public LiveData<List<TileItem>> getPinnedLiveData () {
        return dao.getPinned();
    }

    public LiveData<List<TileItem>> getUnpinnedLiveData () {
        return dao.getUnpinned();
    }

    public LiveData<TileItem> getById (Integer _tileItemId) { return dao.get(_tileItemId);}

    public void insert (TileItem _ti) {
        executor.execute( () -> {
            dao.insert(_ti);
            this.pinnedTileItems = dao.getPinned();
            this.unpinnedTileItems = dao.getUnpinned();
        } );
    }

    public void update (TileItem _ti) {
        executor.execute( () -> {
            dao.update(_ti);
            this.pinnedTileItems = dao.getPinned();
            this.unpinnedTileItems = dao.getUnpinned();
        } );
    }

    public void updateForAlarmRelatedThings (TileItem _ti) {
        executor.execute( () -> {
            dao.update(_ti);
        });
    }

    public void delete (TileItem _ti) {
        executor.execute( () -> {
            dao.delete(_ti);
            this.pinnedTileItems = dao.getPinned();
            this.unpinnedTileItems = dao.getUnpinned();
        } );
    }

}

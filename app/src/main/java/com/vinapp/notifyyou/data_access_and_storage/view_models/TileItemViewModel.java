package com.vinapp.notifyyou.data_access_and_storage.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.vinapp.notifyyou.data_access_and_storage.data_access_objects.TileItemDAO;
import com.vinapp.notifyyou.data_access_and_storage.database_access.TileItemDatabase;
import com.vinapp.notifyyou.models.TileItem;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TileItemViewModel extends ViewModel {

    private SavedStateHandle savedStateHandle;
    private final ExecutorService executor;
    private final TileItemDAO dao;

    public TileItemViewModel (SavedStateHandle _savedStateHandle) {
        this.dao = TileItemDatabase.getInstance().dao();
        this.savedStateHandle = _savedStateHandle;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<TileItem> getLatest () {
        return dao.getLatest();
    }

    public LiveData<TileItem> getById (int _id) {
        return dao.get(_id);
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

    public long insert (TileItem _ti) {
        Future<Long> future = executor.submit(() -> dao.insert(_ti));

        try {
            long id = future.get();
            return id;

        } catch ( InterruptedException | ExecutionException e) {
            return 0;

        }
    }

    public void update (TileItem _ti) {
        executor.execute( () -> {
            dao.update(_ti);
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
        } );
    }

}

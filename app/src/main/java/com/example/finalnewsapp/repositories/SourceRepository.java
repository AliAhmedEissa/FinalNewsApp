package com.example.finalnewsapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.finalnewsapp.Constant;
import com.example.finalnewsapp.api.APIManager;
import com.example.finalnewsapp.database.MyDataBase;
import com.example.finalnewsapp.model.SourceResponse.SourceResponse;
import com.example.finalnewsapp.model.SourceResponse.SourcesItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SourceRepository {

    public MutableLiveData<List<SourcesItem>> cacheSourcesList = new MutableLiveData<>();

    public void getSources() {
        Single observable = APIManager.getApis().getNewsSource(Constant.API_KEY)
                .subscribeOn(Schedulers.computation())
                .map(sourceResponse -> {
                    cacheSources(sourceResponse.getSources());
                    return sourceResponse;
                })
                .observeOn(Schedulers.io());

        SingleObserver singleObserver = new SingleObserver<SourceResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(SourceResponse sourceResponse) {
                cacheSourcesList.postValue(sourceResponse.getSources());
            }

            @Override
            public void onError(Throwable e) {
                getSourcesFromCache();
            }
        };

        observable.subscribe(singleObserver);
    }

    private void getSourcesFromCache() {
        List<SourcesItem> sourcesItems = MyDataBase.getInstance().sourceDao().getAllSources();
        cacheSourcesList.postValue(sourcesItems);
    }

    private void cacheSources(final List<SourcesItem> sources) {
        MyDataBase.getInstance().sourceDao().addAllSources(sources);
    }

}

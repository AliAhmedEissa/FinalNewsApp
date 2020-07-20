package com.example.finalnewsapp.repositories;

import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.finalnewsapp.Constant;
import com.example.finalnewsapp.api.APIManager;
import com.example.finalnewsapp.database.MyDataBase;
import com.example.finalnewsapp.model.NewsRespnse.ArticlesItem;
import com.example.finalnewsapp.model.NewsRespnse.NewsResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {

    public MutableLiveData<List<ArticlesItem>> articlesItem = new MutableLiveData<>();

    public void getNewsSources(String sourceId){
        Single observable = APIManager.getApis().getNewsBySourceId(Constant.API_KEY,sourceId)
                .subscribeOn(Schedulers.computation())
                .map(newsResponse -> {
                    cacheNews(newsResponse.articles);
                    return newsResponse;
                })
                .observeOn(Schedulers.io());

        SingleObserver<NewsResponse> singleObserver = new SingleObserver<NewsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(NewsResponse newsResponse) {
                articlesItem.postValue(newsResponse.getArticles());
            }

            @Override
            public void onError(Throwable e) {
                getNewsFromCache();
            }
        };
        observable.subscribe(singleObserver);
    }

    private void getNewsFromCache() {
        List<ArticlesItem> articlesItemList = MyDataBase.getInstance()
                .newsDao().getAllArticles();
        articlesItem.postValue(articlesItemList);
    }

    private void cacheNews(final List<ArticlesItem> articles) {
        MyDataBase.getInstance().newsDao().addAllArticles(articles);
    }

}

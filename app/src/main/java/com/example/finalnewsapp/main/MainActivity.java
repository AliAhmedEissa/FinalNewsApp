package com.example.finalnewsapp.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalnewsapp.R;
import com.example.finalnewsapp.model.NewsRespnse.ArticlesItem;
import com.example.finalnewsapp.model.SourceResponse.SourcesItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button tryAgainBtn;
    LinearLayout viewNoData;
    TabLayout tablayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Toolbar toolbar;

    NewsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        initView();
        initRecyclerView();
        observeToLiveData();
        mainViewModel.getNewsSources();
    }

    public void observeToLiveData() {
        mainViewModel.sourceList.observe(this, new Observer<List<SourcesItem>>() {
            @Override
            public void onChanged(List<SourcesItem> sourcesItems) {
                if (sourcesItems.size() != 0){
                    viewNoData.setVisibility(View.INVISIBLE);
                    initTabLayout(sourcesItems);
                }else{
                    viewNoData.setVisibility(View.VISIBLE);
                }
            }
        });

        mainViewModel.newsList.observe(this, new Observer<List<ArticlesItem>>() {
            @Override
            public void onChanged(List<ArticlesItem> articlesItems) {
                adapter.changeData(articlesItems);
            }
        });

        mainViewModel.message.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        mainViewModel.showProgressBar.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldShow) {
                if (shouldShow)
                    progressBar.setVisibility(View.GONE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    public void initRecyclerView() {
        adapter = new NewsAdapter(null);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initTabLayout(List<SourcesItem> sourcesItems) {
        for (SourcesItem sources : sourcesItems) {
            TabLayout.Tab tab = tablayout.newTab();
            tab.setText(sources.getName());
            tab.setTag(sources);
            tablayout.addTab(tab);
        }
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SourcesItem sourcesItem = ((SourcesItem) tab.getTag());
                getNewsBySourceId(sourcesItem.getId());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SourcesItem sourcesItem = ((SourcesItem) tab.getTag());
                getNewsBySourceId(sourcesItem.getId());
            }
        });
        tablayout.getTabAt(0).select();
    }

    public void getNewsBySourceId(String sourceID) {
        mainViewModel.getNewsResourcesById(sourceID);
    }

    private void initView() {
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tryAgainBtn = (Button) findViewById(R.id.try_again_btn);
        tryAgainBtn.setOnClickListener(MainActivity.this);
        viewNoData = (LinearLayout) findViewById(R.id.view_no_data);
        Click();
    }

    public void Click() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.Search) {
                    SearchView searchView = (SearchView) item.getActionView();
                    searchView.setQueryHint("Search Here");
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            adapter.getFilter().filter(s);
                            return false;
                        }

                    });
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.try_again_btn) {
            mainViewModel.getNewsSources();
        }
    }
}


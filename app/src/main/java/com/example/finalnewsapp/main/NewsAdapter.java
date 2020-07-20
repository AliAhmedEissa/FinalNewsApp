package com.example.finalnewsapp.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalnewsapp.R;
import com.example.finalnewsapp.model.NewsRespnse.ArticlesItem;
import com.example.finalnewsapp.repositories.SourceRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements Filterable {

    List<ArticlesItem> articles;
    List<ArticlesItem> articlesItemListFull;

    public NewsAdapter(List<ArticlesItem> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArticlesItem item = articles.get(position);
        holder.title.setText(item.title);
        holder.content.setText(item.description);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        String Time = simpleDateFormat.format(calendar.getTime());
        holder.date.setText(Time);

        if (item.source != null)
            holder.sourceName.setText(item.source.getName());

        Glide.with(holder.itemView)
                .load(item.urlToImage)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return articles==null?0:articles.size();
    }

    public void changeData(List<ArticlesItem> items){
        this.articles=items;
        if (items != null){
            articlesItemListFull = new ArrayList<>(items);
        }
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ArticlesItem> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(articlesItemListFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ArticlesItem item : articlesItemListFull){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            articles.clear();
            articles.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView sourceName;
         ImageView image;
         TextView content;
         TextView title, date;

        public ViewHolder(@NonNull View rootView) {
            super(rootView);
            sourceName =  rootView.findViewById(R.id.source_name);
            title =  rootView.findViewById(R.id.title);
            date =  rootView.findViewById(R.id.date);
            image =  rootView.findViewById(R.id.image);
            content =  rootView.findViewById(R.id.content);
        }
    }
}

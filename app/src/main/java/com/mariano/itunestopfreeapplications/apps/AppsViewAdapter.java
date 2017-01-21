package com.mariano.itunestopfreeapplications.apps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.data.Application;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by mariano on 10/01/17.
 */

public class AppsViewAdapter extends RealmRecyclerViewAdapter<Application, AppsViewAdapter.MyViewHolder> {
    private final Context context;
    private ClickListener clickListener;

    public AppsViewAdapter(Context context, OrderedRealmCollection<Application> data) {
        super(context ,data, true);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.application_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Application obj = getData().get(position);
        holder.data = obj;
        Glide.with(context).load(obj.getImage()).into(holder.logo);
        holder.title.setText(obj.getTitle());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //implements View.OnLongClickListener
        public ImageView logo;
        public TextView title;
        public Application data;

        public MyViewHolder(View view) {
            super(view);
            logo = (ImageView) view.findViewById(R.id.logo);
            title = (TextView) view.findViewById(R.id.textView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null)
            clickListener.onItemClick(getItem(getAdapterPosition()),view);
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Application obj, View v);
    }
}

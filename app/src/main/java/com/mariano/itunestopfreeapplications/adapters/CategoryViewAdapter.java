package com.mariano.itunestopfreeapplications.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mariano.itunestopfreeapplications.R;
import com.mariano.itunestopfreeapplications.models.Category;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/** TODO: no necesario, eliminar
 * Created by mariano on 10/01/17.
 */

public class CategoryViewAdapter extends RealmRecyclerViewAdapter<Category, CategoryViewAdapter.MyViewHolder> {
    private final Context context;
    private ClickListener clickListener;

    public CategoryViewAdapter(Context context, OrderedRealmCollection<Category> data) {
        super(context ,data, true);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category obj = getData().get(position);
        holder.data = obj;

        holder.title.setText(obj.getLabel());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //implements View.OnLongClickListener
        public TextView title;
        public Category data;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }

//        @Override
//        public boolean onLongClick(View v) {
//            activity.deleteItem(data);
//            return true;
//        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

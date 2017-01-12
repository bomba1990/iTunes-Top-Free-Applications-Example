package com.mariano.itunestopfreeapplications.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mariano.itunestopfreeapplications.models.Application;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/** TODO: no necesario, eliminar
 * Created by mariano on 11/01/17.
 */

public class ApplicationListViewAdapter extends RealmBaseAdapter<Application> implements ListAdapter {

    private static class ViewHolder {
        TextView timestamp;
    }

    public ApplicationListViewAdapter(Context context, OrderedRealmCollection<Application> realmResults) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.timestamp = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Application item = adapterData.get(position);
        viewHolder.timestamp.setText(item.getTitle());
        return convertView;
    }
}
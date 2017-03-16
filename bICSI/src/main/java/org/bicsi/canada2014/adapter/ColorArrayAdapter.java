package org.bicsi.canada2014.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class ColorArrayAdapter extends ArrayAdapter<Object>{
    private String[] list;
 
    public ColorArrayAdapter(Context context, int textViewResourceId,
            Object[] objects) {
        super(context, textViewResourceId, objects);
        list = new String[objects.length];
        for (int i = 0; i < list.length; i++)
            list[i] = (String) objects[i];
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (TextView)super.getView(position, convertView, parent);
        if(position%2==0)
        {
            view.setBackgroundColor(Color.parseColor("EFE4F2"));
        }
        return view;
    }
 
}

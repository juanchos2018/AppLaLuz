package tech.abralica.clinicalaluzapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import tech.abralica.clinicalaluzapp.models.Especialidad;

public class SpinAdapter2  extends ArrayAdapter<Especialidad> {

    private Context context;
    private ArrayList<Especialidad> values;

    public SpinAdapter2(@NonNull Context context, int resource, ArrayList<Especialidad> values) {
        super(context, resource);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Especialidad getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.toArray(new Object[values.size()])[position].toString());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.toArray(new Object[values.size()])[position].toString());

        return label;
    }

}

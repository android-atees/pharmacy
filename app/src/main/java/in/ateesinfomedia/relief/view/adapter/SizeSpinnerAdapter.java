package in.ateesinfomedia.relief.view.adapter;

/*
 *Created by Adithya T Raj on 02-11-2020
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.models.detailpro.ConfigValues;

public class SizeSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<ConfigValues> sizeValues;
    LayoutInflater inflater;

    public SizeSpinnerAdapter(Context applicationContext, ArrayList<ConfigValues> sizeValues) {
        this.context = context;
        this.sizeValues = sizeValues;
        this.inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return (null != sizeValues ? sizeValues.size() : 0);
    }

    @Override
    public Object getItem(int i) {
        return (null != sizeValues ? sizeValues.get(i) : null);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.holder_spinner_size, null);
        TextView names = (TextView) view.findViewById(R.id.spinner_size_txt);
        names.setText(sizeValues.get(i).getLabel());
        return view;
    }
}

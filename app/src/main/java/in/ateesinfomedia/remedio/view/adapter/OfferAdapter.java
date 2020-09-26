package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.interfaces.OfferCategoryClickListner;
import in.ateesinfomedia.remedio.models.CategoryModel;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<CategoryModel> mCategoryModelList;
    private final OfferCategoryClickListner mOfferCategoryClickListner;
    int row_position = -1;
    private boolean isClicked = false;

    public OfferAdapter(Context context, List<CategoryModel> categoryModelList, OfferCategoryClickListner clickListner) {
        this.mContext = context;
        this.mCategoryModelList = categoryModelList;
        this.mOfferCategoryClickListner = clickListner;
    }

    @NonNull
    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_offer_categories, parent, false);

        return new OfferAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.MyViewHolder holder, final int position) {

        holder.mTitle.setText(mCategoryModelList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_position = position;
                notifyDataSetChanged();
                mOfferCategoryClickListner.titleSelected(mCategoryModelList.get(position).getName(),row_position);
            }
        });

        holder.mMainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_position = position;
                notifyDataSetChanged();
                mOfferCategoryClickListner.titleSelected(mCategoryModelList.get(position).getName(),row_position);
            }
        });
        if(row_position == position){
            holder.mMainLay.setBackgroundColor(mContext.getResources().getColor(R.color.primary_color));
            holder.mTitle.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.mMainLay.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.primary_color));
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mMainLay;
        TextView mTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            mMainLay = (RelativeLayout) itemView.findViewById(R.id.main);

        }
    }
}
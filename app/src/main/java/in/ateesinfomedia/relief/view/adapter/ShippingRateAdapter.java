package in.ateesinfomedia.relief.view.adapter;

/*
 *Created by Adithya T Raj on 10-11-2020
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.interfaces.RateCLickListener;
import in.ateesinfomedia.relief.models.shipping.ShippingCost;

public class ShippingRateAdapter extends RecyclerView.Adapter<ShippingRateAdapter.MyViewHolder> {

    private final RateCLickListener mRateClickListener;
    private final List<ShippingCost> mShippingCostList;
    private final Context mContext;
    private int selectedPosition = -1;

    public ShippingRateAdapter(Context mContext, List<ShippingCost> mShippingCostList, RateCLickListener mRateClickListener) {
        this.mRateClickListener = mRateClickListener;
        this.mShippingCostList = mShippingCostList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ShippingRateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_shipping_rate, parent, false);

        return new ShippingRateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingRateAdapter.MyViewHolder holder, int position) {
        holder.bind(mShippingCostList.get(position), mRateClickListener);
        if (selectedPosition == position) {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#d3d3d3"));
        } else {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return (null != mShippingCostList ? mShippingCostList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mAmount, mMethod, mCarrier;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAmount = (TextView) itemView.findViewById(R.id.ship_rate_amount);
            mMethod = (TextView) itemView.findViewById(R.id.ship_rate_method);
            mCarrier = (TextView) itemView.findViewById(R.id.ship_rate_title);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.ship_rate_mainLayout);
        }

        void bind(ShippingCost item, RateCLickListener listener) {
            mAmount.setText(item.getAmount());
            mMethod.setText(item.getMethod_title());
            mCarrier.setText(item.getCarrier_title());

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = getAdapterPosition();
                    listener.itemClicked(getAdapterPosition(), item);
                    notifyDataSetChanged();
                }
            });
        }
    }

}

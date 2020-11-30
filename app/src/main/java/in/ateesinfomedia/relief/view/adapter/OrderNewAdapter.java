package in.ateesinfomedia.relief.view.adapter;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.AppHelpers;
import in.ateesinfomedia.relief.interfaces.OrderClickListener;
import in.ateesinfomedia.relief.models.orders.OrdersModel;

public class OrderNewAdapter extends RecyclerView.Adapter<OrderNewAdapter.MyViewHolder> {

    private final OrderClickListener mOrderClickListener;
    private final List<OrdersModel> mOrderList;
    private final Context mContext;

    public OrderNewAdapter(Context mContext, List<OrdersModel> mOrderList, OrderClickListener mOrderClickListener) {
        this.mOrderClickListener = mOrderClickListener;
        this.mOrderList = mOrderList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_new_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mOrderList.get(position), mOrderClickListener);
    }

    @Override
    public int getItemCount() {
        return (null != mOrderList ? mOrderList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mDate, mAddress, mOrderId, mStatus, mTotalAmt, mViewOrder;
        MaterialCardView mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.holder_order_date);
            mAddress = (TextView) itemView.findViewById(R.id.holder_order_ship_address);
            mOrderId = (TextView) itemView.findViewById(R.id.holder_order_id);
            mStatus = (TextView) itemView.findViewById(R.id.holder_order_status);
            mTotalAmt = (TextView) itemView.findViewById(R.id.holder_order_amount);
            mViewOrder = (TextView) itemView.findViewById(R.id.holder_order_detail);
            mainLayout = (MaterialCardView) itemView.findViewById(R.id.holder_order_mainLayout);
        }

        void bind(OrdersModel item, OrderClickListener listener) {
            mOrderId.setText("#" + item.getIncrement_id());
            float price = Float.parseFloat(item.getGrand_total());
            mTotalAmt.setText("₹ " + String.format("%.2f", price));
            int drawableBg = R.drawable.bg_order_processing;
            String status = "Processing";
            if (item.getStatus().equals("pending")) {
                drawableBg = R.drawable.bg_order_pending;
                status = "Pending";
            } else if (item.getStatus().equals("completed")) {
                drawableBg = R.drawable.bg_order_complete;
                status = "Completed";
            } else if (item.getStatus().equals("cancelled")) {
                drawableBg = R.drawable.bg_order_cancelled;
                status = "Cancelled";
            }
            mStatus.setText(status);
            mStatus.setBackgroundResource(drawableBg);

            if (item.getExtension_attributes() != null
                    && item.getExtension_attributes().getShipping_assignments()!= null
                    && !item.getExtension_attributes().getShipping_assignments().isEmpty()) {
                if (item.getExtension_attributes().getShipping_assignments().get(0).getShipping() != null
                        && item.getExtension_attributes().getShipping_assignments().get(0).getShipping().getAddress() != null) {
                    String fName = item.getExtension_attributes().getShipping_assignments().get(0).getShipping().getAddress().getFirstname();
                    mAddress.setText("Ship to: " + fName);
                }
                if (item.getExtension_attributes().getShipping_assignments().get(0).getItems() != null
                        && !item.getExtension_attributes().getShipping_assignments().get(0).getItems().isEmpty()) {
                    String orderDateTime = item.getExtension_attributes().getShipping_assignments().get(0).getItems().get(0).getCreated_at();
                    mDate.setText(AppHelpers.convertDate(orderDateTime));
                }
            }

            mViewOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTrackClicked(getAdapterPosition(), item);
                }
            });
        }
    }

}

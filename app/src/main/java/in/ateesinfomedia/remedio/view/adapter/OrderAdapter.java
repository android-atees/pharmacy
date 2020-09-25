package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.models.OrderModel;
import in.ateesinfomedia.remedio.view.activity.OrderDetailsActivity;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<OrderModel> mOrderList;
    private final AdapterClickListner mAdapterClickListner;
    private OrderModel orderModel;

    public OrderAdapter(Context context, List<OrderModel> orderList, AdapterClickListner clickListner) {
        this.mContext = context;
        this.mOrderList = orderList;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_order_list, parent, false);

        return new OrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {

        if (mOrderList.get(position).getStatus().equals("0")){
            holder.tvProcess.setVisibility(View.VISIBLE);
            holder.tvTrack.setVisibility(View.GONE);
        } else {
            holder.tvProcess.setVisibility(View.GONE);
            holder.tvTrack.setVisibility(View.VISIBLE);
        }

        holder.tvPresId.setText(mOrderList.get(position).getUnique_id());
        holder.tvDate.setText(mOrderList.get(position).getDate());
        if(mOrderList.get(position).getTotal().equals("null")){
//            holder.tvTotal.setText("₹ 00");
            holder.tvTotal.setText("");
        } else{
            float total = Float.valueOf(mOrderList.get(position).getTotal());
            holder.tvTotal.setText("₹ "+total);
        }
        holder.tvTotalItems.setText(mOrderList.get(position).getTotal());

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrack,tvProcess,tvTotalItems,tvTotal,tvDate,tvPresId,tvPresOrProdu;
        CardView mItemCard;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvPresOrProdu = (TextView) itemView.findViewById(R.id.tvPresOrProdu);
            tvTrack = (TextView) itemView.findViewById(R.id.tvTrack);
            tvProcess = (TextView) itemView.findViewById(R.id.tvProcess);
            tvTotalItems = (TextView) itemView.findViewById(R.id.tvTotalItems);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvPresId = (TextView) itemView.findViewById(R.id.tvPresId);
            mItemCard = (CardView) itemView.findViewById(R.id.cardView);

            tvTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderModel = mOrderList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),orderModel);
                }
            });

            tvProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderModel = mOrderList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),orderModel);
                }
            });
        }
    }
}
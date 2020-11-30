package in.ateesinfomedia.relief.view.adapter;

/*
 *Created by Adithya T Raj on 12-11-2020
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.models.orders.OrderDetailItem;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<OrderDetailItem> mOrderDetailList;

    public OrderProductAdapter(Context mContext, List<OrderDetailItem> mOrderDetailList) {
        this.mContext = mContext;
        this.mOrderDetailList = mOrderDetailList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_order_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mOrderDetailList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != mOrderDetailList ? mOrderDetailList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mProductName, mPrice, mQty, mSubTotal;
        AppCompatImageView mProductImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mProductName = (TextView) itemView.findViewById(R.id.holder_order_product_name);
            //mPrice = (TextView) itemView.findViewById(R.id.holder_order_product_price);
            mQty = (TextView) itemView.findViewById(R.id.holder_order_product_qty);
            mSubTotal = (TextView) itemView.findViewById(R.id.holder_order_product_sub_total);
            mProductImage = (AppCompatImageView) itemView.findViewById(R.id.holder_order_product_img);
        }

        void bind(OrderDetailItem item) {
            mProductName.setText(item.getName());
            float price = Float.parseFloat(item.getPrice());
            float qtyFloat = Float.parseFloat(item.getQty_ordered());
            int qty = Math.round(qtyFloat);
            float subTotal = price * qty;
            //mPrice.setText("₹ " + String.format("%.2f", price));
            mQty.setText("QTY : " + qty);
            mSubTotal.setText("₹ " + String.format("%.2f", subTotal));
            Glide.with(mContext)
                    .load(item.getImage())
                    .into(mProductImage);
        }
    }

}

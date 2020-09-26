package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.models.OrderDetails;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.MyViewHolder> {

    private final List<OrderDetails> mDetailsList;
    private final Context mContext;

    public OrderItemsAdapter(Context context, List<OrderDetails> detailsList) {
        this.mContext = context;
        this.mDetailsList = detailsList;
    }

    @NonNull
    @Override
    public OrderItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_order_items, parent, false);

        return new OrderItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsAdapter.MyViewHolder holder, int position) {

        holder.itemName.setText(mDetailsList.get(position).getMedicine_name());
        holder.itemPrice.setText("₹ "+mDetailsList.get(position).getMedicine_price());
        holder.itemQuantity.setText(mDetailsList.get(position).getQuantity());

        holder.manufacturer.setText(mDetailsList.get(position).getManufacture_name());
        holder.itemMrp.setText("₹ "+mDetailsList.get(position).getMedicine_mrp());
        holder.expiry.setText(mDetailsList.get(position).getExpairy_date());
        holder.cgstPercentage.setText(mDetailsList.get(position).getCgst_percentage()+"%");
        holder.cgstAmount.setText("₹ "+mDetailsList.get(position).getCgst_amount());
        holder.sgstPercentage.setText(mDetailsList.get(position).getSgst_percentage()+"%");
        holder.sgstAmount.setText("₹ "+mDetailsList.get(position).getSgst_amount());
        holder.hsncode.setText(mDetailsList.get(position).getHsn_code());

//        holder.gstPercentage.setText(mDetailsList.get(position).getGst_percentage()+"%");
//        holder.gstAmount.setText("₹ "+mDetailsList.get(position).getGst());
        holder.total.setText("₹ "+mDetailsList.get(position).getTotal_amount());
    }

    @Override
    public int getItemCount() {
        return mDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName,itemPrice,itemQuantity,gstPercentage,gstAmount,total,manufacturer,
                itemMrp,expiry,cgstPercentage,cgstAmount,sgstPercentage,sgstAmount,hsncode;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantity);

            manufacturer = (TextView) itemView.findViewById(R.id.manufacturer);
            itemMrp = (TextView) itemView.findViewById(R.id.itemMrp);
            expiry = (TextView) itemView.findViewById(R.id.expiry);
            cgstPercentage = (TextView) itemView.findViewById(R.id.cgstPercentage);
            cgstAmount = (TextView) itemView.findViewById(R.id.cgstAmount);
            sgstPercentage = (TextView) itemView.findViewById(R.id.sgstPercentage);
            sgstAmount = (TextView) itemView.findViewById(R.id.sgstAmount);
            hsncode = (TextView) itemView.findViewById(R.id.hsncode);

//            gstPercentage = (TextView) itemView.findViewById(R.id.gstPercentage);
//            gstAmount = (TextView) itemView.findViewById(R.id.gstAmount);
            total = (TextView) itemView.findViewById(R.id.total);
        }
    }
}

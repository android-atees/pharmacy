package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.interfaces.MyAdapterClickListner;

public class OtcOrderAdapter extends RecyclerView.Adapter<OtcOrderAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<OtcOrderModel> mOtcOrderModel;
    private final MyAdapterClickListner mAdapterClickListner;

    public OtcOrderAdapter(Context context, List<OtcOrderModel> otcOrderModels, MyAdapterClickListner clickListner) {
        this.mContext = context;
        this.mOtcOrderModel = otcOrderModels;
        this.mAdapterClickListner = clickListner;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_otc_order_items, parent, false);

        return new OtcOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.orderId.setText("ORDER ID : "+mOtcOrderModel.get(position).getOrder_id());
        holder.date.setText("Date : "+mOtcOrderModel.get(position).getCreate_date());
        holder.itemCount.setText("TOTAL ITEM : "+mOtcOrderModel.get(position).getCount());
        holder.amount.setText("₹ "+mOtcOrderModel.get(position).getTotal());

    }

    @Override
    public int getItemCount() {
        return mOtcOrderModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderId,date,amount,itemCount;
        Button customerSupport,trackOrder;

        public MyViewHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.orderId);
            date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);
            itemCount = (TextView) itemView.findViewById(R.id.itemCount);

            customerSupport = (Button) itemView.findViewById(R.id.btnCustomerSupport);
            trackOrder = (Button) itemView.findViewById(R.id.btnTrackOrder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
//                    mContext.startActivity(intent);

                    mAdapterClickListner.clicked(v,getAdapterPosition());
                }
            });

            trackOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterClickListner.clicked(v,getAdapterPosition());
                }
            });

            customerSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAdapterClickListner.Applyclicked(v,0,0,"");
                }
            });

        }
    }
}

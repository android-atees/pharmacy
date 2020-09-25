package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.models.OtcOrderDetailsModel;

public class OtcOrderDeatailsAdapter extends RecyclerView.Adapter<OtcOrderDeatailsAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<OtcOrderDetailsModel> mOtcOrderDetailsModel;
    public AdapterClickListner clickListner;
    private String[] main = new String[]{"Draft","Confirmed","Processing","Shipped","Delivered"};
    private String[] main1 = new String[]{"Draft","Confirmed","Processing","Shipped","Not Delivered"};
    private String[] main2 = new String[]{"Draft","Confirmed","Processing","Shipped","Return"};
    private String[] main3 = new String[]{"Draft","Confirmed","Processing","Shipped","Cancelled"};
    private String[] main4 = new String[]{"Draft","Confirmed","Processing","Shipped","Refund"};

    public OtcOrderDeatailsAdapter(Context context, List<OtcOrderDetailsModel> orderDetailsModelList, AdapterClickListner clickListner) {
        this.mContext = context;
        this.mOtcOrderDetailsModel = orderDetailsModelList;
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public OtcOrderDeatailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_order_details_list, parent, false);
        return new OtcOrderDeatailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OtcOrderDeatailsAdapter.MyViewHolder holder, int position) {
        holder.itemPrice.setText("₹ "+mOtcOrderDetailsModel.get(position).getProduct_price());
        holder.quantity.setText(mOtcOrderDetailsModel.get(position).getQty());
        holder.menuName.setText(mOtcOrderDetailsModel.get(position).getProduct_name());

        float price = Float.valueOf(mOtcOrderDetailsModel.get(position).getProduct_price());
        float quan = Float.valueOf(mOtcOrderDetailsModel.get(position).getQty());
        float total = price * quan;
        holder.totalPrice.setText("₹ "+total);

//        holder.totalPrice.setText("₹ "+mOtcOrderDetailsModel.get(position).getTotal());
//        holder.ResName.setText(mOtcOrderDetailsModel.get(position).getResName());

        Glide.with(mContext).load(Apis.PRODUCT_IMAGE_BASE_URL + mOtcOrderDetailsModel.get(position).getProduct_image1()).into(holder.menuImage);

        if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("1")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main);
            holder.tracker.setCurrentStep(1);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("2")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main);
            holder.tracker.setCurrentStep(2);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("3")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main);
            holder.tracker.setCurrentStep(3);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("4")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main);
            holder.tracker.setCurrentStep(4);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("5")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main);
            holder.TvCancelItem.setVisibility(View.GONE);
            holder.tracker.setCurrentStep(5);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("6")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main1);
            holder.TvCancelItem.setVisibility(View.GONE);
            holder.tracker.setCurrentStep(5);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("7")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main2);
            holder.TvCancelItem.setVisibility(View.GONE);
            holder.tracker.setCurrentStep(5);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("8")){
            holder.tracker.setVisibility(View.VISIBLE);
            holder.TvCancel.setVisibility(View.GONE);
            holder.tracker.setLabels(main4);
            holder.TvCancelItem.setVisibility(View.GONE);
            holder.tracker.setCurrentStep(5);
        } else if (mOtcOrderDetailsModel.get(position).getOrder_status().equals("0")){
            holder.tracker.setVisibility(View.GONE);
            holder.TvCancel.setVisibility(View.VISIBLE);
            holder.TvCancelItem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mOtcOrderDetailsModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView totalPrice,quantity,itemPrice,menuName,ResName,TvCancel,TvCancelItem;
        StepperIndicator tracker;
        ImageView menuImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            totalPrice = (TextView) itemView.findViewById(R.id.totalPrice);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            tracker = (StepperIndicator) itemView.findViewById(R.id.tracker);
            menuName = (TextView) itemView.findViewById(R.id.menuName);
            menuImage = (ImageView) itemView.findViewById(R.id.menuImage);
            ResName = (TextView) itemView.findViewById(R.id.resName);
            TvCancel = (TextView) itemView.findViewById(R.id.tv_cancel);
            TvCancelItem = (TextView) itemView.findViewById(R.id.tv_cancel_order);

//            TvCancelItem.setVisibility(View.GONE);

            TvCancelItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OtcOrderDetailsModel otcOrderDetailsModel = mOtcOrderDetailsModel.get(getAdapterPosition());
                    clickListner.itemClicked(getAdapterPosition(),otcOrderDetailsModel);
                }
            });

        }
    }
}
package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.MyAdapterClickListner;
import in.ateesinfomedia.remedio.models.ProductsModel;

public class OfferProductsAdapter extends RecyclerView.Adapter<OfferProductsAdapter.MyViewHolder> {

    private final AdapterClickListner mAdapterClickListner;
    private final List<ProductsModel> mProductsModelList;
    private final Context mContext;

    public OfferProductsAdapter(Context context, List<ProductsModel> productsModelList, AdapterClickListner clickListner) {

        this.mContext = context;
        this.mProductsModelList = productsModelList;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public OfferProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_offer_products, parent, false);

        return new OfferProductsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferProductsAdapter.MyViewHolder holder, int position) {
        ProductsModel productsModel = mProductsModelList.get(position);
        holder.mProductName.setText(productsModel.getProduct_name());
        holder.mPrice.setText("₹ "+productsModel.getProduct_total_offer());
        holder.mOfferPrice.setText("₹ "+productsModel.getProduct_total());

        String offerprice = productsModel.getProduct_total();
        String prices = productsModel.getProduct_total_offer();

        if (offerprice.contains(".")){
            String[] arrStr = offerprice.split("\\.", 2);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                holder.mOfferPrice.setText("₹ "+arrStr[0]+"."+second);
            } else {
                holder.mOfferPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }
        } else {
            holder.mOfferPrice.setText("₹ " + offerprice);
        }

        if (prices.contains(".")){
            String[] arrStr = prices.split("\\.", 2);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                holder.mPrice.setText("₹ "+arrStr[0]+"."+second);
            } else {
                holder.mPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }
        } else {
            holder.mPrice.setText("₹ " + offerprice);
        }


        strikeThroughText(holder.mOfferPrice);

        float price = Float.valueOf(productsModel.getProduct_total_offer());
        float strikePrice = Float.valueOf(productsModel.getProduct_total());
        float percen = (strikePrice-price)/strikePrice*100;

        holder.mOffPercentage.setText("SAVE UPTO "+Math.round(percen)+"% off");

        Glide.with(mContext).load(Apis.PRODUCT_IMAGE_BASE_URL+ mProductsModelList.get(position).getProduct_image1()).into(holder.mProductImage);
    }

    @Override
    public int getItemCount() {
        return mProductsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mProductName,mPrice,mOfferPrice,mOffPercentage;
        ImageView mProductImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            mProductName = (TextView) itemView.findViewById(R.id.productName);
            mPrice = (TextView) itemView.findViewById(R.id.mainprice);
            mOfferPrice = (TextView) itemView.findViewById(R.id.offerprice);
            mOffPercentage = (TextView) itemView.findViewById(R.id.offpercentage);
            mProductImage = (ImageView) itemView.findViewById(R.id.productImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductsModel productsModel = mProductsModelList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),productsModel);
                }
            });
        }
    }

    private void strikeThroughText(TextView price){
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
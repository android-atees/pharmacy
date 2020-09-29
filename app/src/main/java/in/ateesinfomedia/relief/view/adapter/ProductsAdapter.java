package in.ateesinfomedia.relief.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.models.ProductsModel;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<ProductsModel> mProductsModelList;
    private final AdapterClickListner mAdapterClickListner;

    public ProductsAdapter(Context context, List<ProductsModel> productsModelList, AdapterClickListner clickListner) {
        this.mContext = context;
        this.mProductsModelList = productsModelList;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_products, parent, false);

        return new ProductsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.MyViewHolder holder, int position) {

        holder.mProductName.setText(mProductsModelList.get(position).getProduct_name());
//        holder.mProductPrice.setText("₹ " + mProductsModelList.get(position).getProduct_total_offer());
        if (mProductsModelList.get(position).getProduct_total_offer().contains(".")){
            String splite = mProductsModelList.get(position).getProduct_total_offer();
            Log.d("splittttttt",splite);
            String[] arrStr = splite.split("\\.");
            Log.d("ttttttt",arrStr[0]);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                holder.mProductPrice.setText("₹ "+arrStr[0]+"."+second);
//                Log.d("ttttttt",arrStr[0]+"."+second);
            } else {
                holder.mProductPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }

        } else {
            holder.mProductPrice.setText("₹ " + mProductsModelList.get(position).getProduct_total_offer());
        }
        Glide.with(mContext).load(Apis.PRODUCT_IMAGE_BASE_URL + mProductsModelList.get(position).getProduct_image1()).into(holder.mProductImage);

    }

    @Override
    public int getItemCount() {
        return mProductsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mProductName,mProductPrice;
        ImageView mProductImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            mProductImage = (ImageView) itemView.findViewById(R.id.product_image);
            mProductName = (TextView) itemView.findViewById(R.id.product_name);
            mProductPrice = (TextView) itemView.findViewById(R.id.product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductsModel productsModel = mProductsModelList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),productsModel);
                }
            });

        }
    }
}

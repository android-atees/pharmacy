package in.ateesinfomedia.relief.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;
import in.ateesinfomedia.relief.models.products.ProductModel;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<ProductModel> mProductsModelList;
    private final AdapterClickListner mAdapterClickListner;

    public ProductsAdapter(Context context, List<ProductModel> productsModelList, AdapterClickListner clickListner) {
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

        holder.mProductName.setText(mProductsModelList.get(position).getName());
        float price = Float.parseFloat(mProductsModelList.get(position).getPrice());
        holder.mProductPrice.setText("₹ " + String.format("%.2f", price));
        /*if (mProductsModelList.get(position).getProduct_total_offer().contains(".")){
            String splite = mProductsModelList.get(position).getProduct_total_offer();
            Log.d("splittttttt",splite);
            String[] arrStr = splite.split("\\.");
            Log.d("ttttttt",arrStr[0]);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                holder.mProductPrice.setText("₹ "+arrStr[0]+"."+second);
                //Log.d("ttttttt",arrStr[0]+"."+second);
            } else {
                holder.mProductPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }

        } else {
            holder.mProductPrice.setText("₹ " + mProductsModelList.get(position).getProduct_total_offer());
        }*/
        ArrayList<MediaGalleryEntries> imageList = mProductsModelList.get(position).getMedia_gallery_entries();
        if (imageList != null && !imageList.isEmpty() && imageList.get(0).getFile() != null) {
            Glide.with(mContext)
                    .load(Apis.NEW_PRODUCT_IMAGE_BASE_URL + imageList.get(0).getFile())
                    .into(holder.mProductImage);
        }
        if (mProductsModelList.get(position).getType_id() != null &&
            mProductsModelList.get(position).getType_id().equals("configurable")) {
            holder.mProductButton.setText("VIEW");
        } else {
            holder.mProductButton.setText("ADD TO CART");
        }

    }

    @Override
    public int getItemCount() {
        return mProductsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mProductName,mProductPrice;
        ImageView mProductImage;
        Button mProductButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            mProductImage = (ImageView) itemView.findViewById(R.id.product_image);
            mProductName = (TextView) itemView.findViewById(R.id.product_name);
            mProductPrice = (TextView) itemView.findViewById(R.id.product_price);
            mProductButton = (Button) itemView.findViewById(R.id.product_add_cart_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductModel productsModel = mProductsModelList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),productsModel);
                }
            });

            mProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel productsModel = mProductsModelList.get(getAdapterPosition());
                    mAdapterClickListner.buttonClicked(getAdapterPosition(),productsModel);
                }
            });

        }
    }

    public void addProducts(List<ProductModel> newList) {
        mProductsModelList.addAll(newList);
        notifyDataSetChanged();
    }

    public void clearProducts(List<ProductModel> newList) {
        mProductsModelList.clear();
        mProductsModelList.addAll(newList);
        notifyDataSetChanged();
    }
}

package in.ateesinfomedia.relief.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.CartItemClickListner;
import in.ateesinfomedia.relief.models.CartsModel;
import in.ateesinfomedia.relief.models.cart.CartModel;
import me.himanshusoni.quantityview.QuantityView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{

    private final CartItemClickListner mCartItemClickListner;
    private final List<CartModel> mCartModels;
    private final Context mContext;

    public CartAdapter(Context context, List<CartModel> cartModels, CartItemClickListner cartItemClickListner) {

        this.mContext = context;
        this.mCartModels = cartModels;
        this.mCartItemClickListner = cartItemClickListner;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_cart, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.MyViewHolder holder, int position) {

        holder.mProductName.setText(mCartModels.get(position).getName());
        holder.mQuantity.setText(mCartModels.get(position).getQty());
        Glide.with(mContext).load(mCartModels.get(position).getImage()).into(holder.mproductImage);
        float price = Float.parseFloat(mCartModels.get(position).getPrice());
        int qty = Integer.parseInt(mCartModels.get(position).getQty());
        float total = price * qty;
        holder.mPrice.setText("₹ " + String.format("%.2f", total));

        holder.bind(mCartModels.get(position), mCartItemClickListner);


        /*holder.mProductName.setText(mCartModels.get(position).getProduct_name());
        String offerprice = String.valueOf(Integer.valueOf(mCartModels.get(position).getQuantity()) * Float.valueOf(mCartModels.get(position).getProduct_total_offer()));
        if (offerprice.contains(".")){
            String[] arrStr = offerprice.split("\\.", 2);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                holder.mPrice.setText("₹ "+arrStr[0]+"."+second);
            } else {
                holder.mPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }
        } else {
            holder.mPrice.setText("₹ " + offerprice);
        }
        holder.quantityView.setQuantity(Integer.valueOf(mCartModels.get(position).getQuantity()));

        Glide.with(mContext).load(Apis.PRODUCT_IMAGE_BASE_URL + mCartModels.get(position).getProduct_image1()).into(holder.mproductImage);

        holder.quantityView.setTag(position);

        holder.quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean b) {
                Integer pos = (Integer) holder.quantityView.getTag();
                if (newQuantity>oldQuantity){
                    mCartItemClickListner.onEditQuantityClicked(pos,"plus",newQuantity);
                } else {
                    mCartItemClickListner.onEditQuantityClicked(pos,"minus",newQuantity);
                }
            }

            @Override
            public void onLimitReached() {
                Toast.makeText(mContext, "You reached the maximum limit.", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mCartModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mProductName,mQuantity,mPrice;
        ImageView mproductImage,mRemove;
        //QuantityView quantityView;
        Button mCartIncrement, mCartDecrement;

        public MyViewHolder(View itemView) {
            super(itemView);

            //quantityView = (QuantityView) itemView.findViewById(R.id.quantityCountPickerCart);
            mProductName = (TextView) itemView.findViewById(R.id.product_name);
            mPrice = (TextView) itemView.findViewById(R.id.price);
            mRemove = (ImageView) itemView.findViewById(R.id.remove);
            mproductImage = (ImageView) itemView.findViewById(R.id.product_image);
            mCartIncrement = (Button) itemView.findViewById(R.id.cart_increment);
            mCartDecrement = (Button) itemView.findViewById(R.id.cart_decrement);
            mQuantity = (TextView) itemView.findViewById(R.id.cart_qty);
        }

        void bind(CartModel item, CartItemClickListner listener) {
            int qty = Integer.parseInt(item.getQty());
            mCartIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (qty == 10) {
                        Toast.makeText(mContext, "You reached the maximum limit.", Toast.LENGTH_SHORT).show();
                    } else {
                        listener.onEditQuantityClicked(getAdapterPosition(),"plus", qty + 1, item);
                    }
                }
            });
            mCartDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (qty == 1) {
                        Toast.makeText(mContext, "You reached the minimum limit.", Toast.LENGTH_SHORT).show();
                    } else {
                        listener.onEditQuantityClicked(getAdapterPosition(),"minus", qty - 1, item);
                    }
                }
            });
            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveItemClicked(getAdapterPosition(), item);
                }
            });
        }
    }
}
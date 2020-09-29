package in.ateesinfomedia.relief.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.models.CartModel;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<CartModel> mCartList;

    public CheckOutAdapter(Context context, List<CartModel> cartList) {
        this.mContext = context;
        this.mCartList = cartList;
    }

    @NonNull
    @Override
    public CheckOutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_check_out_list, parent, false);

        return new CheckOutAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutAdapter.MyViewHolder holder, int position) {
        holder.mFoodName.setText(mCartList.get(position).getProduct_name());
        holder.mResName.setText(""+mCartList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mFoodName,mResName;

        public MyViewHolder(View itemView) {
            super(itemView);

            mFoodName = (TextView) itemView.findViewById(R.id.foodName);
            mResName = (TextView) itemView.findViewById(R.id.restaurantName);

        }
    }
}

package in.ateesinfomedia.relief.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;

public class MoreMenusAdapter extends RecyclerView.Adapter<MoreMenusAdapter.MyViewHolder> {

    private final Context mContext;
    private final AdapterClickListner mAdapterClickListner;
//    String [] names = new String[] { "Profile","My Orders", "Home",
//            "Upload Order"/*, "View Offers"*/, "Rewards", "Rate App", "Customers Support", "Refer And Earn","Sign Out"};

    String [] names = new String[] {"Home","Profile","Upload Order","My Orders","Refer And Earn","Rewards","Customers Support","Rate App","Sign Out" };

//    Integer [] images = new Integer[] {R.drawable.ic_more_profile,R.drawable.ic_more_orders,
//            R.drawable.ic_more_home,R.drawable.ic_more_upload/*,R.drawable.ic_more_offers*/,
//            R.drawable.ic_more_rewards,R.drawable.ic_more_rate_us,R.drawable.ic_more_customer_support,
//            R.drawable.ic_more_refer,R.drawable.ic_more_logout};

    Integer [] images = new Integer[] {R.drawable.ic_more_home,R.drawable.ic_more_profile,
            R.drawable.ic_more_upload,R.drawable.ic_more_orders,R.drawable.ic_more_refer,
            R.drawable.ic_more_rewards,R.drawable.ic_more_customer_support,R.drawable.ic_more_rate_us,
            R.drawable.ic_more_logout};

    public MoreMenusAdapter(Context context, AdapterClickListner adapterClickListner) {

        this.mContext = context;
        this.mAdapterClickListner = adapterClickListner;
    }

    @NonNull
    @Override
    public MoreMenusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaper_more_menu_item, parent,false);
        MoreMenusAdapter.MyViewHolder viewHolder1 = new MoreMenusAdapter.MyViewHolder(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull MoreMenusAdapter.MyViewHolder holder, int position) {
        holder.names.setText(names[position]);
        holder.icons.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout llContainer;
        ImageView icons;
        TextView names;

        public MyViewHolder(View itemView) {
            super(itemView);

            icons = (ImageView) itemView.findViewById(R.id.icons);
            names = (TextView) itemView.findViewById(R.id.names);


            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_more_item_container);
            llContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapterClickListner.itemClicked(getAdapterPosition(),names);
        }
    }
}

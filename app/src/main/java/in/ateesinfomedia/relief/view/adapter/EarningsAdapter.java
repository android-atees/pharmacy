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
import in.ateesinfomedia.relief.models.EarningsModel;

public class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<EarningsModel> mEarningsModelList;

    public EarningsAdapter(Context context, List<EarningsModel> modelList) {
        this.mContext = context;
        this.mEarningsModelList = modelList;
    }

    @NonNull
    @Override
    public EarningsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_earnings, parent, false);

        return new EarningsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningsAdapter.MyViewHolder holder, int position) {

        holder.name.setText(mEarningsModelList.get(position).getName());
        holder.date.setText(mEarningsModelList.get(position).getCreate_date());
        holder.number.setText(mEarningsModelList.get(position).getPhone());
        holder.price.setText("₹ "+mEarningsModelList.get(position).getRef_amount());

    }

    @Override
    public int getItemCount() {
        return mEarningsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,date,number,price;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            number = (TextView) itemView.findViewById(R.id.number);
            price = (TextView) itemView.findViewById(R.id.price);

        }
    }
}

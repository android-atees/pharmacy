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
import in.ateesinfomedia.relief.models.NotificationModel;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private final List<NotificationModel> mNotificationModelList;
    private final Context mContext;

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.mNotificationModelList = notificationModelList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_notification, parent, false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        holder.message.setText(mNotificationModelList.get(position).getMessage());
        holder.date.setText(mNotificationModelList.get(position).getDate());
//        Toast.makeText(mContext, ""+mNotificationModelList.get(position).getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return mNotificationModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView message,date;

        public MyViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.msg);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }
}

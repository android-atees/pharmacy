package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.models.ReviewModel;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<ReviewModel> mReviewModelList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewModelList) {

        this.mContext = context;
        this.mReviewModelList = reviewModelList;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_review, parent, false);

        return new ReviewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        Log.d("adapterrrrrrr","yes");
        holder.mName.setText(mReviewModelList.get(position).getFirstname());
        holder.mDate.setText(mReviewModelList.get(position).getCreate_date());
        holder.mReview.setText(mReviewModelList.get(position).getReview());
        holder.mRating.setText(mReviewModelList.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return mReviewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mName,mDate,mReview,mRating;

        public MyViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.name);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mReview = (TextView) itemView.findViewById(R.id.review);
            mRating = (TextView) itemView.findViewById(R.id.rating);

        }
    }
}

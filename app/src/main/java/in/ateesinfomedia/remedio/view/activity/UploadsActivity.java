package in.ateesinfomedia.remedio.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import java.util.List;

import in.ateesinfomedia.remedio.Gallery.AGVRecyclerViewAdapter;
import in.ateesinfomedia.remedio.Gallery.AsymmetricRecyclerView;
import in.ateesinfomedia.remedio.Gallery.AsymmetricRecyclerViewAdapter;
import in.ateesinfomedia.remedio.Gallery.DemoItem;
import in.ateesinfomedia.remedio.Gallery.DemoUtils;
import in.ateesinfomedia.remedio.Gallery.SpacesItemDecoration;
import in.ateesinfomedia.remedio.R;

public class UploadsActivity extends AppCompatActivity {

    private final DemoUtils demoUtils = new DemoUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        AsymmetricRecyclerView recyclerView = (AsymmetricRecyclerView) findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(demoUtils.moarItems(50));
        recyclerView.setRequestedColumnCount(3);
        recyclerView.setDebugging(true);
        recyclerView.setRequestedHorizontalSpacing(Utils.dpToPx(this, 3));
        recyclerView.addItemDecoration(
                new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_padding)));
        recyclerView.setAdapter(new AsymmetricRecyclerViewAdapter<>(this, recyclerView, adapter));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}
class RecyclerViewAdapter extends AGVRecyclerViewAdapter<ViewHolder> {
    private final List<DemoItem> items;

    RecyclerViewAdapter(List<DemoItem> items) {
        this.items = items;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerViewActivity", "onCreateView");
        return new ViewHolder(parent, viewType);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("RecyclerViewActivity", "onBindView position=" + position);
        holder.bind(items.get(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public AsymmetricItem getItem(int position) {
        return items.get(position);
    }

    @Override public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }
}
class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    ViewHolder(ViewGroup parent, int viewType) {
        super(LayoutInflater.from(parent.getContext()).inflate(
                viewType == 0 ? R.layout.adapter_item : R.layout.adapter_item_odd, parent, false));
        if (viewType == 0) {
            textView = (TextView) itemView.findViewById(R.id.textview);
        } else {
            textView = (TextView) itemView.findViewById(R.id.textview_odd);
        }
    }

    void bind(DemoItem item) {
        textView.setText(String.valueOf(item.getPosition()));
    }
}

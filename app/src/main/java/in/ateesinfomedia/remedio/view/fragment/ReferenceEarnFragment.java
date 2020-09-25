package in.ateesinfomedia.remedio.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Global;
import in.ateesinfomedia.remedio.models.EarningsModel;
import in.ateesinfomedia.remedio.view.adapter.DoctorsAdapter;
import in.ateesinfomedia.remedio.view.adapter.EarningsAdapter;

public class ReferenceEarnFragment extends Fragment {
    private View mView;
    private RecyclerView mreferenceRecycler;
    private List<EarningsModel> earningsModelList = new ArrayList<>();
    private EarningsAdapter mAdapter;
    private TextView mTvNodata;

    public static ReferenceEarnFragment getInstance() {
        ReferenceEarnFragment referenceEarnFragment = new ReferenceEarnFragment();

        return referenceEarnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_reference_earn, container, false);

        mreferenceRecycler = (RecyclerView) mView.findViewById(R.id.reference_recycler);
        mTvNodata = (TextView) mView.findViewById(R.id.nodata);

        earningsModelList.clear();
        earningsModelList = Global.ReferenceEarnList;

        if (earningsModelList.size()<1 || earningsModelList == null){
            mreferenceRecycler.setVisibility(View.GONE);
            mTvNodata.setVisibility(View.VISIBLE);
        } else {
            mreferenceRecycler.setVisibility(View.VISIBLE);
            mTvNodata.setVisibility(View.GONE);

            mreferenceRecycler.setNestedScrollingEnabled(true);
            mAdapter = new EarningsAdapter(getActivity(),earningsModelList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        ShimmerRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mreferenceRecycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
            mreferenceRecycler.setItemAnimator(new DefaultItemAnimator());
            mreferenceRecycler.setAdapter(mAdapter);
        }



        return mView;
    }
}

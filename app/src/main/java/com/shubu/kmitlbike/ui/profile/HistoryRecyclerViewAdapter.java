package com.shubu.kmitlbike.ui.profile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.ProfileHistory;
import com.shubu.kmitlbike.data.model.Timestamps;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder> {

    List<ProfileHistory> histories;

    public HistoryRecyclerViewAdapter(List<ProfileHistory> histories){
        this.histories = histories;
    }

    public void updateHistoryList(List<ProfileHistory> histories){
        this.histories.clear();
        this.histories.addAll(histories);
        this.notifyDataSetChanged();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_card, parent, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        ProfileHistory history = this.histories.get(position);
        holder.time.setText( history.getTimestamps().getBorrowDate() + " " + history.getTimestamps().getBorrowTime() );
        holder.duration.setText( Integer.toString(history.getDuration()) + " min");
        holder.bikeModel.setText( history.getBike().getBikeModel() );
        holder.distances.setText( history.getDistance() );

    }


    @Override
    public int getItemCount() {
        return this.histories.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView time;
        TextView duration;
        TextView bikeModel;
        TextView distances;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.ProfileHistoryCard);
            time = (TextView) itemView.findViewById(R.id.ProfileCardTimeText);
            duration = (TextView) itemView.findViewById(R.id.ProfileCardDurationText);
            bikeModel = (TextView) itemView.findViewById(R.id.ProfileCardBikeTypeText);
            distances = (TextView) itemView.findViewById(R.id.profileCardDistanceText);
        }
    }
}

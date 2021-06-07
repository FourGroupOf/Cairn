package com.GOF.cairn.ui.favourites;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.R;
import com.GOF.cairn.SavedPOI;
import com.GOF.cairn.ui.start.actRegister;


import java.util.List;


public class MySavedPointsRecyclerViewAdapter extends RecyclerView.Adapter<MySavedPointsRecyclerViewAdapter.ViewHolder> {

    private final List<SavedPOI> mValues;


    public MySavedPointsRecyclerViewAdapter(List<SavedPOI> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fav_l, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SavedPOI item = mValues.get(position);
        holder.mItem = mValues.get(position);
        holder.llblLandName.setText(item.HeadingP);
        holder.llblLong.setText(item.longitide + "");
        holder.llblLat.setText(item.latitude + "");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView llblLong;
        public final TextView llblLat;
        public final TextView llblLandName;
        public SavedPOI mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            llblLat= (TextView) view.findViewById(R.id.lblLandmarkLat);
            llblLong = (TextView) view.findViewById(R.id.lblLandmarkLong);
            llblLandName = (TextView) view.findViewById(R.id.lblLandmarkHeading);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + llblLandName.getText() + "'";
        }
    }
}
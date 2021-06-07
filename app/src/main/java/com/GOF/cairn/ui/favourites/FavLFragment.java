package com.GOF.cairn.ui.favourites;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.R;



public class FavLFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public FavLFragment() {
    }

    @SuppressWarnings("unused")
    public static FavLFragment newInstance(int columnCount) {
        FavLFragment fragment = new FavLFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_l_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (AuthHold.getInstance().loggedInUser.lsFavLandmarks == null){
                Toast.makeText(getActivity(), "No favourites added", Toast.LENGTH_SHORT).show();
            } else{
                recyclerView.setAdapter(new MySavedPointsRecyclerViewAdapter(AuthHold.getInstance().loggedInUser.lsFavLandmarks)); //passes user POI to recycler view
            }

        }
        return view;
    }
}
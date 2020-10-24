package com.blz.cookietech.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blz.cookietech.cookietechmetronomelibrary.R;

public class SubdivisionAdapter extends RecyclerView.Adapter<SubdivisionAdapter.SubdivisionViewHolder> {

    private static final String TAG = "SubdivisionAdapter";

    private int recyclerViewHeight;

    private RecyclerView recyclerView;




    public SubdivisionAdapter(RecyclerView recyclerView){
        this.recyclerView = recyclerView;

    }

    @NonNull
    @Override
    public SubdivisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subdivision_item,parent,false);
        recyclerViewHeight = recyclerView.getHeight();
        return new SubdivisionViewHolder(view,recyclerViewHeight);
    }

    @Override
    public void onBindViewHolder(@NonNull SubdivisionViewHolder holder, int position) {

        //holder.rootViewGroup.setMinHeight(recyclerViewHeight/3);

    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public class SubdivisionViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout rootViewGroup;

        public SubdivisionViewHolder(@NonNull View itemView, int recyclerViewHeight) {
            super(itemView);
            rootViewGroup = itemView.findViewById(R.id.rootViewGroup);
            rootViewGroup.setMinHeight(recyclerViewHeight/3);
        }
    }
}

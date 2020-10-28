package com.blz.cookietech.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blz.cookietech.cookietechmetronomelibrary.R;
import com.bumptech.glide.Glide;

public class SubdivisionAdapter extends RecyclerView.Adapter<SubdivisionAdapter.SubdivisionViewHolder> {

    private static final String TAG = "SubdivisionAdapter";

    private int recyclerViewHeight;

    private RecyclerView recyclerView;
    private int rightTimeSignature;
    private Context context;
    private static int icon_count = 1;

    private final int[][] drawables = new int[][]{{R.mipmap.sub_a_1,R.mipmap.sub_a_2,R.mipmap.sub_a_3,R.mipmap.sub_a_4},{R.mipmap.sub_b_1,R.mipmap.sub_b_2,R.mipmap.sub_b_3,R.mipmap.sub_b_4},{R.mipmap.sub_c_1,R.mipmap.sub_c_2,R.mipmap.sub_c_3,R.mipmap.sub_c_4},{R.mipmap.sub_d_1,R.mipmap.sub_d_2,R.mipmap.sub_d_3,R.mipmap.sub_d_4}};




    public SubdivisionAdapter( RecyclerView recyclerView, int rightTimeSignature){
        this.recyclerView = recyclerView;
        this.rightTimeSignature = rightTimeSignature;

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
        int drawableId = drawables[rightTimeSignature-1][position];
        holder.subdivision_icon.setImageResource(drawableId);


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class SubdivisionViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout rootViewGroup;
        ImageView subdivision_icon;

        public SubdivisionViewHolder(@NonNull View itemView, int recyclerViewHeight) {
            super(itemView);
            rootViewGroup = itemView.findViewById(R.id.rootViewGroup);
            rootViewGroup.setMinHeight(recyclerViewHeight/3);
            subdivision_icon = itemView.findViewById(R.id.subdivision_icon);
        }
    }


    public void setRightTimeSignature(int rightTimeSignature) {
        this.rightTimeSignature = rightTimeSignature;
        notifyDataSetChanged();
    }

}

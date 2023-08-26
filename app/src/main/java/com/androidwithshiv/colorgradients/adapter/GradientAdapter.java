package com.androidwithshiv.colorgradients.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidwithshiv.colorgradients.GradientClickListener;
import com.androidwithshiv.colorgradients.R;
import com.androidwithshiv.colorgradients.common.Common;
import com.androidwithshiv.colorgradients.models.Gradient;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class GradientAdapter extends RecyclerView.Adapter<GradientViewHolder>{
    Context context;
    List<Gradient> gradientList;
    GradientClickListener gradientClickListener;
    public GradientAdapter(Context context, List<Gradient> gradientList, GradientClickListener gradientClickListener) {
        this.context = context;
        this.gradientList = gradientList;
        this.gradientClickListener = gradientClickListener;
    }

    @NonNull
    @Override
    public GradientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradientViewHolder(LayoutInflater.from(context).inflate(R.layout.gradients_home_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GradientViewHolder holder, int position) {
        holder.gradientNameTv.setText(gradientList.get(position).getGradientName());
        holder.gradientStartColorTv.setText(gradientList.get(position).getGradientColorStart());
        holder.gradientEndColorTv.setText(gradientList.get(position).getGradientColorEnd());

        if(gradientList.get(position).isFavourite()){
            holder.favouriteIv.setImageResource(R.drawable.hearts_fill);
        }
        else{
            holder.favouriteIv.setImageResource(R.drawable.heart_add_svgrepo_com);
        }

        int colorCodeStart = Common.hexToIntColorCode(gradientList.get(position).getGradientColorStart().toString());
        int colorCodeEnd = Common.hexToIntColorCode(gradientList.get(position).getGradientColorEnd().toString());
        int colors[] = {colorCodeStart, colorCodeEnd};

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradientDrawable.setCornerRadii(new float[] { 50, 50, 50, 50, 50, 50, 50, 50 });
        holder.gradientCard.setBackground(gradientDrawable);


        holder.favouriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gradientClickListener.onClickFavourite(gradientList.get(holder.getAdapterPosition()));
            }
        });

        holder.editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gradientClickListener.onClickEdit(gradientList.get(holder.getAdapterPosition()));
            }
        });

        holder.copyCodeStartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gradientClickListener.onCLickCopyCodeStart(gradientList.get(holder.getAdapterPosition()));
            }
        });

        holder.copyCodeEndIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gradientClickListener.onClickCopyCodeEnd(gradientList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return gradientList.size();
    }

    public void filteredList(List<Gradient> filteredList){
        gradientList = filteredList;
        notifyDataSetChanged();
    }
}
class GradientViewHolder extends RecyclerView.ViewHolder{

    TextView gradientNameTv, gradientStartColorTv, gradientEndColorTv;
    ImageView editIv, copyCodeStartIv, copyCodeEndIv, favouriteIv;
    MaterialCardView gradientCard;
    public GradientViewHolder(@NonNull View itemView) {
        super(itemView);
        gradientNameTv = itemView.findViewById(R.id.gradients_name);
        gradientStartColorTv = itemView.findViewById(R.id.first_color);
        gradientEndColorTv = itemView.findViewById(R.id.second_color);
        editIv = itemView.findViewById(R.id.edit_gradient);
        copyCodeStartIv = itemView.findViewById(R.id.copy_code1);
        copyCodeEndIv = itemView.findViewById(R.id.copy_code2);
        favouriteIv = itemView.findViewById(R.id.favrt);
        gradientCard = itemView.findViewById(R.id.gradient_card);
    }
}

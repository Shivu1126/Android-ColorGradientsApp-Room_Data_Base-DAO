package com.androidwithshiv.colorgradients;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidwithshiv.colorgradients.adapter.GradientAdapter;
import com.androidwithshiv.colorgradients.common.Common;
import com.androidwithshiv.colorgradients.database.RoomDB;
import com.androidwithshiv.colorgradients.models.Gradient;

import java.util.ArrayList;
import java.util.List;

public class FavouriteGradientsActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView gradientRecyclerViewFav;
    private RelativeLayout noData;
    private ImageView backButton;
    GradientAdapter gradientAdapter;
    List<Gradient> gradientList;
    RoomDB database;
    private void init(){
        context = FavouriteGradientsActivity.this;
        gradientRecyclerViewFav = findViewById(R.id.gradients_favrt_rv);
        noData = findViewById(R.id.no_data);
        backButton = findViewById(R.id.back);
        gradientList = new ArrayList<>();
        database = RoomDB.getInstance(context);
        gradientList = database.mainDAO().getFavourite(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_gradients);
        init();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.home_bg));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        updateGradientsList(gradientList);
    }

    private void updateGradientsList(List<Gradient> gradientList) {
        gradientRecyclerViewFav.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        gradientRecyclerViewFav.setHasFixedSize(true);
        gradientAdapter = new GradientAdapter(context, gradientList, gradientClickListener);
        gradientRecyclerViewFav.setAdapter(gradientAdapter);
        //        List<Gradient> historyList =?
        if(gradientList.size()==0){
            noData.setVisibility(View.VISIBLE);
        }
        else{
            noData.setVisibility(View.GONE);
        }
    }

    private final GradientClickListener gradientClickListener = new GradientClickListener() {
        @Override
        public void onClickEdit(Gradient gradient) {
            Intent intent = new Intent(FavouriteGradientsActivity.this, GradientCreateActivity.class);
            intent.putExtra("old_gradient", gradient);
            startActivityForResult(intent, 102);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClickFavourite(Gradient gradient) {
            if(gradient.isFavourite()){
                database.mainDAO().favourite(gradient.getId(), false);
                Common.showToast(context, "Removed from favourite ..!");
            }else{
                database.mainDAO().favourite(gradient.getId(), true);
                Common.showToast(context, "Added to favourite..!");
            }
            gradientList.clear();
            gradientList.addAll(database.mainDAO().getFavourite(true));
            if(gradientList.isEmpty()){
                noData.setVisibility(View.VISIBLE);
            }
            else {
                noData.setVisibility(View.GONE);
            }
            gradientAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCLickCopyCodeStart(Gradient gradient) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("colorStart", gradient.getGradientColorStart());
            clipboard.setPrimaryClip(clip);
            Common.showToast(context, "Code copied to clipboard");
        }

        @Override
        public void onClickCopyCodeEnd(Gradient gradient) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("colorEnd", gradient.getGradientColorEnd());
            clipboard.setPrimaryClip(clip);
            Common.showToast(context, "Code copied to clipboard");
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK){
                Gradient new_gradient = (Gradient) data.getSerializableExtra("gradientObj");
                database.mainDAO().insert(new_gradient);
                noData.setVisibility(View.GONE);
                gradientList.clear();
                gradientList.addAll(database.mainDAO().getAll());
                gradientAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == 102) {
            if(resultCode==Activity.RESULT_OK){
                Gradient updateGradient = (Gradient) data.getSerializableExtra("gradientObj");
                database.mainDAO().update(updateGradient.getId(), updateGradient.getGradientName(),
                        updateGradient.getGradientColorStart(), updateGradient.getGradientColorEnd(),
                        updateGradient.isFavourite());
                gradientList.clear();
                gradientList.addAll(database.mainDAO().getFavourite(true));
                gradientAdapter.notifyDataSetChanged();
            }
        }
    }
}
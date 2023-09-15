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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidwithshiv.colorgradients.adapter.GradientAdapter;
import com.androidwithshiv.colorgradients.common.Common;
import com.androidwithshiv.colorgradients.database.RoomDB;
import com.androidwithshiv.colorgradients.models.Gradient;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private Context context;
    private ImageView addGradient, favouriteGradient;
    private EditText searchEt;
    private RecyclerView gradientRecyclerView;
    private RelativeLayout noData;
    GradientAdapter gradientAdapter;
    List<Gradient> gradientList;
    Gradient selectedGradient;
    RoomDB database;

    private void init(){
        context = HomeActivity.this;

        addGradient = findViewById(R.id.add_gradient);
        favouriteGradient = findViewById(R.id.yours_favrt);
        searchEt = findViewById(R.id.editText_search);
        gradientRecyclerView = findViewById(R.id.gradients_home_rv);
        noData = findViewById(R.id.no_data);

        gradientList = new ArrayList<>();
        database = RoomDB.getInstance(context);
        gradientList = database.mainDAO().getAll();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.home_bg));

        addGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //color picker activity
                Intent intent = new Intent(HomeActivity.this, GradientCreateActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        favouriteGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //favourite activity
                startActivity(new Intent(HomeActivity.this, FavouriteGradientsActivity.class));
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        updateGradientsList(gradientList);
    }
    private void updateGradientsList(List<Gradient> gradientList){
        gradientRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
        gradientRecyclerView.setHasFixedSize(true);
        gradientAdapter = new GradientAdapter(context, gradientList, gradientClickListener);
        gradientRecyclerView.setAdapter(gradientAdapter);
   //        List<Gradient> historyList =?
        if(gradientList.size()==0){
            noData.setVisibility(View.VISIBLE);
        }
        else{
            noData.setVisibility(View.GONE);
        }
    }

    private void filter(String txt){
        List<Gradient> filterList = new ArrayList<>();
        for (Gradient singleGradient: gradientList) {
            if(singleGradient.getGradientName().toLowerCase().contains(txt.toLowerCase())){
                filterList.add(singleGradient);
            }
        }
        this.gradientAdapter.filteredList(filterList);
    }

    private final GradientClickListener gradientClickListener = new GradientClickListener() {
        @Override
        public void onClickEdit(Gradient gradient) {
            Intent intent = new Intent(HomeActivity.this, GradientCreateActivity.class);
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
            refresh();
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
                Log.d("create-success", "true");
                Gradient new_gradient = (Gradient) data.getSerializableExtra("gradientObj");
                database.mainDAO().insert(new_gradient);
                noData.setVisibility(View.GONE);
//                gradientList.clear();
//                gradientList.addAll(database.mainDAO().getAll());
//                gradientAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 102) {
            if(resultCode==Activity.RESULT_OK){

                Gradient updateGradient = (Gradient) data.getSerializableExtra("gradientObj");
                database.mainDAO().update(updateGradient.getId(), updateGradient.getGradientName(),
                        updateGradient.getGradientColorStart(), updateGradient.getGradientColorEnd(),
                        updateGradient.isFavourite());
                Log.d("edit-update", "true");
            }
        }
        refresh();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh(){
        gradientList.clear();
        gradientList.addAll(database.mainDAO().getAll());
        this.gradientAdapter.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
package com.androidwithshiv.colorgradients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidwithshiv.colorgradients.common.Common;
import com.androidwithshiv.colorgradients.models.Gradient;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.card.MaterialCardView;

public class GradientCreateActivity extends AppCompatActivity {

    private Context context;
    private MaterialCardView pickColor1Button, pickColor2Button, saveData, pickColor1bg, pickColor2bg;
    private TextView colorCode1Tv, colorCode2Tv;
    private EditText gradientNameEt;
    private ImageView backButton;

    private void init() {
        context = GradientCreateActivity.this;
        pickColor1Button = findViewById(R.id.pick_color_one);
        pickColor2Button = findViewById(R.id.pick_color_second);
        saveData = findViewById(R.id.save);
        pickColor1bg = findViewById(R.id.color_1);
        pickColor2bg = findViewById(R.id.color_2);
        colorCode1Tv = findViewById(R.id.color_1_code);
        colorCode2Tv = findViewById(R.id.color_2_code);
        gradientNameEt = findViewById(R.id.gradient_name);
        backButton = findViewById(R.id.back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_create);
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

        pickColor1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker(pickColor1bg, colorCode1Tv, view);
            }
        });

        pickColor2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker(pickColor2bg, colorCode2Tv, view);
            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gradientNameEt.getText().toString().isEmpty()) {
                    Common.showToast(context, "Please add name...");
                } else {
                    String gradientName = gradientNameEt.getText().toString();
                    String colorCode1 = colorCode1Tv.getText().toString();
                    String colorCode2 = colorCode2Tv.getText().toString();

                    Log.d("color_code_1", colorCode1);
                    Log.d("color_code_2", colorCode2);

                    Gradient gradient = new Gradient();
                    gradient.setGradientName(gradientName);
                    gradient.setGradientColorStart(colorCode1);
                    gradient.setGradientColorEnd(colorCode2);
                    gradient.setFavourite(false);

                    Intent intent = new Intent();
                    intent.putExtra("gradientObj", gradient);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });


    }

    private void showColorPicker(MaterialCardView pickColorBg, TextView colorCodeTv, View v) {
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        pickColorBg.setCardBackgroundColor(selectedColor);
                        colorCodeTv.setText(Common.intToHexColorCode(selectedColor));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
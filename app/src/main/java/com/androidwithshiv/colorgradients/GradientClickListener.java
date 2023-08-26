package com.androidwithshiv.colorgradients;

import com.androidwithshiv.colorgradients.models.Gradient;

public interface GradientClickListener {
    void onClickEdit(Gradient gradient);
    void onClickFavourite(Gradient gradient);
    void onCLickCopyCodeStart(Gradient gradient);
    void onClickCopyCodeEnd(Gradient gradient);
}

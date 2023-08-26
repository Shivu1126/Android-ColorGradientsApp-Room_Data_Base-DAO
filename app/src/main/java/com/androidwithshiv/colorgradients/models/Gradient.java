package com.androidwithshiv.colorgradients.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Gradient")
public class Gradient implements Serializable {
    @ColumnInfo(name = "gradient_id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "gradient_name")
    String gradientName;
    @ColumnInfo(name = "gradient_color_start")
    String gradientColorStart;

    @ColumnInfo(name = "gradient_color_end")
    String gradientColorEnd;
    @ColumnInfo(name = "gradient_favourite")
    boolean isFavourite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGradientName() {
        return gradientName;
    }

    public void setGradientName(String gradientName) {
        this.gradientName = gradientName;
    }

    public String getGradientColorStart() {
        return gradientColorStart;
    }

    public void setGradientColorStart(String gradientColorStart) {
        this.gradientColorStart = gradientColorStart;
    }

    public String getGradientColorEnd() {
        return gradientColorEnd;
    }

    public void setGradientColorEnd(String gradientColorEnd) {
        this.gradientColorEnd = gradientColorEnd;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}

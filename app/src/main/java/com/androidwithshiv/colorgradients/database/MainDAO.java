package com.androidwithshiv.colorgradients.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

import com.androidwithshiv.colorgradients.models.Gradient;

import java.util.List;

@Dao
public interface MainDAO {
    //DAO - DATA ACCESS OBJECT

    @Insert(onConflict = REPLACE)
    void insert(Gradient gradient);
    @Query("SELECT * FROM Gradient ORDER BY gradient_id DESC")
    List<Gradient> getAll();

    @Query("UPDATE Gradient SET gradient_favourite = :isFavourite WHERE gradient_id = :id")
    void favourite(int id, boolean isFavourite);

    @Query("SELECT * FROM Gradient WHERE gradient_favourite = :b ORDER BY gradient_id DESC")
    List<Gradient> getFavourite(boolean b);

    @Query("UPDATE Gradient SET gradient_name = :gradientName, " +
            "gradient_color_start = :colorStart, gradient_color_end = :colorEnd, " +
            "gradient_favourite = :favourite WHERE gradient_id = :gradientId")
    void update(int gradientId,String gradientName, String colorStart, String colorEnd, boolean favourite);
}

package com.example.laborator8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhoneDao {
    @Insert
    long insertPhone(Phone phone);

    @Query("SELECT * FROM phones")
    List<Phone> getAllPhones();

    @Query("SELECT * FROM phones WHERE brand = :brand")
    List<Phone> getPhonesByBrand(String brand);

    @Query("SELECT * FROM phones WHERE price BETWEEN :min AND :max")
    List<Phone> getPhonesInRange(double min, double max);

    @Query("DELETE FROM phones WHERE model = :model")
    int deletePhoneByModel(String model);

    @Query("UPDATE phones SET price = price + 1 WHERE brand LIKE :letter || '%'")
    void incrementPriceForBrandsStartingWith(String letter);
}

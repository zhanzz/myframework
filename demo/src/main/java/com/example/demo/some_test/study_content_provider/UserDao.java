package com.example.demo.some_test.study_content_provider;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
 
    //增
    @Insert
    void insert(User... users);
 
    //删
    @Delete
    void delete(User... users);
 
    //改
    @Update
    void update(User... users);
 
    //查
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Query("SELECT * FROM user WHERE age > :minAge LIMIT 5")
    public Cursor queryStudent(int minAge);
}
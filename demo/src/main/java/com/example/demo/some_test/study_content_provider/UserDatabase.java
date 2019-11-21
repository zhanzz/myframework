package com.example.demo.some_test.study_content_provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//                    表名         数据库版本     不添加会警告
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
 
    private static final String DB_NAME = "UserDatabase.db";
    private static volatile UserDatabase instance;
 
    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
 
    private static UserDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                UserDatabase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }
 
    public abstract UserDao getUserDao();
}
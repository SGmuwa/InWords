package com.dreamproject.inwords.data.source.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dreamproject.inwords.data.dto.User;
import com.dreamproject.inwords.data.dto.WordTranslation;
import com.dreamproject.inwords.data.dto.WordsSeria;

@Database(entities = {WordTranslation.class, User.class, WordsSeria.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract WordTranslationDao wordTranslationDao();
    public abstract UserDao userDao();
    public abstract WordsSeriaDao wordsSeriaDao();
}
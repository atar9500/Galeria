package com.atar.galeria.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {

    @TypeConverter
    public List<String> fromString(String value){
        return new Gson().fromJson(value, new TypeToken<List<String>>() {}.getType());
    }

    @TypeConverter
    public String fromList(List<String> list){
        return new Gson().toJson(list);
    }

}

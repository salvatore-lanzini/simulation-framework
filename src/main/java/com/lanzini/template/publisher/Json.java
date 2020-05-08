package com.lanzini.template.publisher;

import com.google.gson.Gson;

class Json {
    private static final Gson gson = new Gson();

    static <T> String stringify(T object){
        return gson.toJson(object);
    }
}

package com.dl.playfun.entity;

import com.dl.playfun.data.typeadapter.BooleanTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author litchi
 */
public class UserConnMicStatusEntity {

    /**
     * is_connection : 0
     */
    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_connection")
    private Boolean isConnection;

    public Boolean getConnection() {
        return isConnection;
    }

    public void setConnection(Boolean connection) {
        isConnection = connection;
    }
}

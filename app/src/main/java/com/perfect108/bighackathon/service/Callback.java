package com.perfect108.bighackathon.service;

/**
 * Created by Kunal on 30-03-2018.
 */

public interface Callback<T> {
    void onSuccess(T response);

    void onFailure(Error error);
}

package com.collage.base;

import java.util.List;

public interface BaseListener<T> {

    void onListFetchingStarted();

    void onListFetched(List<T> fetchedList);
}

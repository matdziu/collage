package pl.collage.base;

import java.util.List;

public interface BaseView<T> {

    void updateRecyclerView(List<T> fetchedList);

    void showConnectionError();

    boolean isConnected();

    void showProgressBar();

    void hideProgressBar();

    void showNoItemsInfo();

    void hideNoItemsInfo();
}

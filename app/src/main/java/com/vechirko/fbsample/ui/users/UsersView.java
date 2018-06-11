package com.vechirko.fbsample.ui.users;

import com.vechirko.fbsample.data.model.UserModel;

import java.util.Collection;

public interface UsersView {

    void showLoading(boolean show);

    void showError(String message);

    void setData(Collection<UserModel> data);

    void showEmptyView(boolean show);
}

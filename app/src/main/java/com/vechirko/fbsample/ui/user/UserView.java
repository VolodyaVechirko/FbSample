package com.vechirko.fbsample.ui.user;

import com.vechirko.fbsample.data.model.UserModel;

public interface UserView {

    String getUserId();

    void showLoading(boolean show);

    void showError(String message);

    void setData(UserModel data);

}

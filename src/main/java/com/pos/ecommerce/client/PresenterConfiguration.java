package com.pos.ecommerce.client;

import com.pos.ecommerce.client.presenter.Presenter;

public class PresenterConfiguration {
    private static PresenterConfiguration instance;

    public static PresenterConfiguration getInstance() {
        if (instance==null){
            instance = new PresenterConfiguration();
        }
        return instance;
    }

    public Presenter getPresenterFor(String token) {
        return null;
    }
}

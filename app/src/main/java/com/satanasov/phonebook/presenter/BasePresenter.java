package com.satanasov.phonebook.presenter;

public abstract class BasePresenter<T> {

    protected T view                   = null;
    protected boolean isSubscribed     = false;
    protected boolean isFirstSubscribe = true;

    public void attachView(T view){

        this.view = view;
    }

    public void subscribe(){

        isSubscribed = true;
    }

    public void unSubscribe(){
        isSubscribed     = false;
        isFirstSubscribe = false;
    }

    public void detachView(){

        this.view = null;
    }

}

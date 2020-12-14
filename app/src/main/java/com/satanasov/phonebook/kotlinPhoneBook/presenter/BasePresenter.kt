package com.satanasov.phonebook.kotlinPhoneBook.presenter

 abstract class BasePresenter<T> {
    protected var view: T?                  = null
    protected var isSubscribed: Boolean     = false
    protected var isFirstSubscribe: Boolean = true

    fun attachView(view: T){
        this.view = view
    }

    open fun subscribe(){
        isSubscribed = true
    }

    fun unSubscribe(){
        isSubscribed     = false
        isFirstSubscribe = false
    }

    fun detachView(){
        this.view = null
    }
}
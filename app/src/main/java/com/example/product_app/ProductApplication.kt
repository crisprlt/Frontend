package com.example.product_app

import android.app.Application

class ProductApplication : Application() {

    companion object {
        lateinit var instance: ProductApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

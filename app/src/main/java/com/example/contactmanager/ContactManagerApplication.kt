package com.example.contactmanager

import android.app.Application
import com.example.contactmanager.data.AppContainer
import com.example.contactmanager.data.AppDataContainer

class ContactManagerApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
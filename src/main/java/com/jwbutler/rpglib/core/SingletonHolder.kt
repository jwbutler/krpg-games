package com.jwbutler.rpglib.core

abstract class SingletonHolder<T>
{
    private var instance: T? = null

    fun initialize(supplier: () -> T): T
    {
        instance = supplier()
        return instance!!
    }

    fun getInstance() = instance!!
}
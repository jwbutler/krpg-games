package com.jwbutler.rpglib.core

abstract class BoundSingletonHolder<T>(private val supplier: (() -> T))
{
    private var instance: T? = null

    fun initialize(): T
    {
        instance = supplier()
        return instance!!
    }

    fun getInstance() = instance!!
}
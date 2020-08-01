package com.jwbutler.krpg.core

import java.lang.IllegalStateException

abstract class SingletonHolder<T>(
    private val supplier: () -> T
)
{
    private var instance: T? = null

    fun initialize(): T
    {
        instance = supplier()
        return instance ?: throw IllegalStateException()
    }

    fun getInstance() = instance ?: throw IllegalStateException()
}
package com.jwbutler.krpg.core

import java.lang.IllegalStateException

private typealias Supplier<T> = (() -> T)?

abstract class SingletonHolder<T>(private var supplier: Supplier<T>)
{
    private var instance: T? = null

    constructor() : this(null)

    fun initialize(): T
    {
        instance = supplier?.invoke() ?: throw IllegalStateException()
        return instance ?: throw IllegalStateException()
    }

    fun initialize(supplier: () -> T): T
    {
        this.supplier = supplier
        return initialize()
    }

    fun getInstance() = instance ?: throw IllegalStateException()
}
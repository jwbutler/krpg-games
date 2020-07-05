package com.jwbutler.krpg.behavior

enum class Activity
{
    STANDING,
    WALKING,
    ATTACKING,
    FALLING;

    override fun toString() = name.toLowerCase()
}
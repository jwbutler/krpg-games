package com.jwbutler.rpglib.levels

data class VictoryCondition
(
    val predicate: () -> Boolean,
    val onComplete: () -> Unit
)
{
    companion object
    {
        val NONE = VictoryCondition({ false }, {})
    }
}
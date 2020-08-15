package com.jwbutler.krpg.levels

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
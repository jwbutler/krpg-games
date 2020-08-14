package com.jwbutler.krpg.levels

data class VictoryCondition
(
    val predicate: () -> Boolean,
    val onComplete: () -> Unit
)
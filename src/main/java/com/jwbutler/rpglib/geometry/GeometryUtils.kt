package com.jwbutler.rpglib.geometry

import kotlin.math.sqrt

fun hypotenuse(first: Coordinates, second: Coordinates): Double
{
    val dx = first.x - second.x
    val dy = first.y - second.y
    return sqrt((dx*dx + dy*dy).toDouble())
}
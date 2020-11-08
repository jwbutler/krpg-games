package com.jwbutler.rpglib.geometry

data class Offsets(override val x: Int, override val y: Int) : IntPair
{
    operator fun plus(other: IntPair): Offsets = Offsets(x + other.x, y + other.y)
}
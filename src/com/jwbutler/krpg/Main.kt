package com.jwbutler.krpg

import com.jwbutler.krpg.entities.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.Sprite

fun main()
{
    val unit = Unit.create(Sprite.PLAYER(), Coordinates(0, 0))
    unit.moveTo(Coordinates(3, 5))
}
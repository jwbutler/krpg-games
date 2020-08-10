package com.jwbutler.krpg.levels

import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.geometry.Coordinates

object LevelFactory
{
    val FIRST_LEVEL = Level(
        mapOf(Coordinates(0, 0) to Tile(Coordinates(0, 0))),
        mapOf(),
        mapOf(),
        Coordinates(0, 0),
        { false }
    )
}

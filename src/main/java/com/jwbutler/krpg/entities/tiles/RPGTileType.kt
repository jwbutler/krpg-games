package com.jwbutler.krpg.entities.tiles

import com.jwbutler.rpglib.entities.tiles.TileType

enum class RPGTileType(override val filename: String) : TileType
{
    GRASS("grass_24x12"),
    DIRT("dirt_24x12"),
    STONE("stone_24x12");
}
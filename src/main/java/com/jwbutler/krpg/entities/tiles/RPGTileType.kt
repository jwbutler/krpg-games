package com.jwbutler.krpg.entities.tiles

import com.jwbutler.rpglib.entities.tiles.TileType

enum class RPGTileType(override val filename: String) : TileType
{
    GRASS("tiles/grass_24x12"),
    DIRT("tiles/dirt_24x12"),
    STONE("tiles/stone_24x12");
}
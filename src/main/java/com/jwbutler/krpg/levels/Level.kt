package com.jwbutler.krpg.levels

import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.geometry.Coordinates

data class Level
(
    val tiles: Map<Coordinates, Tile>,
    val objects: Map<Coordinates, GameObject>,
    val units: Map<Coordinates, Unit>,
    val startPoint: Coordinates
)
package com.jwbutler.krpg.levels

import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.players.Player

data class Level
(
    val tiles: Map<Coordinates, Tile>,
    val units: Map<Player, Map<Coordinates, Unit>>,
    val objects: Map<Coordinates, Collection<GameObject>>,
    val startPosition: Coordinates,
    val victoryCondition: () -> Boolean
)
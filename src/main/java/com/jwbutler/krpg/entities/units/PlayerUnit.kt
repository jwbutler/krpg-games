package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.PlayerSprite
import com.jwbutler.krpg.players.Player
import java.awt.Color

private fun _getSprite() = PlayerSprite(mutableMapOf(Color.WHITE to Color(0, 0, 0, 0)))

class PlayerUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp)
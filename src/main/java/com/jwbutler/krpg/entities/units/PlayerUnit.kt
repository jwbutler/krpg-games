package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.PlayerSprite
import com.jwbutler.krpg.players.Player

private fun _getSprite() = PlayerSprite(PaletteSwaps.WHITE_TRANSPARENT)

class PlayerUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp)
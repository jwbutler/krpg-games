package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.WizardSprite
import com.jwbutler.krpg.players.Player
import java.awt.Color

private fun _getSprite() = WizardSprite(mutableMapOf(Color.WHITE to Color(0, 0, 0, 0)))

abstract class WizardUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp)
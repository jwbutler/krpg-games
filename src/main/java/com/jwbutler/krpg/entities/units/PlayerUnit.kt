package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import com.jwbutler.krpg.players.Player

class PlayerUnit
(
    player: Player,
    coordinates: Coordinates,
    hp: Int,
    paletteSwaps: PaletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
) : AbstractUnit(player, PlayerSprite(paletteSwaps), coordinates, hp)
{
    override fun onActivityComplete(activity: Activity)
    {
    }
}
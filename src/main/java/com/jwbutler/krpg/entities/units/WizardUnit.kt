package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.WizardSprite
import com.jwbutler.krpg.players.Player

private fun _getSprite() = WizardSprite(PaletteSwaps.WHITE_TRANSPARENT)

private const val RESURRECTING_COOLDOWN = 32

class WizardUnit(player: Player, coordinates: Coordinates, hp: Int) : AbstractUnit(player, _getSprite(), coordinates, hp)
{
    override fun onActivityComplete(activity: Activity)
    {
        if (activity == Activity.RESURRECTING)
        {
            triggerCooldown(activity, RESURRECTING_COOLDOWN)
        }
    }
}
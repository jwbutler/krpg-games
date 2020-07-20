package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import java.awt.Color

private val OFFSETS = PlayerSprite.OFFSETS

class SwordSprite(paletteSwaps: PaletteSwaps) : EquipmentSprite("sword", paletteSwaps.withTransparentColor(Color.WHITE), OFFSETS)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return PlayerSprite.getFrames(activity, direction)
    }
}
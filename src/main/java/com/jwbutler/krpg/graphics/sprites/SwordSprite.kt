package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import java.awt.Color

private val OFFSETS = PlayerSprite.OFFSETS

class SwordSprite(paletteSwaps: PaletteSwaps) : EquipmentSprite("sword", paletteSwaps.withTransparentColor(Color.WHITE), OFFSETS)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return PlayerSprite.getFrames(activity, direction)
    }
}
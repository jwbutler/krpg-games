package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.PaletteSwaps
import java.awt.Color

/**
 * Units are 40x40
 * Tiles are 48x23
 * Bottom of unit renders 4 pixels from bottom of frame
 * Vertical center of tile is 12 pixels from bottom
 * 23 - 40 = -17
 * x offset = +4
 * y offset = -12 + 4 + -17 = -25
 *
 * TODO: Copied from player sprite
 */
private val offsets = Offsets(4, -25);

class SwordSprite(paletteSwaps: PaletteSwaps) : EquipmentSprite("sword", paletteSwaps.withTransparentColor(Color.WHITE), offsets)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return PlayerSprite.getFrames(activity, direction)
    }
}
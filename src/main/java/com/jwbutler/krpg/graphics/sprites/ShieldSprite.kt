package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import com.jwbutler.krpg.utils.SpriteUtils

private val OFFSETS = PlayerSprite.OFFSETS

class ShieldSprite(paletteSwaps: PaletteSwaps) : EquipmentSprite
(
    "shield2",
    paletteSwaps.withTransparentColor(Colors.WHITE),
    OFFSETS
)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return SpriteUtils.getPlayerFrames(activity, direction)
    }
}
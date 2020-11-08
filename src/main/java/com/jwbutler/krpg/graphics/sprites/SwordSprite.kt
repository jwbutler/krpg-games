package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import com.jwbutler.krpg.utils.SpriteUtils
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.PaletteSwaps

private val OFFSETS = PlayerSprite.OFFSETS

class SwordSprite(paletteSwaps: PaletteSwaps) : EquipmentSprite
(
    "sword",
    paletteSwaps.withTransparentColor(Colors.WHITE),
    OFFSETS
)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return SpriteUtils.getPlayerFrames(activity, direction)
    }
}
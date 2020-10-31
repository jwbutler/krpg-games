package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite
import com.jwbutler.krpg.utils.SpriteUtils

private val OFFSETS = PlayerSprite.OFFSETS

class MailArmorSprite(paletteSwaps: PaletteSwaps) : EquipmentSprite
(
    "mail",
    paletteSwaps.withTransparentColor(Colors.WHITE),
    OFFSETS
)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return SpriteUtils.getPlayerFrames(activity, direction)
    }
}
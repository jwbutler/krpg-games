package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.geometry.TILE_HEIGHT
import com.jwbutler.krpg.geometry.TILE_WIDTH
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.utils.SpriteUtils

class PlayerSprite(paletteSwaps: PaletteSwaps) : UnitSprite
(
    "player",
    paletteSwaps.withTransparentColor(Colors.WHITE)
)
{
    override val offsets = OFFSETS

    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return SpriteUtils.getPlayerFrames(activity, direction)
    }

    companion object
    {
        private const val SPRITE_WIDTH = 40
        private const val SPRITE_HEIGHT = 40
        /**
         * X = (24 - 40) / 2
         * Y = 12 - 40 - 1
         */
        val OFFSETS = Offsets(
            (TILE_WIDTH - SPRITE_WIDTH) / 2,
            (TILE_HEIGHT - SPRITE_HEIGHT - 1)
        )
    }
}
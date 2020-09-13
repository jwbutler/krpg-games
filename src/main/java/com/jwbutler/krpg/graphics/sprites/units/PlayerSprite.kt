package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.utils.SpriteUtils
import kotlin.math.roundToInt

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
        val OFFSETS = {
            val renderer = GameRenderer.getInstance()
            val tileWidth = (renderer.tileWidth * renderer.scaleRatioX).roundToInt()
            val tileHeight = (renderer.tileHeight * renderer.scaleRatioY).roundToInt()
            val spriteWidth = (SPRITE_WIDTH * renderer.scaleRatioX).roundToInt()
            val spriteHeight = (SPRITE_HEIGHT * renderer.scaleRatioY).roundToInt()
            Offsets(
                (tileWidth - spriteWidth) / 2,
                (tileHeight - spriteHeight - 2)
            )
        }()
    }
}
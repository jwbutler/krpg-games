package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.utils.SpriteUtils
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.geometry.Offsets
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.rpglib.graphics.sprites.UnitSprite

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
            (GameView.getInstance().tileDimensions.width - SPRITE_WIDTH) / 2,
            (GameView.getInstance().tileDimensions.height - SPRITE_HEIGHT - 1)
        )
    }
}
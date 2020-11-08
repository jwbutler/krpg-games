package com.jwbutler.rpglib.entities

import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.ImageLoader
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.rpglib.graphics.sprites.Sprite
import com.jwbutler.rpglib.graphics.sprites.StaticSprite
import java.awt.Color

class TileOverlay(
    private val coordinates: Coordinates,
    private val outerColor: Color,
    private val innerColor: Color
): Entity
{
    companion object
    {
        private val INNER_COLOR = Colors.BLACK
        private val OUTER_COLOR = Colors.RED
    }

    override fun getCoordinates() = coordinates
    override val sprite = _getSprite()

    private fun _getSprite(): Sprite
    {
        val paletteSwaps = PaletteSwaps(
            OUTER_COLOR to outerColor,
            INNER_COLOR to innerColor
        )

        return StaticSprite(
            ImageLoader.getInstance().loadImage("tiles/overlay_24x12", paletteSwaps),
            RenderLayer.FLOOR_TILE
        )
    }

    override fun render() = sprite.render(this)
}

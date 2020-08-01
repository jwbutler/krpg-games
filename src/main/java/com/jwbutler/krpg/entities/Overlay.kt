package com.jwbutler.krpg.entities

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.Sprite
import com.jwbutler.krpg.graphics.sprites.StaticSprite
import java.awt.Color

private val INNER_COLOR = Colors.BLACK
private val OUTER_COLOR = Colors.RED

class Overlay(
    private val coordinates: Coordinates,
    private val outerColor: Color,
    private val innerColor: Color
): Entity
{
    override fun getCoordinates() = coordinates
    /**
     * TODO - tile types, palette swaps, caching, etc.
     */
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

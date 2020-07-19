package com.jwbutler.krpg.entities

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.StaticSprite

class Tile(private val coordinates: Coordinates) : Entity
{
    /**
     * TODO - tile types, palette swaps, caching, etc.
     */
    private val sprite = StaticSprite(ImageLoader.getInstance().loadImage("tiles/48x23/tile_48x23_stone", PaletteSwaps.WHITE_TRANSPARENT))

    override fun getCoordinates() = coordinates
    override fun getSprite() = sprite
    override fun update() {}
    override fun afterRender() {}

    override fun render(): Pair<Image, Pixel>
    {
        return sprite.render(this)
    }
}
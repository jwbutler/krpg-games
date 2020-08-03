package com.jwbutler.krpg.entities

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.StaticSprite

/**
 * TODO: Unlike other entity types, this does not automatically add itself to the board in its initializer
 */
class Tile(private val coordinates: Coordinates) : Entity
{
    /**
     * TODO - tile types, palette swaps, caching, etc.
     */
    override val sprite = StaticSprite(
        ImageLoader.getInstance().loadImage("tiles/grass_24x12", PaletteSwaps()),
        RenderLayer.FLOOR_TILE
    )

    override fun isBlocking() = false
    override fun getCoordinates() = coordinates
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun update() {}
    override fun afterRender() {}

    override fun render(): Renderable = sprite.render(this)
}
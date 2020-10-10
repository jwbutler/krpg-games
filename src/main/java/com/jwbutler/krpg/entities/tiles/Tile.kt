package com.jwbutler.krpg.entities.tiles

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.StaticSprite

class Tile(private val type: TileType, private val coordinates: Coordinates) : Entity
{
    override val sprite = StaticSprite(
        ImageLoader.getInstance().loadImage("tiles/${type.filename}"),
        RenderLayer.FLOOR_TILE
    )

    override fun isBlocking() = false
    // TODO: create GameState#getCoordinates(Tile)?
    override fun getCoordinates() = coordinates
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun endFrame() {}

    override fun render(): Renderable = sprite.render(this)
}
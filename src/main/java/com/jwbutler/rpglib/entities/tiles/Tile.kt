package com.jwbutler.rpglib.entities.tiles

import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.graphics.images.ImageLoader
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.rpglib.graphics.sprites.StaticSprite

class Tile
(
    type: TileType,
    private val coordinates: Coordinates
) : Entity
{
    override val sprite = StaticSprite(
        ImageLoader.getInstance().loadImage(type.filename),
        RenderLayer.FLOOR_TILE
    )

    override fun isBlocking() = false
    // TODO: create GameState#getCoordinates(Tile)?
    override fun getCoordinates() = coordinates
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun update() {}

    override fun render(): Renderable = sprite.render(this)
}
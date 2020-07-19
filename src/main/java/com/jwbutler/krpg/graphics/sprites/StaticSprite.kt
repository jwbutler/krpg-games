package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.Image

class StaticSprite(private val image: Image) : Sprite
{
    override fun render(entity: Entity) = Pair(image, entity.getCoordinates().toPixel())
}
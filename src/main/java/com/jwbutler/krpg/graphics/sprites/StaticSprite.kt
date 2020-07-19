package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable

class StaticSprite(private val image: Image, private val layer: RenderLayer) : Sprite
{
    override fun render(entity: Entity) = Renderable(image, entity.getCoordinates().toPixel(), layer)
}
package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable

class StaticSprite(private val image: Image, private val layer: RenderLayer, override val offsets: Offsets = Offsets(0, 0)) : Sprite
{
    override fun render(entity: Entity) = Renderable(
        image,
        entity.getCoordinates().toPixel() + offsets,
        layer
    )
}
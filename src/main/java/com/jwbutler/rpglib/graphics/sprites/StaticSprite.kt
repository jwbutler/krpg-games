package com.jwbutler.rpglib.graphics.sprites

import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.geometry.Offsets
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.rpglib.graphics.images.Image

class StaticSprite
(
    private val image: Image,
    private val layer: RenderLayer,
    override val offsets: Offsets = Offsets(0, 0 )
) : Sprite
{
    override fun render(entity: Entity) = Renderable(
        image,
        entity.getCoordinates().toPixel() + offsets,
        layer
    )
}
package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.sprites.StaticSprite

class Wall(coordinates: Coordinates) : AbstractObject(coordinates)
{
    override val sprite = StaticSprite(ImageLoader.getInstance().loadImage("tiles/wall_24x12"), RenderLayer.OBJECT)
    override fun isBlocking() = true
}
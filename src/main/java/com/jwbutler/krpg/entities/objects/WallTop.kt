package com.jwbutler.krpg.entities.objects

import com.jwbutler.rpglib.entities.objects.AbstractObject
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.images.Image
import com.jwbutler.rpglib.graphics.images.ImageLoader
import com.jwbutler.rpglib.graphics.sprites.StaticSprite

class WallTop : AbstractObject()
{
    override val sprite = StaticSprite(_getImage(), RenderLayer.OBJECT)
    override fun isBlocking() = true

    companion object
    {
        private fun _getImage(): Image
        {
            return ImageLoader.getInstance().loadImage("objects/wall_top")
        }
    }
}
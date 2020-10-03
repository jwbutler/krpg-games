package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.sprites.StaticSprite

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
package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.graphics.sprites.StaticSprite

class Wall : AbstractObject()
{
    override val sprite = StaticSprite(_getImage(), RenderLayer.OBJECT, Offsets(0, -36))
    override fun isBlocking() = true

    companion object
    {
        private fun _getImage(): Image
        {
            return ImageLoader.getInstance().loadImage("objects/wall_24x48").scale2x()
        }
    }
}
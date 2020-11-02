package com.jwbutler.rpglib.graphics.awt

import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.graphics.GameRenderer
import com.jwbutler.rpglib.graphics.images.Image

class GameRendererAWT(override val dimensions: Dimensions) : GameRenderer
{
    private val buffer: Image = ImageAWT(dimensions.width, dimensions.height)

    override fun render(): Image
    {
        _clearBuffer()

        for ((image, pixel) in GameView.getInstance().getRenderables())
        {

            buffer.drawImage(image, pixel.x, pixel.y)
        }
        return buffer
    }

    override fun getImage(): Image = buffer

    private fun _clearBuffer()
    {
        buffer.clearRect(0, 0, dimensions.width, dimensions.height)
    }
}
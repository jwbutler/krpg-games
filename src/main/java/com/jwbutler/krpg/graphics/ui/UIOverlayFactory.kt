package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.utils.rectFromPixels
import com.jwbutler.rpglib.geometry.Pixel
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.Image

object UIOverlayFactory
{
    fun getSelectionRect(start: Pixel, end: Pixel): Renderable
    {
        val rectangle = rectFromPixels(start, end)
        val image = Image.create(rectangle.width + 1, rectangle.height + 1)
        image.drawRect(0, 0, rectangle.width, rectangle.height, Colors.GREEN)

        return Renderable(image, Pixel(rectangle.x, rectangle.y), RenderLayer.UI_OVERLAY)
    }
}
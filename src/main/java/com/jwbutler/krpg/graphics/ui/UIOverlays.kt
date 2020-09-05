package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.utils.rectFromPixels

object UIOverlays
{
    fun getSelectionRect(start: Pixel, end: Pixel): Renderable
    {
        val rectangle = rectFromPixels(start, end)
        val image = Image(rectangle.width + 1, rectangle.height + 1)
        image.drawRect(0, 0, rectangle.width, rectangle.height, Colors.GREEN)

        return Renderable(
            image,
            Pixel(rectangle.x, rectangle.y),
            RenderLayer.UI_OVERLAY
        )
    }
}
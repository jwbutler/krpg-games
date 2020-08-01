package com.jwbutler.krpg.graphics.images

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage

data class Image
(
    private val delegate: BufferedImage
)
{
    constructor(width: Int, height: Int) : this(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))

    val width = delegate.width
    val height = delegate.width

    fun getRGB(x: Int, y: Int) = delegate.getRGB(x, y)
    fun setRGB(x: Int, y: Int, rgb: Int) = delegate.setRGB(x, y, rgb)

    fun draw(graphics: Graphics, x: Int, y: Int)
    {
        graphics.drawImage(delegate, x, y, null)
    }

    fun clearRect(x: Int, y: Int, width: Int, height: Int)
    {
        delegate.getGraphics().clearRect(x, y, width, height)
    }

    fun drawImage(image: java.awt.Image, x: Int, y: Int)
    {
        delegate.getGraphics().drawImage(image, x, y, null)
    }

    fun drawImage(image: Image, x: Int, y: Int)
    {
        drawImage(image.delegate, x, y)
    }

    fun scaleTo(width: Int, height: Int): Image
    {
        // TODO this does not look efficient
        val scaledDelegate = delegate.getScaledInstance(width, height, java.awt.Image.SCALE_FAST)
        val scaled = Image(width, height)
        scaled.drawImage(scaledDelegate, 0, 0)
        return scaled
    }

    fun getGraphics(): Graphics2D = delegate.getGraphics() as Graphics2D
}
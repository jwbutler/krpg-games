package com.jwbutler.krpg.graphics.awt

import com.jwbutler.krpg.graphics.images.Image
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

/**
 * [Image] implementation that delegates to AWT's [BufferedImage]
 */
class ImageAWT(private val delegate: BufferedImage) : Image
{
    constructor(width: Int, height: Int) : this(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))

    override val width = delegate.width
    override val height = delegate.height

    override fun getRGB(x: Int, y: Int) = delegate.getRGB(x, y)
    override fun setRGB(x: Int, y: Int, rgb: Int) = delegate.setRGB(x, y, rgb)

    override fun clearRect(x: Int, y: Int, width: Int, height: Int)
    {
        delegate.getGraphics().clearRect(x, y, width, height)
    }

    override fun drawRect(left: Int, top: Int, width: Int, height: Int, color: Color)
    {
        val graphics = delegate.getGraphics()
        graphics.setColor(color)
        graphics.drawRect(left, top, width, height)
    }

    override fun fillRect(left: Int, top: Int, width: Int, height: Int, color: Color)
    {
        val graphics = delegate.getGraphics()
        graphics.setColor(color)
        graphics.fillRect(left, top, width, height)
    }

    override fun drawImage(image: Image, x: Int, y: Int)
    {
        delegate.getGraphics().drawImage((image as ImageAWT).delegate, x, y, null)
    }

    override fun drawText(text: String, font: Font, x: Int, y: Int)
    {
        val graphics = delegate.getGraphics() as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        graphics.setFont(font)
        graphics.drawString(text, x, y)
    }

    override fun scale(scaledWidth: Int, scaledHeight: Int): Image
    {
        val scaled = ImageAWT(scaledWidth, scaledHeight)
        val graphics = scaled.delegate.getGraphics()
        graphics.drawImage(delegate, 0, 0, scaledWidth, scaledHeight, null)
        return scaled
    }

    override fun scale2x(): Image = scale(width * 2, height * 2)

    // Methods specific to the AWT implementation below

    fun drawOnto(otherGraphics: Graphics, x: Int, y: Int, width: Int, height: Int)
    {
        otherGraphics.drawImage(delegate, x, y, width, height, null)
    }
}
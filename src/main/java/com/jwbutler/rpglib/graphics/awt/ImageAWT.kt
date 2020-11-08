package com.jwbutler.rpglib.graphics.awt

import com.jwbutler.rpglib.graphics.images.Image
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage

/**
 * [Image] implementation that delegates to AWT's [BufferedImage]
 */
internal class ImageAWT(private val delegate: BufferedImage) : Image
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
        delegate.getGraphics().setFont(font)
        delegate.getGraphics().drawString(text, x, y)
    }

    // Methods specific to the AWT implementation below

    fun drawOnto(otherGraphics: Graphics, x: Int, y: Int, width: Int, height: Int)
    {
        otherGraphics.drawImage(delegate, x, y, width, height, null)
    }
}
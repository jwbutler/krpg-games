package com.jwbutler.krpg.graphics

import java.awt.image.BufferedImage
import java.io.IOException
import java.io.UncheckedIOException
import javax.imageio.ImageIO

class ImageLoader private constructor()
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    {
        val baseImage = _imageFromFile(filename)
        val copy = _copyImage(baseImage)
        (0 until baseImage.height).forEach { y ->
            (0 until baseImage.width).forEach { x ->
                paletteSwaps?.forEach { src, dest ->
                    // TODO this probably doesn't handle alpha correctly
                    if (src.getRGB() == copy.getRGB(x, y))
                    {
                        copy.setRGB(x, y, dest.getRGB())
                    }
                }
            }
        }
        return copy
    }

    private fun _copyImage(baseImage: Image): Image
    {
        val copy = BufferedImage(baseImage.width, baseImage.height, BufferedImage.TYPE_INT_ARGB)
        copy.getGraphics().drawImage(baseImage, 0, 0, null)
        return copy
    }

    private fun _imageFromFile(filename: String): Image
    {
        val fullFilename = "/png/${filename}.png"
        try
        {
            val url = ImageLoader::class.java.getResource(fullFilename) ?: error("Could not open filename ${fullFilename}")
            return ImageIO.read(url.openStream())
        }
        catch (e: IOException)
        {
            println("Can't read input file: ${fullFilename}")
            e.printStackTrace()
            throw UncheckedIOException(e)
        }
    }

    companion object
    {
        private var INSTANCE : ImageLoader? = null

        fun getInstance(): ImageLoader
        {
            return INSTANCE ?: error("ImageLoader has not been instantiated")
        }

        fun initialize()
        {
            INSTANCE = ImageLoader()
        }
    }
}
package com.jwbutler.krpg.graphics.images

import java.awt.image.BufferedImage
import java.io.IOException
import java.io.UncheckedIOException
import java.net.URL
import javax.imageio.ImageIO

fun applyPaletteSwaps(baseImage: Image, paletteSwaps: PaletteSwaps?): Image
{
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

fun imageFromFile(filename: String): Image
{
    val fullFilename = "/png/${filename}.png"
    try
    {
        val url = _getFileURL(fullFilename)
        return Image(ImageIO.read(url.openStream()))
    }
    catch (e: IOException)
    {
        println("Can't read input file: ${fullFilename}")
        e.printStackTrace()
        throw UncheckedIOException(e)
    }
}

fun imageFromFileOptional(filename: String): Image?
{
    val fullFilename = "/png/${filename}.png"
    try
    {
        val url = ImageLoader::class.java.getResource(fullFilename)
        if (url != null)
        {
            return Image(ImageIO.read(url.openStream()))
        }
    }
    catch (e: IOException)
    {
        // this is expected
    }
    return null
}

private fun _copyImage(baseImage: Image): Image
{
    val copy = BufferedImage(baseImage.width, baseImage.height, BufferedImage.TYPE_INT_ARGB)
    baseImage.draw(copy.getGraphics(), 0, 0)
    return Image(copy)
}

private fun _getFileURL(filename: String): URL
{
    return ImageLoader::class.java.getResource(filename) ?: error("Could not open filename ${filename}")
}
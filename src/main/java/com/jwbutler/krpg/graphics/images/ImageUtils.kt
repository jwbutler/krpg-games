package com.jwbutler.krpg.graphics.images

import com.jwbutler.krpg.graphics.awt.ImageAWT
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
        return ImageAWT(ImageIO.read(url.openStream()))
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
            return ImageAWT(ImageIO.read(url.openStream()))
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
    val copy = Image.create(baseImage.width, baseImage.height)
    copy.drawImage(baseImage, 0, 0)
    return copy
}

private fun _getFileURL(filename: String): URL
{
    return ImageLoader::class.java.getResource(filename) ?: error("Could not open filename ${filename}")
}
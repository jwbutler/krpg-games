package com.jwbutler.krpg.graphics.awt

import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import java.io.IOException
import java.io.UncheckedIOException
import java.net.URL
import javax.imageio.ImageIO

/**
 * This implementation uses two caches to optimize performance:
 * [cacheWithoutSwaps] stores the base images based on their filenames,
 * and [cacheWithSwaps] stores each palette-swapped version of the base image.
 *
 * TODO: Convert maps to LRU caches
 */
class ImageloaderAWT : ImageLoader
{
    /**
     * Note that this has nullable values
     */
    private val cacheWithoutSwaps = mutableMapOf<String, Image?>()
    private val cacheWithSwaps = mutableMapOf<Pair<String, PaletteSwaps>, Image>()

    override fun loadImage(filename: String, paletteSwaps: PaletteSwaps?): Image
    {
        val baseImage = cacheWithoutSwaps.computeIfAbsent(filename) { _imageFromFile(filename) }!!
        if (paletteSwaps == null)
        {
            return baseImage
        }

        return cacheWithSwaps.computeIfAbsent(filename to paletteSwaps) { _applyPaletteSwaps(baseImage, paletteSwaps) }
    }

    override fun loadOptional(filename: String, paletteSwaps: PaletteSwaps?): Image?
    {
        val baseImage: Image

        // Can't use computeIfAbsent() because it won't insert nulls into the map
        if (cacheWithoutSwaps.containsKey(filename))
        {
            baseImage = cacheWithoutSwaps[filename] ?: return null
        }
        else
        {
            val baseImageOptional: Image? = _imageFromFileOptional(filename)
            cacheWithoutSwaps[filename] = baseImageOptional
            baseImage = baseImageOptional ?: return null
        }

        if (paletteSwaps == null)
        {
            return baseImage
        }

        return cacheWithSwaps.computeIfAbsent(filename to paletteSwaps) { _applyPaletteSwaps(baseImage, paletteSwaps) }
    }
}

/**
 * No use for this but let's keep it around I guess since it's so simple
 */
private class BasicImageLoaderAWT : ImageLoader
{
    override fun loadImage(filename: String, paletteSwaps: PaletteSwaps?): Image
    {
        val baseImage = _imageFromFile(filename)
        return _applyPaletteSwaps(baseImage, paletteSwaps)
    }

    override fun loadOptional(filename: String, paletteSwaps: PaletteSwaps?): Image?
    {
        val baseImage = _imageFromFileOptional(filename)
        if (baseImage != null)
        {
            return _applyPaletteSwaps(baseImage, paletteSwaps)
        }
        return null
    }
}

private fun _applyPaletteSwaps(baseImage: Image, paletteSwaps: PaletteSwaps?): Image
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

private fun _imageFromFile(filename: String): Image
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

private fun _imageFromFileOptional(filename: String): Image?
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
    // TODO - weird to reference ImageLoader here, maybe there is a more idiomatic
    // Kotlin way to grab the resource
    return ImageLoader::class.java.getResource(filename) ?: error("Could not open filename ${filename}")
}
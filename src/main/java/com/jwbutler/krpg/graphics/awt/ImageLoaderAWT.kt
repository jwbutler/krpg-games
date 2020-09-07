package com.jwbutler.krpg.graphics.awt

import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.images.applyPaletteSwaps
import com.jwbutler.krpg.graphics.images.imageFromFile
import com.jwbutler.krpg.graphics.images.imageFromFileOptional

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
        val baseImage = cacheWithoutSwaps.computeIfAbsent(filename) { imageFromFile(filename) }!!
        if (paletteSwaps == null)
        {
            return baseImage
        }

        return cacheWithSwaps.computeIfAbsent(filename to paletteSwaps) { applyPaletteSwaps(baseImage, paletteSwaps) }
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
            val baseImageOptional: Image? = imageFromFileOptional(filename)
            cacheWithoutSwaps[filename] = baseImageOptional
            baseImage = baseImageOptional ?: return null
        }

        if (paletteSwaps == null)
        {
            return baseImage
        }

        return cacheWithSwaps.computeIfAbsent(filename to paletteSwaps) { applyPaletteSwaps(baseImage, paletteSwaps) }
    }
}

/**
 * No use for this but let's keep it around I guess since it's so simple
 */
private class BasicImageLoaderAWT : ImageLoader
{
    override fun loadImage(filename: String, paletteSwaps: PaletteSwaps?): Image
    {
        val baseImage = imageFromFile(filename)
        return applyPaletteSwaps(baseImage, paletteSwaps)
    }

    override fun loadOptional(filename: String, paletteSwaps: PaletteSwaps?): Image?
    {
        val baseImage = imageFromFileOptional(filename)
        if (baseImage != null)
        {
            return applyPaletteSwaps(baseImage, paletteSwaps)
        }
        return null
    }
}
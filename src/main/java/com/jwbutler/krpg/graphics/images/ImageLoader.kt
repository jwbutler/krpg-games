package com.jwbutler.krpg.graphics.images

import com.jwbutler.krpg.core.SingletonHolder

private const val USE_CACHED_IMPL = true

interface ImageLoader
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    fun loadOptional(filename: String, paletteSwaps: PaletteSwaps? = null): Image?

    companion object : SingletonHolder<ImageLoader>(
    {
        if (USE_CACHED_IMPL)
        {
            CachedImpl()
        }
        else
        {
            BasicImpl()
        }
    })
}

private class BasicImpl : ImageLoader
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

/**
 * This implementation uses two caches to optimize performance:
 * [cacheWithoutSwaps] stores the base images based on their filenames,
 * and [cacheWithSwaps] stores each palette-swapped version of the base image.
 *
 * TODO: Convert maps to LRU caches
 */
private class CachedImpl : ImageLoader
{
    /**
     * Note that this has nullable values
     */
    val cacheWithoutSwaps = mutableMapOf<String, Image?>()
    val cacheWithSwaps = mutableMapOf<Pair<String, PaletteSwaps>, Image>()

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
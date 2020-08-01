package com.jwbutler.krpg.graphics.images

import com.jwbutler.krpg.core.SingletonHolder

class ImageLoader private constructor()
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    {
        val baseImage = imageFromFile(filename)
        return applyPaletteSwaps(baseImage, paletteSwaps)
    }

    fun loadOptional(filename: String, paletteSwaps: PaletteSwaps? = null): Image?
    {
        val baseImage = imageFromFileOptional(filename)
        if (baseImage != null)
        {
            return applyPaletteSwaps(baseImage, paletteSwaps)
        }
        return null
    }

    companion object : SingletonHolder<ImageLoader>(::ImageLoader)
}
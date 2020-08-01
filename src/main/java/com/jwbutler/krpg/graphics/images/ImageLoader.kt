package com.jwbutler.krpg.graphics.images

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
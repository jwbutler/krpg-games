package com.jwbutler.krpg.graphics.images

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.graphics.awt.ImageloaderAWT

interface ImageLoader
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    fun loadOptional(filename: String, paletteSwaps: PaletteSwaps? = null): Image?

    companion object : SingletonHolder<ImageLoader>(::ImageloaderAWT)
}
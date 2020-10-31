package com.jwbutler.rpglib.graphics.images

import com.jwbutler.rpglib.core.SingletonHolder

interface ImageLoader
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    fun loadOptional(filename: String, paletteSwaps: PaletteSwaps? = null): Image?

    companion object : SingletonHolder<ImageLoader>()
}
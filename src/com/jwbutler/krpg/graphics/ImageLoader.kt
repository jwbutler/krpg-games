package com.jwbutler.krpg.graphics

import java.lang.IllegalStateException

class ImageLoader private constructor()
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image = TODO()

    companion object
    {
        private var INSTANCE : ImageLoader? = null

        fun getInstance(): ImageLoader
        {
            return INSTANCE ?: throw IllegalStateException()
        }

        fun initialize()
        {
            INSTANCE = ImageLoader()
        }
    }
}
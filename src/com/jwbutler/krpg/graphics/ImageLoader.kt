package com.jwbutler.krpg.graphics

import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import javax.imageio.ImageIO

class ImageLoader private constructor()
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    {
        try
        {
            return ImageIO.read(File(filename))
        }
        catch (e: IOException)
        {
            println("fux")
            e.printStackTrace()
            throw UncheckedIOException(e)
        }
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
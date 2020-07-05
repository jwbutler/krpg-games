package com.jwbutler.krpg.graphics

import java.io.IOException
import java.io.UncheckedIOException
import javax.imageio.ImageIO

class ImageLoader private constructor()
{
    fun loadImage(filename: String, paletteSwaps: PaletteSwaps? = null): Image
    {
        val fullFilename = "/png/${filename}.png"
        try
        {
            val url = ImageLoader::class.java.getResource(fullFilename) ?: error("Could not open filename ${fullFilename}")
            return ImageIO.read(url.openStream())
        }
        catch (e: IOException)
        {
            println("Can't read input file: ${fullFilename}")
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
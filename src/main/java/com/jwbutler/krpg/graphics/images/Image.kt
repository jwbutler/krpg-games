package com.jwbutler.krpg.graphics.images

import com.jwbutler.krpg.graphics.awt.ImageAWT
import java.awt.Color
import java.awt.Font

interface Image
{
    val width: Int
    val height: Int

    fun getRGB(x: Int, y: Int): Int
    fun setRGB(x: Int, y: Int, rgb: Int)

    fun clearRect(x: Int, y: Int, width: Int, height: Int)
    fun drawRect(left: Int, top: Int, width: Int, height: Int, color: Color)
    fun fillRect(left: Int, top: Int, width: Int, height: Int, color: Color)
    fun drawImage(image: Image, x: Int, y: Int)
    fun drawText(text: String, font: Font, x: Int, y: Int)

    fun scale(scaledWidth: Int, scaledHeight: Int): Image
    fun scale2x(): Image

    companion object
    {
        fun create(width: Int, height: Int) = ImageAWT(width, height)
    }
}
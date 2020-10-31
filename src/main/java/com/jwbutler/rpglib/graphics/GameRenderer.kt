package com.jwbutler.rpglib.graphics

import com.jwbutler.rpglib.core.SingletonHolder
import com.jwbutler.rpglib.graphics.images.Image

/**
 * This class renders the game at its "native" resolution.
 */
interface GameRenderer
{
    val width: Int
    val height: Int

    fun render(): Image
    fun getImage(): Image

    companion object : SingletonHolder<GameRenderer>()
}
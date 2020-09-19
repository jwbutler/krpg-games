package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.graphics.awt.GameRendererAWT
import com.jwbutler.krpg.graphics.images.Image

/**
 * This class renders the game at its "native" resolution.
 */
interface GameRenderer
{
    val width: Int
    val height: Int

    fun render(): Image
    fun getImage(): Image

    companion object : SingletonHolder<GameRenderer>({ GameRendererAWT(GAME_WIDTH, GAME_HEIGHT) })
}
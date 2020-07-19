package com.jwbutler.krpg.graphics

import java.awt.Color

private val TRANSPARENT_BLACK = Color(0, 0, 0, 0)

class PaletteSwaps(delegate: Map<Color, Color>)
{
    private val delegate = delegate.toMutableMap()

    fun forEach(action: (Color, Color) -> Unit)
    {
        delegate.forEach(action)
    }

    fun withTransparentColor(color: Color): PaletteSwaps
    {
        val copy = delegate.toMutableMap()
        copy[color] = TRANSPARENT_BLACK
        return PaletteSwaps(copy)
    }

    fun put(map: Map<Color, Color>): PaletteSwaps
    {
        val copy = delegate.toMutableMap()
        copy.putAll(map)
        return PaletteSwaps(copy)
    }

    companion object
    {
        val WHITE_TRANSPARENT = PaletteSwaps(mutableMapOf(Color.WHITE to TRANSPARENT_BLACK))
        val BLACK_TRANSPARENT = PaletteSwaps(mutableMapOf(Color.BLACK to TRANSPARENT_BLACK))
    }
}
package com.jwbutler.krpg.graphics.ui

import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.Renderer

abstract class AbstractRenderer(private val window: Window) : Renderer
{
    override fun render()
    {
        window.clearBuffer()

        for ((image, pixel) in _getRenderables())
        {
            window.render(image, pixel)
        }

        window.redraw()
    }

    protected abstract fun _getRenderables(): List<Renderable>
}
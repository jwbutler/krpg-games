package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.graphics.ui.Window

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
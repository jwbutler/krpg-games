package com.jwbutler.rpglib.input

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 * [MouseAdapter] implementation that performs a custom event transformation
 * before delegating to another [MouseAdapter].  This is used to handle
 * pixel scaling, so that mouse events can cleanly use the game's internal
 * (scaled) pixels rather than the window's native pixels.
 */
internal class DelegatingMouseListener(val delegate: MouseAdapter, val transform: (MouseEvent) -> MouseEvent) : MouseAdapter()
{
    override fun mouseClicked(event: MouseEvent) = delegate.mouseClicked(transform(event))
    override fun mouseDragged(event: MouseEvent) = delegate.mouseDragged(transform(event))
    override fun mousePressed(event: MouseEvent) = delegate.mousePressed(transform(event))
    override fun mouseReleased(event: MouseEvent) = delegate.mouseReleased(transform(event))
}
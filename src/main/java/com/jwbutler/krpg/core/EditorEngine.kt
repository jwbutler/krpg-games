package com.jwbutler.krpg.core

import com.jwbutler.krpg.graphics.Renderer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val FRAME_INTERVAL = 83 // ~12 FPS

class EditorEngine(private val renderer: Renderer) : GameEngine
{
    private var initialized = false

    override fun start()
    {
        check(!initialized)
        GlobalScope.launch {
            while (true)
            {
                doLoop()
                delay(FRAME_INTERVAL.toLong())
            }
        }
        initialized = true
    }

    override fun pause() {} // N/A
    override fun unpause() {} // N/A
    override fun togglePause() {} // N/A
    override fun isPaused() = false

    override fun doLoop()
    {
        renderer.render()
    }
}
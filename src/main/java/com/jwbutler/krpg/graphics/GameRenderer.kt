package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.graphics.ui.HUDRenderer

interface GameRenderer
{
    val gameWidth: Int
    val gameHeight: Int
    val scaleRatioX: Double
    val scaleRatioY: Double
    val tileWidth: Int
    val tileHeight: Int

    fun render()


    companion object : SingletonHolder<GameRenderer>(::GameRendererImpl)
}

private class GameRendererImpl : GameRenderer
{
    override val gameWidth = 640
    override val gameHeight = 360
    override val scaleRatioX = 2.0
    override val scaleRatioY = 0.75
    override val tileWidth = 24
    override val tileHeight = 12

    override fun render()
    {
        val window = GameWindow.getInstance()
        window.clearBuffer()

        for ((image, pixel) in _getRenderables())
        {
            window.render(image, pixel)
        }

        val (image, pixel) = HUDRenderer.render()
        window.render(image, pixel)

        window.redraw()
    }

    private fun _getRenderables(): List<Renderable>
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        val humanPlayer = state.getHumanPlayer()
        val tileOverlays = humanPlayer.getTileOverlays().values
        val uiOverlays = humanPlayer.getUIOverlays()

        val renderables = entities.plus(tileOverlays).map { it to it.render() }

        return renderables
            .sortedBy { (_, renderable) -> renderable.layer }
            .sortedBy { (entity, _) -> entity.getCoordinates().y }
            .map { (_, renderable) -> renderable }
            .map { (image, pixel, layer) -> Renderable(image.scaleBy(scaleRatioX, scaleRatioY), pixel, layer) }
            .plus(uiOverlays) // TODO: Get these into the sort somehow
    }
}
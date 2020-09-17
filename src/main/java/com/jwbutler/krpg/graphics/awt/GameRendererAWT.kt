package com.jwbutler.krpg.graphics.awt

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.graphics.ui.HUDRenderer

class GameRendererAWT
(
    override val width: Int,
    override val height: Int
) : GameRenderer
{
    private val buffer: Image = ImageAWT(GAME_WIDTH, GAME_HEIGHT)

    override fun render(): Image
    {
        _clearBuffer()

        for ((image, pixel) in _getRenderables())
        {

            buffer.drawImage(image, pixel.x, pixel.y)
        }

        val (image, pixel) = HUDRenderer.render()
        buffer.drawImage(image, pixel.x, pixel.y)
        return buffer
    }

    override fun getImage(): Image = buffer

    private fun _clearBuffer()
    {
        buffer.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT)
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
            .plus(uiOverlays) // TODO: Get these into the sort somehow
    }
}
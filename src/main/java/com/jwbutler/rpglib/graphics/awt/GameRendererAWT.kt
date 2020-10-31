package com.jwbutler.rpglib.graphics.awt

import com.jwbutler.krpg.geometry.GeometryConstants.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GeometryConstants.GAME_WIDTH
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.graphics.GameRenderer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.rpglib.graphics.images.Image
import com.jwbutler.krpg.graphics.ui.HUDRenderer
import com.jwbutler.krpg.players.HumanPlayer

class GameRendererAWT
(
    override val width: Int,
    override val height: Int
) : GameRenderer
{
    /**
     * TODO dependency on krpg
     */
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
        // TODO dependency on KRPG
        val humanPlayer = state.getHumanPlayer() as HumanPlayer
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
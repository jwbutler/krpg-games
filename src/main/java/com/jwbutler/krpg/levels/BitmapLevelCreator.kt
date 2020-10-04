package com.jwbutler.krpg.levels

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.objects.Tree
import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.objects.WallTop
import com.jwbutler.krpg.entities.tiles.Tile
import com.jwbutler.krpg.entities.tiles.TileType
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.players.Player
import java.awt.Color

// TODO: Lots of hardcoded color values here.
// In the future these will be specified individually for each level.
private object LevelColors
{
    val DIRT = Colors.BROWN
    val ENEMY = Colors.ORANGE
    val GRASS = Colors.GREEN
    val STONE = Colors.DARK_GRAY
    val TREE = Colors.DARK_GREEN
    val WALL = Colors.LIGHT_GRAY
    val WALL_TOP = Colors.BLACK
    val BLANK = Colors.WHITE
    val START = Colors.RED
}

object BitmapLevelCreator
{
    fun loadLevel(
        filename: String,
        baseTileType: TileType = TileType.GRASS,
        victoryCondition: VictoryCondition = VictoryCondition.NONE
    ): Level
    {
        val fullFilename = "levels/${filename}"
        val image = ImageLoader.getInstance().loadImage(fullFilename)

        val tiles = mutableMapOf<Coordinates, Tile>()
        val objects = mutableMapOf<Coordinates, Collection<GameObject>>()
        val units = mutableListOf<Level.UnitData>()
        var startPosition = Coordinates(0, 0)

        for (y in 0 until image.height)
        {
            for (x in 0 until image.width)
            {
                val coordinates = Coordinates(x, y)
                val color = Color(image.getRGB(x, y))
                val tileType = _getTile(color, baseTileType)
                if (tileType != null)
                {
                    tiles[coordinates] = Tile(tileType, coordinates)
                }

                val `object` = _getObject(color)
                if (`object` != null)
                {
                    objects[coordinates] = listOf(`object`)
                }

                val playerUnitPair = _getUnit(color)
                if (playerUnitPair != null)
                {
                    val (player, unit) = playerUnitPair
                    units += Level.UnitData(unit, coordinates, player, mutableMapOf()) // TODO Equipment
                }

                if (color == LevelColors.START)
                {
                    startPosition = coordinates
                }
            }
        }

        return Level(
            tiles = tiles,
            units = units,
            objects = objects,
            startPosition = startPosition,
            victoryCondition = victoryCondition
        )
    }

    private fun _getTile(color: Color, baseTileType: TileType): TileType?
    {
        return when (color)
        {
            LevelColors.GRASS -> TileType.GRASS
            LevelColors.DIRT  -> TileType.DIRT
            LevelColors.STONE -> TileType.STONE
            LevelColors.BLANK -> null
            else              -> baseTileType
        }
    }

    private fun _getObject(color: Color): GameObject?
    {
        return when (color)
        {
            LevelColors.TREE     -> Tree()
            LevelColors.WALL     -> Wall()
            LevelColors.WALL_TOP -> WallTop()
            else                 -> null
        }
    }

    private fun _getUnit(color: Color): Pair<Player, Unit>?
    {
        val enemyPlayer = GameState.getInstance()
            .getPlayers()
            .filterNot(Player::isHuman)
            .first()

        return when (color)
        {
            LevelColors.ENEMY -> enemyPlayer to ZombieUnit(50)
            else              -> null
        }
    }
}
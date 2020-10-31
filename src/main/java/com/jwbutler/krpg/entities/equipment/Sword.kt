package com.jwbutler.krpg.entities.equipment

import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.SwordSprite
import com.jwbutler.rpglib.entities.equipment.EquipmentSlot

class Sword : AbstractEquipment(SwordSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = RPGEquipmentSlot.MAIN_HAND
}
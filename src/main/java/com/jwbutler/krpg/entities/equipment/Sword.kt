package com.jwbutler.krpg.entities.equipment

import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.SwordSprite

class Sword : AbstractEquipment(SwordSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = RPGEquipmentSlot.MAIN_HAND
}
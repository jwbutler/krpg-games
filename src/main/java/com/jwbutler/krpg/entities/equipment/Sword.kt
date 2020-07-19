package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.SwordSprite

class Sword : AbstractEquipment(SwordSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = EquipmentSlot.MAIN_HAND
}
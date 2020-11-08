package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.graphics.sprites.MailArmorSprite
import com.jwbutler.rpglib.graphics.images.PaletteSwaps

class MailArmor : AbstractEquipment(MailArmorSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = RPGEquipmentSlot.CHEST
}
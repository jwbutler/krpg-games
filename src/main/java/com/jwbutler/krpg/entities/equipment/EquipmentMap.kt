package com.jwbutler.krpg.entities.equipment

class EquipmentMap
{
    private val delegate: Map<EquipmentSlot, Equipment> = mutableMapOf()

    fun getEquipment(slot: EquipmentSlot): Equipment?
    {
        return delegate.get(slot)
    }
}
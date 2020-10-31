package com.jwbutler.rpglib.behavior

import com.jwbutler.rpglib.entities.units.Unit

interface Activity
{
    fun onComplete(unit: Unit) {}
}
package com.github.syari.ss.plugins.rpg.core

object DamageCalculator {
    @OptIn(ExperimentalStdlibApi::class)
    fun get(attacks: Map<ElementType, Double>, defences: Map<ElementType, Double>): Map<ElementType, Double> {
        return buildMap {
            attacks.forEach { (attackType, attack) ->
                var sumDefence = 0.0
                defences.forEach { (defenceType, defence) ->
                    sumDefence += effectiveMap[attackType to defenceType]?.let {
                        defence / it
                    } ?: effectiveMap[defenceType to attackType]?.let {
                        defence * it
                    } ?: run {
                        defence
                    }
                }
                val damage = attack - sumDefence
                if (0 < damage) {
                    put(attackType, damage)
                }
            }
        }
    }

    private val effectiveMap = mapOf(
        ElementType.Fire to ElementType.Wood to 1.5,
        ElementType.Wood to ElementType.Water to 1.5,
        ElementType.Water to ElementType.Fire to 1.5,
        ElementType.Holy to ElementType.Dark to 2.0,
        ElementType.Dark to ElementType.Fire to 1.3,
        ElementType.Dark to ElementType.Water to 1.3,
        ElementType.Dark to ElementType.Wood to 1.3
    )
}

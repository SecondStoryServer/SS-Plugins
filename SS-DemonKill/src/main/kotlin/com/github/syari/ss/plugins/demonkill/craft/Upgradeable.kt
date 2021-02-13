package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.ss.plugins.core.item.CustomItemStack

open class Upgradeable(item: CustomItemStack) : DependItem(item) {
    interface Upgrade {
        val request: List<CustomItemStack>
    }
}

package com.github.syari.ss.wplugins.core.permission

import net.md_5.bungee.api.CommandSender

object Permission {
    const val admin = "ss.admin"

    inline val CommandSender.isOp
        get() = hasPermission(admin)
}

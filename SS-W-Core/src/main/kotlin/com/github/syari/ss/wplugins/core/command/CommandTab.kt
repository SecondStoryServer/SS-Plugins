package com.github.syari.ss.wplugins.core.command

sealed class CommandTab {
    /**
     * @see CommandTabContainer.arg
     */
    class Argument internal constructor(
        val arg: List<String>,
        val complete: CommandCompleteElement.() -> CommandTabElement?
    ) : CommandTab()

    /**
     * @see CommandTabContainer.flag
     */
    class Flag internal constructor(
        val arg: String,
        val flag: Map<String, CommandTabElement>
    ) : CommandTab()
}

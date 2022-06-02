package ru.debajo.reduktor

import kotlinx.coroutines.flow.Flow

/**
 * Command to trigger [CommandProcessor]
 */
interface Command

/**
 * Result of [CommandProcessor] working
 */
interface CommandResult {
    companion object {
        /**
         * When [CommandProcessor] should not return anything, you can pass an [CommandResult.EMPTY]
         */
        val EMPTY = object : CommandResult {}
    }
}

/**
 * Side effect processor of input commands
 */
typealias CommandProcessor = (commands: Flow<Command>) -> Flow<CommandResult>

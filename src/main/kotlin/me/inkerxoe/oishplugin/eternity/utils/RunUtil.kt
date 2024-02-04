package me.inkerxoe.oishplugin.eternity.utils

/**
 * @className Runnable
 *
 * @author Glom
 * @date 2023/1/7 22:18 Copyright 2023 user. All rights reserved.
 */
fun <T> safe(run: () -> T): T? {
    return runCatching { run() }.run {
        if (isSuccess) getOrNull()
        else {
            exceptionOrNull()?.printStackTrace()
            null
        }
    }
}
/*
 * Copyright (c) 2020 - 2022. Eritque arcus and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version(in your opinion).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tech.eritquearcus.nasaPlugin

import java.io.File
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

fun setupWorkingDir() {
    // see: net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal
    System.setProperty("user.dir", File("debug-sandbox").absolutePath)
}

@OptIn(ConsoleExperimentalApi::class)
suspend fun main() {
    setupWorkingDir()

    MiraiConsoleTerminalLoader.startAsDaemon()

    val pluginInstance = NasaPlugin

    pluginInstance.load() // 主动加载插件, Console 会调用 NasaPlugin.onLoad
    pluginInstance.enable() // 主动启用插件, Console 会调用 NasaPlugin.onEnable

    val bot = MiraiConsole.addBot(TestConfig().account, TestConfig().password).alsoLogin() // 登录一个测试环境的 Bot

    MiraiConsole.job.join()
}

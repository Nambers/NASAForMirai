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

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import java.lang.IllegalArgumentException

private suspend fun handle(e: MessageEvent, config: Config) {
    val param = e.message.serializeToMiraiCode().split(" ")
    for (a in commandTexts) {
        if (config.commandStatus.containsKey(a.key.toString()) && config.commandStatus[a.key.toString()] == false)
            continue
        if (param[0] == a.value.second)
            a.value.first.build(config, param, e)
    }
}

object NasaPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "tech.eritquearcus.nasaPlugin",
        name = "NasaPlugin",
        version = "1.0-SNAPSHOT",
    ) {
        author("Eritque arcus")
    }
) {
    override fun onEnable() {
        val configFile = this.resolveConfigFile("config.json")
        if (!configFile.exists()) {
            logger.error("配置文件不存在, 生成并退出加载")
            configFile.writeText(gson.toJson(Config("apikey")))
            return
        }
        val config = gson.fromJson(configFile.readText(), Config::class.java)
        for(a in config.commandName){
            val b = try {
                CommandType.valueOf(a.key)
            }catch (_:IllegalArgumentException){
                NasaPlugin.logger.error(a.key + "不是正确的参数, 可选参数有: " + CommandType.values().joinToString("/"))
                continue
            }
            commandTexts[b] = (commandTexts[b]!!.first to a.value)
        }
        if (config.enableGroup) globalEventChannel().subscribeAlways<GroupMessageEvent> {
            handle(this, config)
        }
        if (config.enableFriend) globalEventChannel().subscribeAlways<FriendMessageEvent> {
            handle(this, config)
        }
    }
}
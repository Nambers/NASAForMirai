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

package tech.eritquearcus.nasaPlugin.commands

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.buildForwardMessage
import tech.eritquearcus.nasaPlugin.*

class Earth : CommandHandler {
    override suspend fun build(config: Config, data: List<String>, e: MessageEvent) {
        // commandName, lon, lat, date
        if (data.size != 4) return
        if (data[1].toFloatOrNull() == null || data[2].toFloatOrNull() == null) return
        val date = getDate(data[3]) ?: let {
            e.subject.sendMessage("日期格式错误, 需要符合yyy-MM-DD 如 2022-01-01")
            return
        }
        val re = request<EarthRe>(
            config,
            "https://api.nasa.gov/planetary/earth/assets",
            mapOf("lon" to data[1], "lat" to data[2], "date" to date)
        )
        try {
            val img = e.subject.uploadImage(getImage(re.url))
            val msg = buildForwardMessage(e.subject, CustomDisplayStrategy("photo in ${data[1]}° ${data[2]}°")) {
                e.bot says img
                e.bot says """
            |地点: ${data[1]}° ${data[2]}°
            |拍摄时间: ${re.date}
            |下载地址: ${re.url}
            """.trimIndent()
            }
            e.subject.sendMessage(msg)
        }catch (e:Exception){
            NasaPlugin.logger.error(e.message)
            NasaPlugin.logger.error("Nasa返回" + gson.toJson(re))
        }
    }
}
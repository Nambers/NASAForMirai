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
import net.mamoe.mirai.message.data.*
import tech.eritquearcus.nasaPlugin.*
import java.lang.Exception

class APOD: CommandHandler {
    override suspend fun build(config: Config, data: List<String>, e: MessageEvent){
        if(data.isNotEmpty() && data.size > 2) return
        val date = getDate(data[1])
        if(date == null){
            e.subject.sendMessage("日期格式错误, 需要符合yyy-MM-dd 如 2022-01-01")
            return
        }
        val re = request<APODRe>(config, "https://api.nasa.gov/planetary/apod", mapOf("date" to date, "thumbs" to "true"))
        try {
            val img = e.subject.uploadImage(getImage(re.url))
            val msg = buildForwardMessage(e.subject, CustomDisplayStrategy(re.title + " from NASA")){
                e.bot says "标题:${re.title}\n"
                e.bot says img
                e.bot says """
            |下载地址: ${re.url}
            |拍摄时间: ${re.date}
            |版权: ${re.copyright}
            """.trimMargin()
                e.bot says "描述:${re.explanation}"
            }
            e.subject.sendMessage(msg)
        }catch (e:Exception){
            NasaPlugin.logger.error(e.message)
            NasaPlugin.logger.error("Nasa返回" + gson.toJson(re))
        }
    }
}
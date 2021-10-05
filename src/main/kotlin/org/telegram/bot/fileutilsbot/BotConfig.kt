package org.telegram.bot.fileutilsbot

import com.typesafe.config.Config

data class BotSecurityConfig(val token: String)

data class BotConfig(val botName: String)

fun parseBotSecurityConfig(config: Config): BotSecurityConfig {
    return BotSecurityConfig(config.getString("token"))
}
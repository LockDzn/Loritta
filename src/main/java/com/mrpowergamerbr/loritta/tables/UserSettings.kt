package com.mrpowergamerbr.loritta.tables

import com.mrpowergamerbr.loritta.utils.locale.Gender
import org.jetbrains.exposed.dao.LongIdTable

object UserSettings : LongIdTable() {
	val aboutMe = text("about_me").nullable()
	val gender = enumeration("gender", Gender::class)
	val hidePreviousUsernames = bool("hide_previous_usernames")
	val hideSharedServers = bool("hide_shared_servers")
	val hideLastSeen = bool("hide_last_seen")
}
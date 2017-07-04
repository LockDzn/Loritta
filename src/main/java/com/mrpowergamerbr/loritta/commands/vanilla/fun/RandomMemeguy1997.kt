package com.mrpowergamerbr.loritta.commands.vanilla.`fun`

import com.github.kevinsawicki.http.HttpRequest
import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonParser
import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.commands.CommandBase
import com.mrpowergamerbr.loritta.commands.CommandCategory
import com.mrpowergamerbr.loritta.commands.CommandContext
import com.mrpowergamerbr.loritta.utils.LorittaUtils
import java.awt.image.BufferedImage

class RandomMemeguy1997 : CommandBase() {
	override fun getLabel(): String {
		return "randommemeguy1997"
	}

	override fun getAliases(): List<String> {
		return listOf("randommemeguy", "randomeme", "randomemeguy", "randomemeguy1997")
	}

	override fun getDescription(): String {
		return "Pega uma postagem aleatória do Memeguy1997"
	}

	override fun getCategory(): CommandCategory {
		return CommandCategory.FUN
	}

	override fun needsToUploadFiles(): Boolean {
		return true
	}

	override fun run(context: CommandContext) {
		val source = if (Loritta.random.nextBoolean()) "página" else "grupo";

		var response: String?;
		if (source == "página") {
			response = HttpRequest.get("https://graph.facebook.com/v2.9/memeguy1997/posts?fields=attachments{url,subattachments,media,description}&access_token=${Loritta.config.facebookToken}&offset=${Loritta.random.nextInt(0, 1000)}").body();
		} else {
			response = HttpRequest.get("https://graph.facebook.com/380626148947201/feed?fields=message,attachments{url,subattachments,media,description}&access_token=${Loritta.config.facebookToken}&offset=${Loritta.random.nextInt(0, 1000)}").body();
		}

		val json = JsonParser().parse(response)

		var url: String? = null;
		var description: String? = null;
		var image: BufferedImage? = null;

		for (post in json["data"].array) {
			var foundUrl = post["attachments"]["data"][0]["url"].string;

			if (!foundUrl.contains("video")) {
				url = post["attachments"]["data"][0]["media"]["image"]["src"].string;
				description = if (source == "página") post["attachments"]["data"][0]["description"].string else post["message"].string
				image = LorittaUtils.downloadImage(url, 4000)
				if (image != null) {
					break;
				}
			}
		}

		if (url != null && description != null) {
			context.sendFile(image, "memeguy1997.png", "<:memeguy1997:331617335909548042> | " + context.getAsMention(true) + "(Fonte: *$source do Memeguy1997*) `$description`")
		} else {
			context.sendMessage(LorittaUtils.ERROR + " | " + context.getAsMention(true) + "Não consegui encontrar nenhum meme na página do Memeguy1997...")
		}
	}
}
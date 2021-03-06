package com.mrpowergamerbr.loritta.commands.vanilla.economy

import com.mrpowergamerbr.loritta.Loritta.Companion.RANDOM
import com.mrpowergamerbr.loritta.commands.AbstractCommand
import com.mrpowergamerbr.loritta.commands.CommandCategory
import com.mrpowergamerbr.loritta.commands.CommandContext
import com.mrpowergamerbr.loritta.network.Databases
import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.LoriReply
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import com.mrpowergamerbr.loritta.utils.loritta
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.Executors

class LigarCommand : AbstractCommand("ligar", category = CommandCategory.ECONOMY) {
	companion object {
		val coroutineExecutor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
	}

	override fun getDescription(locale: BaseLocale): String {
		return "Experimental"
	}

	override suspend fun run(context: CommandContext,locale: BaseLocale) {
		val phoneNumber = context.args.getOrNull(0)?.replace("-", "")

		if (phoneNumber != null) {
			if (phoneNumber == "40028922") {
				val profile = context.lorittaUser.profile

				if (75 > profile.money) {
					context.reply(
							"Você não tem sonhos suficientes para completar esta ligação!",
							Constants.ERROR
					)
					return
				}

				transaction(Databases.loritta) {
					profile.money -= 75
				}

				GlobalScope.launch(coroutineExecutor) {
					if (loritta.bomDiaECia.available) {
						val args = context.args.toMutableList()
						args.removeAt(0)
						val text = args.joinToString(" ")
								.toLowerCase()

						if (text.contains("\u200B") || text.contains("\u200C") || text.contains("\u200D")) {
							context.reply(
									LoriReply(
											"Poxa, não foi dessa vez amiguinho... mas não desista, ligue somente durante o programa, tá? Valeu! Aliás, não utilize CTRL-C e CTRL-V para você tentar vencer mais rápido. :^)",
											"<:yudi:446394608256024597>"
									)
							)
							return@launch
						}

						if (text != loritta.bomDiaECia.currentText) {
							context.reply(
									LoriReply(
											"Poxa, não foi dessa vez amiguinho... mas não desista, ligue somente durante o programa, tá? Valeu! Não se esqueça de escrever a nossa frase para que você possa ganhar o prêmio!",
											"<:yudi:446394608256024597>"
									)
							)
							return@launch
						}

						loritta.bomDiaECia.available = false

						val randomPrize = RANDOM.nextInt(150, 376)

						transaction(Databases.loritta) {
							profile.money += randomPrize
						}

						logger.info("${context.userHandle.id} ganhou ${randomPrize} no Bom Dia & Cia!")

						context.reply(
								LoriReply(
										"Rodamos a roleta e... Parabéns! Você ganhou **${randomPrize} Sonhos**!",
										"<:yudi:446394608256024597>"
								)
						)

						loritta.bomDiaECia.announceWinner(context.guild, context.userHandle)
					} else {
						context.reply(
								LoriReply(
										"Poxa, não foi dessa vez amiguinho... mas não desista, ligue somente durante o programa, tá? Valeu! (Você apenas deve ligar após a <@297153970613387264> anunciar no chat para ligar!)",
										"<:yudi:446394608256024597>"
								)
						)
					}
				}
			} else {
				context.reply(
						"Número desconhecido",
						"\uD83D\uDCF4"
				)
			}
		} else {
			this.explain(context)
		}
	}
}
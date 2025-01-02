package dev.reassembly.evalite

import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import dev.reassembly.evalite.commands.punishments.punishmentimpl.BanCommand
import dev.reassembly.evalite.commands.punishments.punishmentimpl.KickCommand
import dev.reassembly.evalite.controllers.network.NetworkController
import dev.reassembly.evalite.controllers.punishments.PunishmentController
import dev.reassembly.evalite.database.DatabaseHandler
import dev.reassembly.evalite.handlers.PlayerHandler
import dev.reassembly.evalite.listeners.PlayerListeners
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

val evalite: Evalite
    get() = JavaPlugin.getPlugin(Evalite::class.java)

class Evalite : SuspendingJavaPlugin() {

    lateinit var database: DatabaseHandler
    lateinit var messagesConfig: FileConfiguration
    lateinit var playerHandler: PlayerHandler
    lateinit var punishmentHandler: PunishmentController
    val networkHandler = NetworkController()

    override suspend fun onEnableAsync() {
        saveDefaultConfig()
        messagesConfig = YamlConfiguration.loadConfiguration(File(dataFolder, "messages.yml"))
        defineTagResolvers()
        launch {
            database = DatabaseHandler()
            database.init()
            punishmentHandler = database.punishmentController
        }
        registerListeners()
        registerCommands()
    }

    override suspend fun onDisableAsync() {

    }

    private fun registerListeners() {
        server.pluginManager.registerSuspendingEvents(PlayerListeners(), this)
    }

    private fun registerCommands() {
        val manager = PaperCommandManager(this)
        manager.registerCommand(BanCommand())
        manager.registerCommand(KickCommand())
    }

    override fun saveDefaultConfig() {
        super.saveDefaultConfig()
        val file = File(dataFolder, "messages.yml")
        if (!file.exists()) {
            saveResource("messages.yml", false)
        }
    }

    private fun defineTagResolvers() {
        TagResolver.resolver("prefix") { _, _ ->
            Tag.inserting(MiniMessage.miniMessage().deserialize(messagesConfig.getString("prefix") ?: "<dark_purple>[Evalite]"))
        }
        TagResolver.resolver("discord") { _, _ ->
            Tag.inserting(MiniMessage.miniMessage().deserialize(messagesConfig.getString("social-links.discord") ?: "N/A"))
        }
        TagResolver.resolver("website") { _, _ ->
            Tag.inserting(MiniMessage.miniMessage().deserialize(messagesConfig.getString("social-links.website") ?: "N/A"))
        }
        TagResolver.resolver("store") { _, _ ->
            Tag.inserting(MiniMessage.miniMessage().deserialize(messagesConfig.getString("social-links.store") ?: "N/A"))
        }
        TagResolver.resolver("twitter") { _, _ ->
            Tag.inserting(MiniMessage.miniMessage().deserialize(messagesConfig.getString("social-links.twitter") ?: "N/A"))
        }
        TagResolver.resolver("server-name") { _, _ ->
            Tag.inserting(MiniMessage.miniMessage().deserialize(messagesConfig.getString("server-name") ?: "N/A"))
        }
    }
}

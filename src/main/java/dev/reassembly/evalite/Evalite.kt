package dev.reassembly.evalite

import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import dev.reassembly.evalite.commands.punishments.BanCommand
import dev.reassembly.evalite.controllers.network.NetworkController
import dev.reassembly.evalite.controllers.punishments.PunishmentController
import dev.reassembly.evalite.database.DatabaseHandler
import dev.reassembly.evalite.handlers.PlayerHandler
import dev.reassembly.evalite.listeners.PlayerListeners
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

val evalite: Evalite
    get() = JavaPlugin.getPlugin(Evalite::class.java)

class Evalite : SuspendingJavaPlugin() {

    lateinit var database: DatabaseHandler
    lateinit var messagesConfig: FileConfiguration
    val playerHandler = PlayerHandler()
    val punishmentHandler = database.punishmentController
    val networkHandler = NetworkController()

    override suspend fun onEnableAsync() {
        saveDefaultConfig()

        messagesConfig = YamlConfiguration.loadConfiguration(File(dataFolder, "messages.yml"))

        database = DatabaseHandler()
        launch {
            database.init()
        }

        registerListeners()

    }

    override suspend fun onDisableAsync() {

    }

    private fun registerListeners() {
        server.pluginManager.registerSuspendingEvents(PlayerListeners(), this)
    }

    private fun registerCommands() {
        val manager = PaperCommandManager(this)
        manager.registerCommand(BanCommand())
    }

    override fun saveDefaultConfig() {
        super.saveDefaultConfig()
        val file = File(dataFolder, "messages.yml")
        if (!file.exists()) {
            saveResource("messages.yml", false)
        }
    }
}

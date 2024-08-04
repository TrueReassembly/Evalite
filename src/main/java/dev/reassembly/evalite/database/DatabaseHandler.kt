package dev.reassembly.evalite.database

import com.google.gson.Gson
import dev.reassembly.evalite.controllers.players.MySQLUserController
import dev.reassembly.evalite.controllers.players.SQLiteUserController
import dev.reassembly.evalite.controllers.players.UserController
import dev.reassembly.evalite.controllers.punishments.MySQLPunishmentController
import dev.reassembly.evalite.controllers.punishments.PunishmentController
import dev.reassembly.evalite.controllers.punishments.SQLitePunishmentController
import dev.reassembly.evalite.evalite
import gg.ingot.iron.Iron
import gg.ingot.iron.IronSettings
import gg.ingot.iron.representation.DBMS
import gg.ingot.iron.serialization.SerializationAdapter
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection

class DatabaseHandler {

    lateinit var sqlConnection: Iron
    lateinit var type: DBMS
    lateinit var punishmentController: PunishmentController
    lateinit var userController: UserController

    suspend fun init() {


        evalite.logger.info("Initializing the database connection.")
        val cfg: ConfigurationSection = evalite.config.getConfigurationSection("database") ?: run {
            evalite.logger.severe("Database configuration not found, please copy the database section for config.yml from GitHub.")
            evalite.server.pluginManager.disablePlugin(evalite)
            return
        }

        type = if (cfg.getString("type")!!.lowercase() == "mysql") DBMS.MYSQL else DBMS.SQLITE

        userController = if (type == DBMS.SQLITE) SQLiteUserController() else MySQLUserController()
        punishmentController = if (type == DBMS.SQLITE) SQLitePunishmentController() else MySQLPunishmentController()

        if (type == DBMS.MYSQL) {
            try {
                sqlConnection = Iron(
                    "jdbc:mysql://${cfg.getString("mysql.ip")}:${cfg.getInt("mysql.port")}/${cfg.getString("mysql.database")}?user=${cfg.getString("mysql.username")}&password=${cfg.getString("mysql.password")}",
                    IronSettings(
                        driver = DBMS.MYSQL,
                        serialization = SerializationAdapter.Gson(Gson())
                    )
                ).connect()

                evalite.logger.info("Established connection to the MySQL database")
            } catch (e: Exception) {
                evalite.logger.severe("Failed to connect to the MySQL database.")
                e.printStackTrace()
                evalite.server.pluginManager.disablePlugin(evalite)
            }
        } else {
            try {
                sqlConnection = Iron("jdbc:sqlite:${evalite.dataFolder.absolutePath}\\${cfg.getString("file.file-name")}",
                    IronSettings(
                        driver = DBMS.MYSQL,
                        serialization = SerializationAdapter.Gson(Gson())
                    )).connect()
                evalite.logger.info("Established connection to the SQLite database")
            } catch (e: Exception) {
                evalite.logger.severe("Failed to connect to the SQLite database.")
                e.printStackTrace()
                evalite.server.pluginManager.disablePlugin(evalite)
            }
        }

        userController.createTable()
        punishmentController.createTable()

    }

    fun getAutoIncrementKeyword(): String {
        if (evalite.config.getString("database.type")!!.lowercase() == "file") {
            return "AUTOINCREMENT"
        }
        return "AUTO_INCREMENT"

    }
}
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File

class System : JavaPlugin(), Listener {

    private lateinit var userDataConfigFile: File
    private lateinit var userDataConfig: YamlConfiguration

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        createUserDataConfig()
    }

    override fun onDisable() {
        for (player in server.onlinePlayers) {
            savePlayerData(player)
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        loadPlayerData(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        savePlayerData(player)
    }

    private fun createUserDataConfig() {
        userDataConfigFile = File(dataFolder, "UserData.yml")
        if (!userDataConfigFile.exists()) {
            userDataConfigFile.parentFile.mkdirs()
            saveResource("UserData.yml", false)
        }
        userDataConfig = YamlConfiguration.loadConfiguration(userDataConfigFile)
    }

    private fun saveUserDataConfig() {
        try {
            userDataConfig.save(userDataConfigFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun savePlayerData(player: Player) {
        userDataConfig.set("${player.name}.money", 1)
        userDataConfig.set("${player.name}.level", 1)
        saveUserDataConfig()
    }

    private fun loadPlayerData(player: Player) {
        val money = userDataConfig.getInt("${player.name}.money", 0)
        val level = userDataConfig.getInt("${player.name}.level", 0)
    }
}

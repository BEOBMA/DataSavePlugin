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
        saveAllPlayerData()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        loadPlayerData(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        savePlayerData(event.player)
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
        userDataConfig.set("${player.uniqueId}.money", 1)
        userDataConfig.set("${player.uniqueId}.level", 1)
    }

    private fun saveAllPlayerData() {
        server.onlinePlayers.forEach { savePlayerData(it) }
        saveUserDataConfig()
    }

    private fun loadPlayerData(player: Player) {
        val money = userDataConfig.getInt("${player.uniqueId}.money", 0)
        val level = userDataConfig.getInt("${player.uniqueId}.level", 0)
    }
}

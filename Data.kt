object QuestManager {
    private val folder: File = File(Main.instance.dataFolder, "/Quests")
    private var questList = mutableListOf<Quest>()

    fun create(quest: Quest) {
        val file = File(folder, "${quest.name}.yml")
        val config = YamlConfiguration.loadConfiguration(file)

        config["name"] = quest.name
        config["description"] = quest.description
        config["reward"] = quest.reward
        config["isClear"] = quest.isClear
        questList.add(quest)

        config.save(file)
    }

    fun save(quest: Quest) {
        val file = File(folder, "${quest.name}.yml")
        val config = YamlConfiguration.loadConfiguration(file)

        config["name"] = quest.name
        config["description"] = quest.description
        config["reward"] = quest.reward
        config["isClear"] = quest.isClear

        config.save(file)
    }

    fun load() {
        questList.clear()
        val files = folder.listFiles() ?: return

        for (file in files) {
            val config = YamlConfiguration.loadConfiguration(file)
            val description = config.getStringList("description")
            val reward = config.getList("reward")!!.map {
                it as? ItemStack ?: throw IllegalStateException("Invalid item stack in reward list")
            }
            val isClear = config.getBoolean("isClear")

            val quest = Quest(file.name, description, reward, isClear)
            questList.add(quest)
        }
    }

    fun getQuestById(name: String): Quest? {
        return questList.find { it.name == name }
    }

    fun getAllQuests(): MutableList<Quest> {
        return questList
    }
}

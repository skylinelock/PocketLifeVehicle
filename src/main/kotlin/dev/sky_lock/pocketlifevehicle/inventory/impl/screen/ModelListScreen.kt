package dev.sky_lock.pocketlifevehicle.inventory.impl.screen
/**
 * @author sky_lock
 */

/*
class ModelListScreen(parent: ModelEditUI) : Screen(parent, null) {

    private val modelAdd = ItemStackBuilder(Material.CHEST_MINECART, 1).setName(ChatColor.GREEN + "モデルを追加する").build()
    private val right = ItemStackBuilder(Material.IRON_NUGGET, 1).setCustomModelData(16).setName(ChatColor.AQUA + "次のページへ進む").build()
    private val left = ItemStackBuilder(Material.IRON_NUGGET, 1).setCustomModelData(17).setName(ChatColor.AQUA + "前のページへ戻る").build()
    private val settingScreen = ModelSettingScreen(parent, this)
    private var page = 0

    init {
        setSlots()
    }

    // flip page (0=back, 1=forward)
    fun flip(order: Int) {
        if (order == 0) page++ else page--
        setSlots()
    }

    private fun setSlots() {
        val firstIndex = 54 * page
        val lastIndex = 54 * (page + 1) - 1
        var vehicleIndex = 0
        ModelRegistry.forEach { model ->
            if (lastIndex < vehicleIndex || firstIndex > vehicleIndex) {
                vehicleIndex++
                return@forEach
            }
            addSlot(vehicleIndex % 53, prepareItemStackForModel(model)) {
    */
/*            EditSessions.of(player.uniqueId).ifPresent { session: ModelOption ->
                    session.isJustEditing = true
                    session.id = model.id
                    session.name = model.name
                    session.lore = model.lore
                    session.maxFuel = spec.maxFuel
                    session.maxSpeed = spec.maxSpeed
                    session.capacity = spec.capacity
                    session.itemType = itemOption.type
                    session.setItemID(itemOption.id)
                    session.setItemPosition(itemOption.position)
                    session.collideBaseSide = model.collideBox.baseSide
                    session.collideHeight = model.collideBox.height
                    session.isBig = model.isBig
                    session.height = model.height
                    session.sound = model.sound
                    menu.flip(player, ModelMenuIndex.SETTING.ordinal)
                }*//*

            }
            vehicleIndex++
        }
        if (page > 0) {
            addSlot(48, left) {
                flip(0)
            }
        }
        addSlot(49, modelAdd) {
            switch(settingScreen)
        }
        addSlot(50, right) {
            flip(1)
        }
    }

    private fun prepareItemStackForModel(model: Model): ItemStack {
        val desc: MutableList<String> = ArrayList()
        desc.add(ChatColor.DARK_AQUA + "名前: " + ChatColor.AQUA + model.name)
        //TODO: [] -> ""
        var lore: List<String?> = model.lore
        if (lore.isEmpty()) {
            lore = listOf("")
        }
        desc.add(ChatColor.DARK_AQUA + "説明: " + ChatColor.AQUA + lore)
        val spec = model.spec
        desc.add(ChatColor.DARK_AQUA + "燃料上限: " + ChatColor.AQUA + spec.maxFuel)
        desc.add(ChatColor.DARK_AQUA + "最高速度: " + ChatColor.AQUA + spec.maxSpeed.label)
        desc.add(ChatColor.DARK_AQUA + "乗車人数: " + ChatColor.AQUA + spec.capacity.value())
        val itemOption = model.itemOption
        val box = model.collideBox
        desc.add(ChatColor.DARK_AQUA + "モデル位置: " + ChatColor.AQUA + itemOption.position.label)
        desc.add(ChatColor.DARK_AQUA + "当たり判定(高さ): " + ChatColor.AQUA + box.height)
        desc.add(ChatColor.DARK_AQUA + "当たり判定(底辺): " + ChatColor.AQUA + box.baseSide)
        val size = if (model.isBig) "大きい" else "小さい"
        desc.add(ChatColor.DARK_AQUA + "大きさ: " + ChatColor.AQUA + size)
        desc.add(ChatColor.DARK_AQUA + "座高: " + ChatColor.AQUA + model.height)
        return ItemStackBuilder(model.itemStack).setName(ChatColor.YELLOW + model.id).setLore(desc).build()
    }
}*/

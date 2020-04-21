package dev.sky_lock.menu

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*
import java.util.function.Consumer

/**
 * InventoryMenuクラスは、InventoryをGUIとして扱うための基本的な操作（インベントリ作成、アイテム配置、ページング）を提供する抽象クラスです。
 *
 * @author sky_lock
 */
abstract class InventoryMenu(title: String,
        /**
         * このInventoryMenuインスタンスのTypeを取得します。
         * ここでのTypeはBukkitのInventoryTypeとは別のものです。
         *
         * @return インベントリのタイプ
         */
                             val type: Type, player: Player) : InventoryHolder {
    /**
     * このインベントリが最初に開かれた時のプレイヤーを取得します。
     *
     * @return プレイヤーのUniqueID
     */
    val holder: UUID = player.uniqueId

    private val inventory: Inventory
    private val contents: MutableList<MenuContents>
    private val openingPlayers: MutableSet<UUID>

    /**
     * 現在開かれているページを返します。
     *
     * @return ページ
     */
    private var page: Int
        private set

    /**
     * メニューコンテンツを追加します。
     *
     * @param contents 追加するメニューコンテンツ
     */
    fun addContents(vararg contents: MenuContents) {
        Collections.addAll(this.contents, *contents)
    }

    /**
     * 指定されたページに登録されているメニューコンテンツを指定したプレイヤーに表示します。
     * 登録されているメニューコンテンツが存在しなかった場合、何もしません。
     *
     * @param player プレイヤー
     */
    fun open(player: Player, page: Int) {
        if (contents.isEmpty()) {
            return
        }
        openingPlayers.add(player.uniqueId)
        flip(player, page)
        player.openInventory(inventory)
    }

    /**
     * 現在設定されているページのメニューコンテンツを指定したプレイヤーに表示します。
     * 登録されているメニューコンテンツが存在しなかった場合、何もしません。
     *
     * @param player プレイヤー
     */
    fun flip(player: Player?, page: Int) {
        if (contents.isEmpty()) {
            return
        }
        this.page = page
        openingContents.ifPresent { content: MenuContents ->
            update()
            content.onFlip(this)
        }
    }

    /**
     * Inventoryの内容を更新します。
     */
    fun update() {
        openingContents.ifPresent { content: MenuContents ->
            inventory.clear()
            content.getSlots().forEach(Consumer { slot: Slot -> inventory.setItem(slot.index(), slot.getItemStack()) })
        }
    }

    /**
     * このインベントリを開いているPlayerのUUIDのSetを返します。
     *
     * @return プレイヤーUUIDのSet
     */
    fun getOpeningPlayers(): Set<UUID> {
        return openingPlayers
    }

    /**
     * 指定したプレイヤーがこのメニューを開いているかどうかを返します。
     *
     * @param player プレイヤー
     * @return このInventoryMenuを開いているかどうか
     */
    fun isOpening(player: Player): Boolean {
        return openingPlayers.contains(player.uniqueId)
    }

    /**
     * 指定したプレイヤーが開いてるメニューを閉じます。プレイヤーがメニューを開いていない場合は何もしません。
     */
    fun close(player: Player) {
        if (openingPlayers.isEmpty() || !openingPlayers.contains(player.uniqueId)) {
            return
        }
        player.closeInventory()
        openingPlayers.remove(player.uniqueId)
    }

    fun remove(player: Player) {
        openingPlayers.remove(player.uniqueId)
    }

    /**
     * このメソッドが呼び出された時点で開いているメニューコンテンツを取得します。
     *
     * @return メニューコンテンツ
     */
    val openingContents: Optional<MenuContents>
        get() = getContentsAt(page)

    /**
     * 指定したページのメニューコンテンツを取得します。
     *
     * @exception IndexOutOfBoundsException 最大ページ数を超えたページを指定した場合
     * @param page ページ
     * @return メニューコンテンツ
     */
    fun getContentsAt(page: Int): Optional<MenuContents> {
        return Optional.ofNullable(contents[page])
    }

    fun click(event: InventoryClickEvent) {
        openingContents.ifPresent { menuContents: MenuContents -> menuContents.click(event) }
    }

    /**
     * このInventoryMenuインスタンスに関連付けられたInventoryを返します。
     *
     * @return インベントリ
     */
    override fun getInventory(): Inventory {
        return inventory
    }

    enum class Type(private val slotSize: Int) {
        BIG(54), SMALL(27);

        fun size(): Int {
            return slotSize
        }

    }

    companion object {
        /**
         * 指定したプレイヤーがInventoryMenuを開いていた場合、そのプレイヤーに関連付けられたInventoryMenuを取得します。
         * プレイヤーがInventoryMenuを開いていなかった場合は空のOptionalが返されます。
         *
         * @param player プレイヤー
         * @return OptionalなInventoryMenu
         */
        @JvmStatic
        fun of(player: Player): Optional<InventoryMenu> {
            val holder = player.openInventory.topInventory.holder
            return if (holder is InventoryMenu) {
                Optional.of(holder as InventoryMenu)
            } else {
                Optional.empty()
            }
        }
    }

    /**
     * 新しいInventoryMenuインスタンスを作成します。
     *
     * @param title InventoryMenuのタイトル
     * @param type Inventoryの大きさや形状などを決めるType
     * @param player Inventoryを最初に開いたプレイヤー
     */
    init {
        inventory = Bukkit.createInventory(this, type.size(), title)
        contents = ArrayList()
        openingPlayers = HashSet()
        page = 1
    }
}
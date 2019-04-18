package dev.sky_lock.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.*;

/**
 * InventoryMenuクラスは、InventoryをGUIとして扱うための基本的な操作（インベントリ作成、アイテム配置、ページング）を提供する抽象クラスです。
 *
 * @author sky_lock
 */

public abstract class InventoryMenu implements InventoryHolder {
    private final UUID holder;
    private final Type type;
    private final Inventory inventory;
    private List<MenuContents> contents;
    private Set<UUID> openingPlayers;
    private int page;

    /**
     * 新しいInventoryMenuインスタンスを作成します。
     *
     * @param title InventoryMenuのタイトル
     * @param type Inventoryの大きさや形状などを決めるType
     * @param player Inventoryを最初に開いたプレイヤー
     */
    public InventoryMenu(String title, Type type, Player player) {
        this.holder = player.getUniqueId();
        this.type = type;
        this.inventory = Bukkit.createInventory(this, type.size(), title);
        this.contents = new ArrayList<>();
        this.openingPlayers = new HashSet<>();
        this.page = 1;
    }

    /**
     * 指定したプレイヤーがInventoryMenuを開いていた場合、そのプレイヤーに関連付けられたInventoryMenuを取得します。
     * プレイヤーがInventoryMenuを開いていなかった場合は空のOptionalが返されます。
     *
     * @param player プレイヤー
     * @return OptionalなInventoryMenu
     */
    public static Optional<InventoryMenu> of(Player player) {
        InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (!(holder instanceof InventoryMenu)) {
            return Optional.empty();
        }
        return Optional.of((InventoryMenu) holder);
    }

    /**
     * このインベントリが最初に開かれた時のプレイヤーを取得します。
     *
     * @return プレイヤーのUniqueID
     */
    public UUID getHolder() {
        return holder;
    }

    /**
     * メニューコンテンツを追加します。
     *
     * @param contents 追加するメニューコンテンツ
     */
    public void addContents(MenuContents... contents) {
        Collections.addAll(this.contents, contents);
    }


    /**
     * 指定されたページに登録されているメニューコンテンツを指定したプレイヤーに表示します。
     * 登録されているメニューコンテンツが存在しなかった場合、何もしません。
     *
     * @param player プレイヤー
     */
    public void open(Player player, int page) {
        if (this.contents.isEmpty()) {
            return;
        }
        openingPlayers.add(player.getUniqueId());
        flip(player, page);
        player.openInventory(inventory);
    }

    /**
     * 現在設定されているページのメニューコンテンツを指定したプレイヤーに表示します。
     * 登録されているメニューコンテンツが存在しなかった場合、何もしません。
     *
     * @param player プレイヤー
     */
    public void flip(Player player, int page) {
        if (this.contents.isEmpty()) {
            return;
        }
        this.page = page;
        getOpeningContents().ifPresent(content -> {
            update();
            content.onFlip(this);
        });
    }

    /**
     * Inventoryの内容を更新します。
     */
    public void update() {
        getOpeningContents().ifPresent(content -> {
            inventory.clear();
            content.getSlots().forEach(slot -> inventory.setItem(slot.index(), slot.getItemStack()));
        });
    }

    /**
     * このインベントリを開いているPlayerのUUIDのSetを返します。
     *
     * @return プレイヤーUUIDのSet
     */
    public Set<UUID> getOpeningPlayers() {
        return openingPlayers;
    }

    /**
     * 指定したプレイヤーがこのメニューを開いているかどうかを返します。
     *
     * @param player プレイヤー
     * @return このInventoryMenuを開いているかどうか
     */
    public boolean isOpening(Player player) {
        return openingPlayers.contains(player.getUniqueId());
    }

    /**
     * 指定したプレイヤーが開いてるメニューを閉じます。プレイヤーがメニューを開いていない場合は何もしません。
     */
    public void close(Player player) {
        if (openingPlayers.isEmpty() || !openingPlayers.contains(player.getUniqueId())) {
            return;
        }
        player.closeInventory();
        openingPlayers.remove(player.getUniqueId());
    }

    public void remove(Player player) {
        openingPlayers.remove(player.getUniqueId());
    }

    /**
     * このメソッドが呼び出された時点で開いているメニューコンテンツを取得します。
     *
     * @return メニューコンテンツ
     */
    public Optional<MenuContents> getOpeningContents() {
        return getContentsAt(page);
    }

    /**
     * 指定したページのメニューコンテンツを取得します。
     *
     * @exception IndexOutOfBoundsException 最大ページ数を超えたページを指定した場合
     * @param page ページ
     * @return メニューコンテンツ
     */
    public Optional<MenuContents> getContentsAt(int page) {
        return Optional.ofNullable(this.contents.get(page - 1));
    }

    void click(InventoryClickEvent event) {
        getOpeningContents().ifPresent(menuContents -> menuContents.click(event));
    }

    /**
     * 現在開かれているページを返します。
     *
     * @return ページ
     */
    public int getPage() {
        return page;
    }

    /**
     * このInventoryMenuインスタンスに関連付けられたInventoryを返します。
     *
     * @return インベントリ
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * このInventoryMenuインスタンスのTypeを取得します。
     * ここでのTypeはBukkitのInventoryTypeとは別のものです。
     *
     * @return インベントリのタイプ
     */
    public Type getType() {
        return type;
    }

    public enum Type {
        BIG(54),
        SMALL(27);

        private final int slotSize;

        Type(int slotSize) {
            this.slotSize = slotSize;
        }

        int size() {
            return slotSize;
        }
    }
}

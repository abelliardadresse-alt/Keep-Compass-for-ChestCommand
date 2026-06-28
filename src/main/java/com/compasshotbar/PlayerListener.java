package com.compasshotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener implements Listener {

    private final CompassHotbar plugin;
    private static final int COMPASS_SLOT = 8;

    public PlayerListener(CompassHotbar plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.isPluginEnabled()) {
            plugin.giveCompass(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (plugin.isPluginEnabled()) {
            plugin.giveCompass(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!plugin.isPluginEnabled()) return;

        PlayerInventory inventory = player.getInventory();
        ItemStack compass = inventory.getItem(COMPASS_SLOT);

        if (compass != null && compass.getType() == Material.COMPASS) {
            event.getDrops().remove(compass);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isPluginEnabled()) return;
        if (!player.hasPermission("compasshotbar.use")) return;

        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (droppedItem.getType() == Material.COMPASS) {
            event.setCancelled(true);
            player.sendMessage("§6[CompassHotbar] §cVous ne pouvez pas lâcher votre boussole!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!plugin.isPluginEnabled()) return;
        if (!player.hasPermission("compasshotbar.use")) return;

        PlayerInventory playerInventory = player.getInventory();
        ItemStack compassInSlot = playerInventory.getItem(COMPASS_SLOT);
        boolean hasCompassInSlot = compassInSlot != null && compassInSlot.getType() == Material.COMPASS;

        // Check if the click is on the compass slot (raw slot 8 or converted slot 8 for bottom inventory)
        boolean isCompassSlotClick = event.getSlot() == COMPASS_SLOT && 
            event.getClickedInventory() != null && 
            event.getClickedInventory().getType() == InventoryType.PLAYER;

        // Block ALL interactions with the compass slot when it contains a compass
        if (isCompassSlotClick && hasCompassInSlot) {
            event.setCancelled(true);
            
            // If player had something on cursor, drop it
            ItemStack cursorItem = event.getCursor();
            if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(player.getLocation(), cursorItem);
                player.setItemOnCursor(new ItemStack(Material.AIR));
            }
            return;
        }

        // Block shift-clicking a compass OUT of the player's inventory
        if (event.isShiftClick() && event.getClickedInventory() != null &&
            event.getClickedInventory().getType() == InventoryType.PLAYER) {
            
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.COMPASS && 
                event.getSlot() == COMPASS_SLOT) {
                event.setCancelled(true);
                return;
            }
        }

        // Block moving compass WITH cursor (drag to hotbar)
        if (event.getCursor() != null && event.getCursor().getType() == Material.COMPASS) {
            // If clicking anywhere in hotbar (slots 0-8) with compass on cursor
            if (event.getSlot() < 9 && event.getClickedInventory().getType() == InventoryType.PLAYER) {
                event.setCancelled(true);
                player.setItemOnCursor(new ItemStack(Material.AIR));
                playerInventory.setItem(COMPASS_SLOT, new ItemStack(Material.COMPASS));
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!plugin.isPluginEnabled()) return;
        if (!player.hasPermission("compasshotbar.use")) return;

        PlayerInventory playerInventory = player.getInventory();
        ItemStack compassInSlot = playerInventory.getItem(COMPASS_SLOT);
        boolean hasCompassInSlot = compassInSlot != null && compassInSlot.getType() == Material.COMPASS;

        // Block any drag operation that affects the compass slot
        for (int slot : event.getRawSlots()) {
            // Raw slot 8 is the compass hotbar slot, 44 is the offhand slot
            // Slots 0-8 are hotbar slots in the player inventory
            if (slot == COMPASS_SLOT || slot == COMPASS_SLOT + 36) {
                if (hasCompassInSlot) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // Also block if compass is being dragged onto the compass slot
        if (hasCompassInSlot) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isPluginEnabled()) return;
        if (!player.hasPermission("compasshotbar.use")) return;

        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.COMPASS) {
            if (event.getAction().name().contains("RIGHT")) {
                plugin.ensureCompassInSlot(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isPluginEnabled()) return;
        if (!player.hasPermission("compasshotbar.use")) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item != null && item.getType() == Material.COMPASS) {
            plugin.ensureCompassInSlot(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!plugin.isPluginEnabled()) return;
        if (!player.hasPermission("compasshotbar.use")) return;

        plugin.ensureCompassInSlot(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerLoad(ServerLoadEvent event) {
        if (plugin.isPluginEnabled()) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.giveCompass(player);
            }
        }
    }
}
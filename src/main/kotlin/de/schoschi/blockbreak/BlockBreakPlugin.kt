package de.schoschi.blockbreak

import com.google.common.io.Resources
import net.axay.kspigot.extensions.bukkit.register
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.pluginManager
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.runnables.async
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.structures.fillBlocks
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlockBreakPlugin : KSpigot(), Listener {

    override fun startup() {

        pluginManager.registerEvents(this, this)
    }

    override fun shutdown() {

    }

    fun updateChunk(player: Player, chunk: Chunk, material: Material, item: ItemStack) {
        sync {
            val area = LocationArea(
                Location(chunk.world, (chunk.x * 16).toDouble(), -64.toDouble(), (chunk.z * 16).toDouble()),
                Location(chunk.world, (chunk.x * 16 + 15).toDouble(), 320.toDouble(), (chunk.z * 16 + 15).toDouble())
            )
            area.fillBlocks.forEach {
                if (it.type == material) {
                    it.getDrops(item).forEach {
                        player.inventory.addItem(it)
                    }
                    it.type = Material.AIR
                }
            }
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        updateChunk(event.player, event.block.chunk, event.block.type, event.player.itemInHand)
    }
}

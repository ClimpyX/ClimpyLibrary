package com.climpy.tnt;

import com.climpy.ClimpyLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;



public class AnimatedTNT
        implements Listener
{
    public AnimatedTNT(ClimpyLibrary plugin) {}

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
            if (ClimpyLibrary.getInstance().getConfig().getBoolean("tnt.enable"))
        {
            if (!ClimpyLibrary.worlds.contains(event.getLocation().getWorld().getName()))
            {
                for (Block blocks : event.blockList()) {

                    float x = (float)(-1.0D + ClimpyLibrary.getInstance().getConfig().getInt("tnt.x") * Math.random());
                    float y = (float)(-2.0D + ClimpyLibrary.getInstance().getConfig().getInt("tnt.y") * Math.random());
                    float z = (float)(-1.0D + ClimpyLibrary.getInstance().getConfig().getInt("tnt.z") * Math.random());

                    final FallingBlock fallingblock = blocks.getWorld().spawnFallingBlock(blocks.getLocation(), blocks.getType(), blocks.getData());
                    fallingblock.setDropItem(false);
                    fallingblock.setVelocity(new Vector(x, y, z));

                    Bukkit.getScheduler().runTaskLater(ClimpyLibrary.getInstance(), new Runnable()
                    {
                        public void run()
                        {
                            if (!ClimpyLibrary.getInstance().getConfig().getBoolean("tnt.solid-block"))
                            {
                                fallingblock.getLocation().getBlock().setType(Material.AIR);
                            }
                        }
                    },  100L);

                    blocks.setType(Material.AIR);
                }
            }
        }
    }
}

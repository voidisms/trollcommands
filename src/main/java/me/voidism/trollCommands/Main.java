package me.voidism.trollCommands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin implements Listener {
    Set<String> invertedControls  = new HashSet<>();
    Set<String> loopKilledPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("TROLLCOMMANDS v0.1: to get started use /trollcmds to see a list of commands");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.isOp() || sender instanceof ConsoleCommandSender) {
            if (sender instanceof Player p) {
                if (label.equalsIgnoreCase("freeze") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.setWalkSpeed(0);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999999, -240));
                        sendMessage(sender, "Player has been frozen.");
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("unfreeze") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.setWalkSpeed(0.2F);
                        target.removePotionEffect(PotionEffectType.JUMP_BOOST);
                        sendMessage(sender, "Player has been unfrozen.");
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("spawncreeper") && args.length == 2) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Location loc = target.getLocation();
                        World w = target.getWorld();
                        try {
                            int amount = Integer.parseInt(args[1]);
                            for (int i = 0; i < amount; i++) {
                                w.spawnEntity(loc, EntityType.CREEPER);
                            }
                            sendMessage(sender, "Spawned " + amount + " creepers on " + target.getName());
                            return true;
                        } catch (NumberFormatException e) {
                            sendMessage(sender, "Please enter a valid number of creepers to spawn.");
                        }
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("loopkill") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        loopKilledPlayers.add(target.getName());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (target.isOnline() && loopKilledPlayers.contains(target.getName())) {
                                    target.setHealth(0.0D);
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(this, 0L, 20L);
                        sendMessage(sender, "Started loopkill on " + target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("unloopkill") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline() && loopKilledPlayers.contains(target.getName())) {
                        loopKilledPlayers.remove(target.getName());
                        sendMessage(sender, "Stopped loopkill on " + target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player/player is not being loopkilled");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("nuke") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Location loc = target.getLocation();
                        World w = target.getWorld();
                        w.createExplosion(loc, 15, true, true);
                        sendMessage(sender, "Nuked " + target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("launch") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.setVelocity(target.getLocation().getDirection().multiply(2).setY(3));
                        sendMessage(sender, "Launched " + target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("cage") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Location loc = target.getLocation();
                        World world = target.getWorld();
                        for (int x = -1; x <= 1; x++) {
                            for (int y = 0; y <= 2; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    if (!(x == 0 && y == 1 && z == 0)) {
                                        world.getBlockAt(loc.clone().add(x, y, z)).setType(Material.BARRIER);
                                    }
                                }
                            }
                        }
                        sendMessage(sender, "Caged " + target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                if (label.equalsIgnoreCase("uncage") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Location loc = target.getLocation();
                        World world = target.getWorld();
                        for (int x = -1; x <= 1; x++) {
                            for (int y = 0; y <= 2; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    Location blockLoc = loc.clone().add(x, y, z);
                                    if (world.getBlockAt(blockLoc).getType() == Material.BARRIER) {
                                        world.getBlockAt(blockLoc).setType(Material.AIR);
                                    }
                                }
                            }
                        }
                        sendMessage(sender, "Uncaged " + target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }
                else if (label.equalsIgnoreCase("supernuke") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Location loc = target.getLocation();
                        World w = target.getWorld();
                        w.createExplosion(loc, 35, true, true);
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }
                else if (label.equalsIgnoreCase("ultranuke") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Location loc = target.getLocation();
                        World w = target.getWorld();
                        w.createExplosion(loc, 70, true, true);
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }

                else if (label.equalsIgnoreCase("starve") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.setFoodLevel(0);
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }
                else if (label.equalsIgnoreCase("floodchat") && args.length == 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {;
                        try {
                            int amount = Integer.parseInt(args[1]);
                            for (int i = 0; i < amount; i++) {
                              target.sendMessage(args[2]);
                            }
                            return true;
                        } catch (NumberFormatException e) {
                            sendMessage(sender, "Please enter a valid number");
                        }
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }
                else if (label.equalsIgnoreCase("invertcontrols") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        invertedControls.add(target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }
                else if (label.equalsIgnoreCase("revertcontrols") && args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline() && invertedControls.contains(target.getName())) {
                        invertedControls.remove(target.getName());
                        return true;
                    } else {
                        sendMessage(sender, "Unable to find player");
                        return true;
                    }
                }


            }



        }

        


        return false;
    }
    private void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player p) {
            p.sendMessage(ChatColor.RED + message);
        } else {
            getLogger().info(message);
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (invertedControls.contains(player.getName())) {
            double newX = event.getTo().getX();
            double newZ = event.getTo().getZ();

            double deltaX = newX - event.getFrom().getX();
            double deltaZ = newZ - event.getFrom().getZ();

            if (deltaX != 0) {
                newX = event.getTo().getX() - (deltaX * 2);
            }
            if (deltaZ != 0) {
                newZ = event.getTo().getZ() - (deltaZ * 2);
            }

            Location newLocation = new Location(event.getTo().getWorld(), newX, event.getTo().getY(), newZ);


            event.setTo(newLocation);
        }
    }


}

package com.tenko.functions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.material.Stairs;

import com.tenko.FriendlyWall;
import com.tenko.objs.ChairWatcher;

public class Chairs extends Function {

	private static Class<?> Packet40, CraftPlayer;

	public Chairs(){
		try {
			Packet40 = Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".Packet40EntityMetadata");
			CraftPlayer = Class.forName("org.bukkit.craftbukkit." + FriendlyWall.getVersion() + ".entity.CraftPlayer");
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		//No commands.
		return false;
	}
	
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if(event.getPlayer().isSneaking() && FriendlyWall.getSitters().containsKey(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void walkAway(PlayerMoveEvent e){
		if(FriendlyWall.getSitters().containsKey(e.getPlayer().getName())){
			if(e.getFrom().getY() < e.getTo().getY()){
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
				stopSittingOnMe(e.getPlayer());
				return;
			}
			Location to = FriendlyWall.getSitters().get(e.getPlayer().getName());
			Location from = e.getPlayer().getLocation();

			if(to.getWorld() == from.getWorld()){
				if(from.distance(to) > 1){
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
					stopSittingOnMe(e.getPlayer());
				} else {
					sendSitPacket(e.getPlayer());
				}
			} else {	
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
				stopSittingOnMe(e.getPlayer());
			}
		}
	}


	@EventHandler
	public void Ragequit(PlayerQuitEvent event){
		if(FriendlyWall.getSitters().containsKey(event.getPlayer().getName())){
			stopSittingOnMe(event.getPlayer());
			Location p = event.getPlayer().getLocation().clone();
			p.setY(p.getY() + 1);
			event.getPlayer().teleport(p, PlayerTeleportEvent.TeleportCause.PLUGIN);
		}
	}

	@EventHandler
	public void screwYoChair(BlockBreakEvent event){
		if(!FriendlyWall.getSitters().isEmpty()){
			ArrayList<String> standList = new ArrayList<String>();
			for (String s : FriendlyWall.getSitters().keySet()) {
				if(FriendlyWall.getSitters().get(s).equals(event.getBlock().getLocation())){
					standList.add(s);
				}
			}
			for(String s : standList){
				Bukkit.getPlayer(s).sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
				stopSittingOnMe(Bukkit.getPlayer(s));
			}
			standList.clear();
		}
	}

	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void sitDown(PlayerInteractEvent e){
		if(e.hasBlock() && !FriendlyWall.getSitters().containsKey(e.getPlayer()) && e.getPlayer().isSneaking() && e.getPlayer().getLocation().distance(e.getClickedBlock().getLocation()) < 2D){
			if(e.getClickedBlock().getRelative(BlockFace.DOWN).isLiquid()){
				return;
			}

			if(e.getClickedBlock().getRelative(BlockFace.DOWN).isEmpty()){
				return;
			}

			if(!e.getClickedBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
				return;
			}

			if(e.getClickedBlock().getRelative(BlockFace.DOWN).getType().toString().contains("FENCE")){
				return;
			}

			if(e.getClickedBlock().getType().toString().contains("STAIR")){
				Stairs s = (Stairs)e.getClickedBlock().getState().getData();

				if (s.isInverted()) {
					return;
				}

				try {
					Location nLocation = e.getClickedBlock().getLocation().clone();
					nLocation.add(0.5D, 0.2D, 0.5D);

					switch (s.getDescendingDirection()) {
					case NORTH:
						nLocation.setYaw(180);
						break;
					case EAST:
						nLocation.setYaw(-90);
						break;
					case SOUTH:
						nLocation.setYaw(0);
						break;
					case WEST:
						nLocation.setYaw(90);
					}

					e.getPlayer().teleport(nLocation);
					e.getPlayer().setSneaking(true);
					sendSitPacket(e.getPlayer());
					FriendlyWall.getSitters().put(e.getPlayer().getName(), e.getClickedBlock().getLocation());
					e.setUseInteractedBlock(Result.DENY);
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You have sat.");
					FriendlyWall.getPlugin().getServer().getScheduler().runTaskLater(FriendlyWall.getPlugin(), new Runnable() {
						@Override
						public void run() {
							sendSit();
						}
					}, 10); 
				} catch (Exception e1) {
					e1.printStackTrace();
					e.getPlayer().sendMessage(ChatColor.RED + "Sitting is broken. Tell Tenko that the error was " + e1 + ".");
				}
			} else if(e.getClickedBlock().getType() == Material.BED_BLOCK){
				try {
					Location nLocation = e.getClickedBlock().getLocation().clone();
					nLocation.add(0.5D, 0.3D, 0.5D);

					e.getPlayer().teleport(nLocation);
					e.getPlayer().setSneaking(true);
					sendSitPacket(e.getPlayer());
					FriendlyWall.getSitters().put(e.getPlayer().getName(), e.getClickedBlock().getLocation());
					e.setUseInteractedBlock(Result.DENY);
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You have sat.");
					FriendlyWall.getPlugin().getServer().getScheduler().runTaskLater(FriendlyWall.getPlugin(), new Runnable() {
						@Override
						public void run() {
							sendSit();
						}
					}, 10); 

					e.setCancelled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
					e.getPlayer().sendMessage(ChatColor.RED + "Sitting is broken. Tell Tenko that the error was " + e1 + ".");
				}
			}
		}
	}

	private void sendPacket(Player p, byte b){
		Object packet;
		try {
			packet = Packet40.getConstructor(int.class, Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".DataWatcher"), boolean.class).newInstance(p.getEntityId(), new ChairWatcher(b), false);

			for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
				if(onlinePlayer.canSee(p) || onlinePlayer == p) {
					if(onlinePlayer.getWorld().equals(p.getWorld())) {
						try {
							Object craft = CraftPlayer.cast(onlinePlayer);
							Object hand = CraftPlayer.getMethod("getHandle").invoke(craft, (Object[])null);
							Object con = hand.getClass().getField("playerConnection").get(hand);
							con.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".Packet")).invoke(con, packet);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	private void sendSitPacket(Player p){
		sendPacket(p, (byte)4);
	}

	private void sendStandPacket(Player p){
		sendPacket(p, (byte)0);
	}

	private void sendSit(){
		for (String s : FriendlyWall.getSitters().keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p != null){
				sendSitPacket(p);
			}
		}
	}

	private void stopSittingOnMe(Player p){
		if (FriendlyWall.getSitters().containsKey(p.getName())) {
			FriendlyWall.getSitters().remove(p.getName());
		}

		sendStandPacket(p);
	}

	//Aka, lazy copy/paste code.
	public static void onDisabledStopSitting(Player p){
		try {
			Object packet = Packet40.getConstructor(int.class, Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".DataWatcher"), boolean.class).newInstance(p.getEntityId(), new ChairWatcher((byte) 0), false);

			for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
				try {
					Object craft = CraftPlayer.cast(onlinePlayer);
					Object hand = CraftPlayer.getMethod("getHandle").invoke(craft, (Object[])null);
					Object con = hand.getClass().getField("playerConnection").get(hand);
					con.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".Packet")).invoke(con, packet);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException e){
			e.printStackTrace();
		}
	}
}

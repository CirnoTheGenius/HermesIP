package com.tenko.functions;

import java.util.ArrayList;
import java.util.HashMap;

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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;

import com.tenko.FriendlyWall;
import com.tenko.asm.ChairClassBuilder;
import com.tenko.nms.NMSLib;
import com.tenko.nms.NMSPacket;

public class Chairs extends Function {

	private static HashMap<String, Location> sitters = new HashMap<String, Location>();
	private static ChairClassBuilder ccb = new ChairClassBuilder();

	public Chairs(){
		super();
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		//No commands.
		return false;
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if(event.getPlayer().isSneaking() && sitters.containsKey(event.getPlayer().getName())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void walkAway(PlayerMoveEvent e){
		if(sitters.containsKey(e.getPlayer().getName())){
			if(e.getFrom().getY() < e.getTo().getY()){
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
				sitters.remove(e.getPlayer().getName());
				stopSittingOnMe(e.getPlayer());
				return;
			}
			Location to = sitters.get(e.getPlayer().getName());
			Location from = e.getPlayer().getLocation();

			if(to.getWorld() == from.getWorld()){
				if(from.distance(to) > 1){
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
					sitters.remove(e.getPlayer().getName());
					stopSittingOnMe(e.getPlayer());
				} else {
					sendSitPacket(e.getPlayer());
				}
			} else {	
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
				sitters.remove(e.getPlayer().getName());
				stopSittingOnMe(e.getPlayer());
			}
		}
	}


	@EventHandler
	public void Ragequit(PlayerQuitEvent event){
		if(sitters.containsKey(event.getPlayer().getName())){
			stopSittingOnMe(event.getPlayer());
			Location p = event.getPlayer().getLocation().clone();
			p.setY(p.getY() + 1);
			event.getPlayer().teleport(p);
			sitters.remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void screwYoChair(BlockBreakEvent event){
		if(!sitters.isEmpty()){
			ArrayList<String> standList = new ArrayList<String>();
			for (String s : sitters.keySet()) {
				if(sitters.get(s).equals(event.getBlock().getLocation())){
					standList.add(s);
				}
			}
			for(String s : standList){
				Bukkit.getPlayer(s).sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
				sitters.remove(s);
				stopSittingOnMe(Bukkit.getPlayer(s));
			}
			standList.clear();
		}
	}

	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void sitDown(PlayerInteractEvent e){
		if(e.hasBlock() && !sitters.containsKey(e.getPlayer().getName()) && e.getPlayer().isSneaking() && e.getPlayer().getLocation().distance(e.getClickedBlock().getLocation()) < 2D){
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
					sitters.put(e.getPlayer().getName(), e.getClickedBlock().getLocation());
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
			} else if(e.getClickedBlock().getType().toString().contains("STEP")){
				if(e.getClickedBlock().getType().toString().contains("WOOD")){
					WoodenStep ws = (WoodenStep)e.getClickedBlock().getState().getData();
					if(ws.isInverted()){
						return;
					}
				} else {
					Step s = (Step)e.getClickedBlock().getState().getData();
					if(s.isInverted()){
						return;
					}
				}
				Location nLocation = e.getClickedBlock().getLocation().clone();
				nLocation.add(0.5D, 0.2D, 0.5D);
				e.getPlayer().teleport(nLocation);
				e.getPlayer().setSneaking(true);
				sendSitPacket(e.getPlayer());
				sitters.put(e.getPlayer().getName(), e.getClickedBlock().getLocation());
				e.setUseInteractedBlock(Result.DENY);
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have sat.");
				FriendlyWall.getPlugin().getServer().getScheduler().runTaskLater(FriendlyWall.getPlugin(), new Runnable() {
					@Override
					public void run() {
						sendSit();
					}
				}, 10);
			} else if(e.getClickedBlock().getType() == Material.BED_BLOCK){
				try {
					Location nLocation = e.getClickedBlock().getLocation().clone();
					nLocation.add(0.5D, 0.3D, 0.5D);

					e.getPlayer().teleport(nLocation);
					e.getPlayer().setSneaking(true);
					sendSitPacket(e.getPlayer());
					sitters.put(e.getPlayer().getName(), e.getClickedBlock().getLocation());
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
		NMSPacket packet = new NMSPacket("Packet40EntityMetadata");
		packet.setField("a", p.getEntityId());
		packet.setDeclaredField("b", ccb.getListFromWatcher(ccb.newWatcherInstance(b)));

		NMSLib.sendPacket(p, packet.seal());
	}

	private void sendSitPacket(Player p){
		sendPacket(p, (byte)4);
	}

	private void sendStandPacket(Player p){
		sendPacket(p, (byte)0);
	}

	private void sendSit(){
		for (String s : sitters.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p != null){
				sendSitPacket(p);
			}
		}
	}

	private void stopSittingOnMe(Player p){
		if (sitters.containsKey(p.getName())) {
			sitters.remove(p.getName());
		}

		sendStandPacket(p);
	}

	//Aka, lazy copy/paste code.
	public static void onDisabledStopSitting(){
		for(String s : sitters.keySet()){
			NMSPacket packet = new NMSPacket("Packet40EntityMetadata");
			packet.setField("a", Bukkit.getPlayer(s).getEntityId());
			packet.setDeclaredField("b", ccb.getListFromWatcher(ccb.newWatcherInstance((byte)0)));

			NMSLib.sendPackets(packet.seal());
		}
	}
}
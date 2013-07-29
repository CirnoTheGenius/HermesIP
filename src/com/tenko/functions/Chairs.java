package com.tenko.functions;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.util.Vector;

import com.tenko.FriendlyWall;

public class Chairs extends Function {

	private static HashMap<String, Arrow> sitters = new HashMap<String, Arrow>();

	public Chairs(){
		super();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(FriendlyWall.getPlugin(), new Runnable(){
			@Override
			public void run(){
				for(Entry<String, Arrow> p: sitters.entrySet()){
					p.getValue().setTicksLived(1);
				}
			}
		}, 20L, 20L);
	}

	@EventHandler
	public void moveAway(PlayerMoveEvent e){
		Player plyr = e.getPlayer();
		if(sitters.containsKey(plyr.getName())){
			if(plyr.getLocation().distanceSquared(sitters.get(plyr.getName()).getLocation()) >= 2D){
				sitters.get(plyr.getName()).remove();
				sitters.remove(plyr.getName());
				plyr.sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
			}
		}
	}

	@EventHandler
	public void Ragequit(final PlayerQuitEvent event){	
		if(sitters.containsKey(event.getPlayer().getName())){
			Arrow ar = sitters.get(event.getPlayer().getName());
			ar.remove();
			event.getPlayer().teleport(event.getPlayer().getLocation().add(0,3,0));
			sitters.remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void screwYoChair(final BlockBreakEvent event){
		if(!sitters.isEmpty() && sitters.containsKey(event.getPlayer().getName())){
			sitters.get(event.getPlayer().getName()).remove();
			sitters.remove(event.getPlayer().getName());
			Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
				@Override
				public void run(){
					event.getPlayer().teleport(event.getPlayer().getLocation().add(0,1,0));
				}
			});
			event.getPlayer().sendMessage(ChatColor.YELLOW + "You have stopped sitting.");
		}
	}

	@EventHandler
	public void sitDown(final PlayerInteractEvent e){
		if(e.hasBlock() && e.getAction() == Action.RIGHT_CLICK_BLOCK && !sitters.containsKey(e.getPlayer().getName()) && e.getPlayer().isSneaking() && e.getPlayer().getLocation().distanceSquared(e.getClickedBlock().getLocation()) < 4D){
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

				if(s.isInverted()) return;

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
					break;
				default:
					break;
				}

				e.getPlayer().teleport(nLocation);
				e.setUseInteractedBlock(Result.DENY);
				e.setUseItemInHand(Result.DENY);
				final Arrow arrow = e.getClickedBlock().getWorld().spawnArrow(nLocation, new Vector(0,0,0), 0, 0);
				arrow.setVelocity(new Vector(0,0,0));
				arrow.setPassenger(e.getPlayer());
				e.getPlayer().setSneaking(false);
				sitters.put(e.getPlayer().getName(), arrow);
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have sat.");
			} else if(e.getClickedBlock().getType() == Material.BED_BLOCK || e.getClickedBlock().getState().getData() instanceof Step || e.getClickedBlock().getTypeId() == 126){
				Location nLocation = e.getClickedBlock().getLocation().clone();	
				nLocation.add(0.5D, 0.3D, 0.5D);

				e.getPlayer().teleport(nLocation);
				e.setUseInteractedBlock(Result.DENY);
				e.setUseItemInHand(Result.DENY);
				final Arrow arrow = e.getClickedBlock().getWorld().spawnArrow(nLocation, new Vector(0,0,0), 0, 0);
				arrow.setVelocity(new Vector(0,0,0));
				arrow.setPassenger(e.getPlayer());
				e.getPlayer().setSneaking(false);
				sitters.put(e.getPlayer().getName(), arrow);
				e.getPlayer().sendMessage(ChatColor.YELLOW + "You have sat.");
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		return false;
	}
}

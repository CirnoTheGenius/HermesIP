package com.tenko.functions;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.NotePlayEvent;

public class NazrinBlocks extends Function {

	@EventHandler(priority=EventPriority.LOW)
	public void onNoteblockPlay(NotePlayEvent event){	
		final NoteBlock note = (NoteBlock)event.getBlock().getState();
		Block belowBlock = note.getWorld().getBlockAt(note.getLocation().subtract(0, 1, 0));
		if(belowBlock != null){
			switch(belowBlock.getType()){
			case DIAMOND_BLOCK:
				note.getWorld().playSound(note.getLocation(), Sound.NOTE_PLING, 1, note.getRawNote());
				event.setCancelled(true);
				break;
			case IRON_BLOCK:
				note.getWorld().playSound(note.getLocation(), Sound.CAT_MEOW, 1, note.getRawNote());
				event.setCancelled(true);
				break;
			case GOLD_BLOCK:
				note.getWorld().playSound(note.getLocation(), Sound.ENDERDRAGON_GROWL, 1, note.getRawNote());
				event.setCancelled(true);
				break;
			case PUMPKIN:
				note.getWorld().playSound(note.getLocation(), Sound.ZOMBIE_IDLE, 1, note.getRawNote());
				event.setCancelled(true);
				break;
			case EMERALD_BLOCK:
				note.getWorld().playSound(note.getLocation(), Sound.GHAST_MOAN, 1, note.getRawNote());
				event.setCancelled(true);
				break;
			default:
				break;
			}
		}
	}
	
}

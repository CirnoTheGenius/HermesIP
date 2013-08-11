package com.tenko.NovEngine.optiontypes;

import org.bukkit.entity.Player;

import com.tenko.asm.IMika;

public class RunnableOption extends Option {
	
	private ResultRunnable result;
	
	public RunnableOption(String q, ResultRunnable r) {
		super(q);
		this.result = r;
	}
	
	@Override
	public void onSelected(IMika em, Player p){
		result.run();
	}
	
}

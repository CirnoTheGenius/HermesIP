package com.tenko.npc.controllers;

import com.tenko.npc.BaseNPC;

import net.minecraft.server.v1_6_R2.ControllerJump;
import net.minecraft.server.v1_6_R2.EntityLiving;

public class NPCControllerJump extends ControllerJump {
	
	private EntityLiving a;
	private boolean b;
	
	public NPCControllerJump(BaseNPC npc){
		super(BaseNPC.SCAPEGOAT);
		this.a = (EntityLiving) npc.getHandle();
	}
	
	@Override
	public void a(){
		b = true;
	}
	
	@Override
	public void b(){
		a.f(b);
		b = false;
	}

}
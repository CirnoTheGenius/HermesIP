package com.tenko.npc.pathgoals;

import com.tenko.npc.BaseNPC;

import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityHuman;
import net.minecraft.server.v1_6_R2.PathfinderGoal;

public class PathfinderGoalLookAtPlayer extends PathfinderGoal {

	private Entity entity;
	private BaseNPC npc;
	protected Entity targetEntity;
	private Class<?> targetEntityClass;

	private int d;
	private float someFloat;
	private float radius;

	public PathfinderGoalLookAtPlayer(BaseNPC entity, Class<?> targetClass, float someFloat){
		this.entity = entity.getHandle();
		this.someFloat = 0.02F;
		this.radius = someFloat;
		this.targetEntityClass = targetClass;
		npc = entity;
		this.a(2);
	}


	@Override
	public boolean a(){
		if(npc.getRandom().nextFloat() >= this.someFloat){
			return false;
		} else {
            if (this.targetEntityClass == EntityHuman.class) {
                this.targetEntity = entity.world.findNearbyPlayer(entity, (double)radius);
            } else {
                this.targetEntity = entity.world.a(targetEntityClass, entity.boundingBox.grow((double)radius, 3.0D, (double)radius), entity);
            }
            
            return this.targetEntity != null;
		}

	}
	
	@Override
	public boolean b(){
		return !entity.isAlive() ? false : (entity.e(this.targetEntity) > (double) (this.someFloat * this.someFloat) ? false : this.d > 0);
	}
	
	@Override
	public void c(){
		this.d = 40 + npc.getRandom().nextInt(40);
	}

	@Override
	public void d(){
		entity = null;
	}

	@Override
	public void e(){
		//entity.getControllerLook().a(entity.locX, entity.locY + (double) entity.getHeadHeight(), entity.locZ, 10.0F, (float)entity.bp());
		--this.d;
	}

}

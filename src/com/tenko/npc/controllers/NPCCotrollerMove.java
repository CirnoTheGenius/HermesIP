package com.tenko.npc.controllers;

import com.tenko.npc.BaseNPC;

import net.minecraft.server.v1_6_R2.ControllerMove;
import net.minecraft.server.v1_6_R2.GenericAttributes;

public class NPCCotrollerMove extends ControllerMove {

	private BaseNPC a;
	private double b;
	private double c;
	private double d;
	private double e;
	private boolean f;
	
	public NPCCotrollerMove(BaseNPC npc) {
		super(BaseNPC.SCAPEGOAT);
		this.a = npc;
		this.b = a.getLivingHandle().locX;
		this.c = a.getLivingHandle().locY;
		this.d = a.getLivingHandle().locZ;
	}

	@Override
	public boolean a(){
		return this.f;
	}

	@Override
	public double b(){
		return this.e;
	}

	@Override
	public void a(double d0, double d1, double d2, double d3){
		this.b = d0;
		this.c = d1;
		this.d = d2;
		this.e = d3;
		this.f = true;
	}

	@Override
	public void c(){
		this.a.getLivingHandle().bf = 0.0f;
		if (this.f) {
			this.f = false;
			int i = (int)Math.floor(this.a.getLivingHandle().boundingBox.b + 0.5D);
			double d0 = this.b - this.a.getLivingHandle().locX;
			double d1 = this.d - this.a.getLivingHandle().locZ;
			double d2 = this.c - (double) i;
			double d3 = d0 * d0 + d2 * d2 + d1 * d1;

			if (d3 >= 2.500000277905201E-7D) {
				float f = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;

				this.a.getLivingHandle().yaw = this.a(this.a.getLivingHandle().yaw, f, 30.0F);
				this.a.getLivingHandle().i((float) (this.e * this.a.getLivingHandle().getAttributeInstance(GenericAttributes.d).getValue()));
				if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D) {
					this.a.getJumpController().a();
				}
			}
		}
	}

	private float a(float f, float f1, float f2){
		float f3 = f1-f;
		float f4 = ((f3 % 360) > 180.0F) ? (f3-360F) : (((f3 % 360) < -180.0F) ? f3+360.0F : f3);

		if (f4 > f2) {
			f4 = f2;
		}

		if (f4 < -f2) {
			f4 = -f2;
		}

		return f + f4;
	}

}

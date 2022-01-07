package com.mydoomsite.statsmod.handlers;

import com.mydoomsite.statsmod.lib.GlobalProperties;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;

public class TickHandlers
{
	private static Minecraft minecraft = Minecraft.getInstance();
	private static double previousHealth = -1.0;
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase != Phase.END || minecraft.player == null)
			return;
		
		if(GlobalProperties.deathTimer > 0.0 && (!minecraft.level.isClientSide() || !minecraft.isPaused())) {
			GlobalProperties.deathTimer -= 1.0;
		}
		
		if(previousHealth == -1.0) {
			previousHealth = minecraft.player.getHealth();
		}
		
		if(minecraft.player.getHealth() < previousHealth) {
			GlobalProperties.damageTaken += (previousHealth - minecraft.player.getHealth())/2.0;
		}
		
		previousHealth = minecraft.player.getHealth();
		
		if(!minecraft.player.isAlive() && GlobalProperties.deathTimer <= 0.0) {
			GlobalProperties.deathTimer = GlobalProperties.deathTimerMax;
		}
		
		if(GlobalProperties.doDistanceCalculations)
		{
			if(GlobalProperties.playerLastTickPos == null)
				GlobalProperties.playerLastTickPos = minecraft.player.position();
			
			Vec3 playerPos = minecraft.player.position();
			double playX = playerPos.x - GlobalProperties.playerLastTickPos.x;
			double playY = playerPos.y - GlobalProperties.playerLastTickPos.y;
			double playZ = playerPos.z - GlobalProperties.playerLastTickPos.z;
			
			GlobalProperties.playerSpeed = Math.sqrt(playX*playX + playZ*playZ);
			GlobalProperties.distanceTravelled += GlobalProperties.playerSpeed;
			
			// Overflow check
			if(GlobalProperties.distanceTravelled < 0.0)
				GlobalProperties.distanceTravelled = 0.0;
			
			if(playY < 0) // Falling results in negative Y
			{
				GlobalProperties.distanceFallen += -(playY);
				
				// Overflow check
				if(GlobalProperties.distanceFallen < 0.0)
					GlobalProperties.distanceFallen = 0.0;
			}
		}
		
		GlobalProperties.playerLastTickPos = minecraft.player.position();
	}
	
	@SubscribeEvent
	public void onClientTick2(TickEvent.ClientTickEvent event)
	{
		if(minecraft.player == null || !GlobalProperties.renderList || !GlobalProperties.isStatusLethal)
			return;
		
		RenderHandler.StatusAlpha = RenderHandler.StatusAlpha - 15 < 0 ? 255 : RenderHandler.StatusAlpha - 15;
	}
}

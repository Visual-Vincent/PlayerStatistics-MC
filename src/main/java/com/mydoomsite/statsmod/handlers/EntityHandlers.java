/*package com.mydoomsite.statsmod.handlers;

import com.mydoomsite.statsmod.lib.GlobalProperties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Deprecated
public class EntityHandlers {
	@SubscribeEvent
	public void onEntityLivingDeath(LivingDeathEvent event) {
		if(event.getEntity() instanceof EntityPlayer || 
			event.getEntity() instanceof EntityPlayerSP || 
			 event.getEntity() instanceof EntityPlayerMP ||
			  event.getEntity() == Minecraft.getMinecraft().player) {
			if(GlobalProperties.deathTimer <= 0.0) {
				GlobalProperties.deathTimer = GlobalProperties.deathTimerMax;
			}
		}
	}
}*/

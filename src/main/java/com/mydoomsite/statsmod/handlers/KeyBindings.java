package com.mydoomsite.statsmod.handlers;

import org.lwjgl.glfw.GLFW;

import com.mydoomsite.statsmod.Main.Drawing;
import com.mydoomsite.statsmod.lib.GlobalProperties;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyBindings
{
	public static final int ShowHideList = 0;
	public static final int ShowHideCoords = 1;
	public static final int ResetDistances = 2;
	public static final int EnableDistanceCalcs = 3;
	public static final int ToggleLethalFlashing = 4;
	public static final int ChangeListSize = 5;
	public static final int ToggleAdditionalStatistics = 6;
	public static final int ClearDeathDropsTimer = 7;
	public static final int StartStopMeasurement = 8;
	
	private static final String[] desc = {
		"Show/hide list",
		"Show/hide coordinates",
		"Reset distance travelled/fallen",
		"Enable/disable distance calculations",
		"Toggle flashing when status is lethal",
		"Change list size",
		"Toggle additional statistics",
		"Clear death drops de-spawn timer",
		"Start/stop measurement"
	};
	
	private static final int[] keyValues = {
		GLFW.GLFW_KEY_J,
		GLFW.GLFW_KEY_K,
		GLFW.GLFW_KEY_KP_3,
		GLFW.GLFW_KEY_KP_2,
		GLFW.GLFW_KEY_KP_1,
		GLFW.GLFW_KEY_KP_4,
		GLFW.GLFW_KEY_KP_5,
		GLFW.GLFW_KEY_KP_6,
		GLFW.GLFW_KEY_KP_7
	};
	
	private final KeyMapping[] keys;
	
	private static Minecraft minecraft = Minecraft.getInstance();
	
	public KeyBindings()
	{
		keys = new KeyMapping[desc.length];
		
		for (int i = 0; i < desc.length; ++i)
		{
			keys[i] = new KeyMapping(desc[i], keyValues[i], "Player Statistics List");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		if ((minecraft.isWindowActive() || (minecraft.screen != null && (minecraft.screen instanceof ChatScreen))) && !minecraft.options.renderDebug)
		{
			if(keys[ShowHideList].isDown())
			{
				GlobalProperties.renderList = !GlobalProperties.renderList;
			}
			else if(keys[ShowHideCoords].isDown())
			{
				GlobalProperties.renderCoords = !GlobalProperties.renderCoords;
			}
			else if(keys[ResetDistances].isDown())
			{
				GlobalProperties.distanceTravelled = 0.0;
				GlobalProperties.distanceFallen = 0.0;
			}
			else if(keys[EnableDistanceCalcs].isDown())
			{
				GlobalProperties.doDistanceCalculations = !GlobalProperties.doDistanceCalculations;
			}
			else if(keys[ToggleLethalFlashing].isDown())
			{
				GlobalProperties.enableLethalFlashing = !GlobalProperties.enableLethalFlashing;
				Drawing.sendChatMessageToClient("Flash status when lethal: " + 
								(GlobalProperties.enableLethalFlashing ? "\u00A72Enabled" : "\u00A74Disabled"));
			}
			else if(keys[ChangeListSize].isDown())
			{
				GlobalProperties.listScale -= 0.25F;
				if(GlobalProperties.listScale < 0.25F) { GlobalProperties.listScale = 1.0F; }
			}
			else if(keys[ToggleAdditionalStatistics].isDown())
			{
				GlobalProperties.AdditionalStatistics = (GlobalProperties.AdditionalStatistics + 1) % GlobalProperties.AdditionalStatisticsCount;
				
				String message = "<error>";
				switch(GlobalProperties.AdditionalStatistics)
				{
					case 0: message = "\u00A74Disabled"; break;
					case 1: message = "\u00A7aTarget Identification only"; break;
					case 2: message = "\u00A7aTarget Identification and Tool Statistics"; break;
				}
				
				Drawing.sendChatMessageToClient("Additional statistics: " + message);
			}
			else if(keys[ClearDeathDropsTimer].isDown())
			{
				GlobalProperties.deathTimer = 0.0;
				Drawing.sendChatMessageToClient("Cleared death drops de-spawn timer.");
			}
			else if(keys[StartStopMeasurement].isDown())
			{
				if(GlobalProperties.measureStart == null)
					GlobalProperties.measureStart = minecraft.player.blockPosition();
				else
					GlobalProperties.measureStart = null;
			}
		}
	}

}

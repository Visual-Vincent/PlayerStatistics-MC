package com.mydoomsite.statsmod.Main;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

import com.mydoomsite.statsmod.handlers.KeyBindings;
import com.mydoomsite.statsmod.handlers.RenderHandler;
import com.mydoomsite.statsmod.handlers.TickHandlers;
import com.mydoomsite.statsmod.helpers.StringHelper;
import com.mydoomsite.statsmod.lib.ReferenceStrings;

@Mod(ReferenceStrings.MODID)
public class MainRegistry
{
	public MainRegistry()
	{
		// This mod is client-side only, therefore it should not be expected to be loaded on any servers.
		// Suppresses the "Incompatible" message on other Forge-based servers.
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (String a, Boolean b) -> true));
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::Setup);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ClientSetup);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::RegisterKeyBindings);
		});
	}
	
	private void Setup(final FMLCommonSetupEvent event)
	{
		
	}
	
	private void ClientSetup(final FMLClientSetupEvent event)
	{
		StringHelper.Initialize();
		MinecraftForge.EVENT_BUS.register(RenderHandler.Instance);
		MinecraftForge.EVENT_BUS.register(KeyBindings.Instance);
		MinecraftForge.EVENT_BUS.register(new TickHandlers());
	}
	
	private void RegisterKeyBindings(RegisterKeyMappingsEvent event)
	{
		KeyBindings.RegisterKeyBindings(event);
	}
}

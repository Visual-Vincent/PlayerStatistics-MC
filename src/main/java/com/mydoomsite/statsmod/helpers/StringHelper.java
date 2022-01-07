package com.mydoomsite.statsmod.helpers;

import java.text.DecimalFormatSymbols;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

import com.mydoomsite.statsmod.handlers.RenderHandler;
import com.mydoomsite.statsmod.lib.GlobalProperties;

public class StringHelper
{
	private static Minecraft minecraft = Minecraft.getInstance();
	public static DecimalFormatSymbols DecimalSymbols = new DecimalFormatSymbols();
	
	public static void Initialize()
	{
		DecimalSymbols.setDecimalSeparator('.');
	}
	
	public static String colorFloatValue(float value, float max, boolean maxIsBad)
	{
		float[] boundaries = new float[] {
			(max/5)*1,
			(max/5)*2,
			(max/5)*3,
			(max/5)*4,
		};
		
		if(value < boundaries[3] && value >= boundaries[2]) //60-80%
		{
			if(!maxIsBad) { return "\u00A7a" + value; }
			else 		  { return "\u00A7c" + value; }
		}
		else if(value < boundaries[2] && value >= boundaries[1]) //40-60%
		{
			if(!maxIsBad) { return "\u00A7e" + value; }
			else 		  { return "\u00A7e" + value; }
		}
		else if(value < boundaries[1] && value >= boundaries[0]) //20-40%
		{
			if(!maxIsBad) { return "\u00A7c" + value; }
			else 		  { return "\u00A7a" + value; }
		}
		else if(value < boundaries[0]) //0-20%
		{
			if(!maxIsBad) { return "\u00A74" + value; }
			else 		  { return "\u00A72" + value; }
		}
		//80-100%
		if(!maxIsBad) { return "\u00A72" + value; }
		else 		  { return "\u00A74" + value; }
	}
	
	public static String colorIntValue(int value, int max, boolean maxIsBad)
	{
		float[] boundaries = new float[] {
			(max/5)*1,
			(max/5)*2,
			(max/5)*3,
			(max/5)*4,
		};
		
		if(value < boundaries[3] && value >= boundaries[2]) //60-80%
		{
			if(!maxIsBad) { return "\u00A7a" + value; }
			else 		  { return "\u00A7c" + value; }
		}
		else if(value < boundaries[2] && value >= boundaries[1]) //40-60%
		{
			if(!maxIsBad) { return "\u00A7e" + value; }
			else 		  { return "\u00A7e" + value; }
		}
		else if(value < boundaries[1] && value >= boundaries[0]) //20-40%
		{
			if(!maxIsBad) { return "\u00A7c" + value; }
			else 		  { return "\u00A7a" + value; }
		}
		else if(value < boundaries[0]) //0-20%
		{
			if(!maxIsBad) { return "\u00A74" + value; }
			else 		  { return "\u00A72" + value; }
		}
		//80-100%
		if(!maxIsBad) { return "\u00A72" + value; }
		else 		  { return "\u00A74" + value; }
	}
	
	public static String colorDoubleValue(double value, double max, int decimals, boolean maxIsBad)
	{
		double[] boundaries = new double[] {
			(max/5)*1,
			(max/5)*2,
			(max/5)*3,
			(max/5)*4,
		};
		
		String resultingValue = MathHelper.roundWithTrail(value, decimals);
		
		if(value < boundaries[3] && value >= boundaries[2]) //60-80%
		{
			if(!maxIsBad) { return "\u00A7a" + resultingValue; }
			else 		  { return "\u00A7c" + resultingValue; }
		}
		else if(value < boundaries[2] && value >= boundaries[1]) //40-60%
		{
			if(!maxIsBad) { return "\u00A7e" + resultingValue; }
			else 		  { return "\u00A7e" + resultingValue; }
		}
		else if(value < boundaries[1] && value >= boundaries[0]) //20-40%
		{
			if(!maxIsBad) { return "\u00A7c" + resultingValue; }
			else 		  { return "\u00A7a" + resultingValue; }
		}
		else if(value < boundaries[0]) //0-20%
		{
			if(!maxIsBad) { return "\u00A74" + resultingValue; }
			else 		  { return "\u00A72" + resultingValue; }
		}
		//80-100%
		if(!maxIsBad) { return "\u00A72" + resultingValue; }
		else 		  { return "\u00A74" + resultingValue; }
	}
	
	public static String colorAirValue(int value, int max, boolean maxIsBad)
	{
		float[] boundaries = new float[] {
			(max/5)*1,
			(max/5)*2,
			(max/5)*3,
			(max/5)*4,
		};
		
		if(value < boundaries[3] && value >= boundaries[2]) //60-80%
		{
			if(!maxIsBad) { return "\u00A7a" + value; }
			else 		  { return "\u00A7c" + value; }
		}
		else if(value < boundaries[2] && value >= boundaries[1]) //40-60%
		{
			if(!maxIsBad) { return "\u00A7e" + value; }
			else 		  { return "\u00A7e" + value; }
		}
		else if(value < boundaries[1] && value >= boundaries[0]) //20-40%
		{
			if(!maxIsBad) { return "\u00A7c" + value; }
			else 		  { return "\u00A7a" + value; }
		}
		else if(value < boundaries[0]) //0-20%
		{
			if(!maxIsBad) { return "\u00A74" + value; }
			else 		  { return "\u00A7b" + value; }
		}
		//80-100%
		if(!maxIsBad) { return "\u00A7b" + value; }
		else 		  { return "\u00A74" + value; }
	}
	
	public static String getColorFromValue(long value, long max, boolean maxIsBad)
	{
		float[] boundaries = new float[] {
			(max/5)*1,
			(max/5)*2,
			(max/5)*3,
			(max/5)*4,
		};
		
		if(value < boundaries[3] && value >= boundaries[2]) //60-80%
		{
			if(!maxIsBad) { return "\u00A7a"; }
			else 		  { return "\u00A7c"; }
		}
		else if(value < boundaries[2] && value >= boundaries[1]) //40-60%
		{
			if(!maxIsBad) { return "\u00A7e"; }
			else 		  { return "\u00A7e"; }
		}
		else if(value < boundaries[1] && value >= boundaries[0]) //20-40%
		{
			if(!maxIsBad) { return "\u00A7c"; }
			else 		  { return "\u00A7a"; }
		}
		else if(value < boundaries[0]) //0-20%
		{
			if(!maxIsBad) { return "\u00A74"; }
			else 		  { return "\u00A72"; }
		}
		//80-100%
		if(!maxIsBad) { return "\u00A72"; }
		else 		  { return "\u00A74"; }
	}
	
	public static String getColorFromValue(double value, double max, boolean maxIsBad)
	{
		double[] boundaries = new double[] {
			(max/5)*1,
			(max/5)*2,
			(max/5)*3,
			(max/5)*4,
		};
		
		if(value < boundaries[3] && value >= boundaries[2]) //60-80%
		{
			if(!maxIsBad) { return "\u00A7a"; }
			else 		  { return "\u00A7c"; }
		}
		else if(value < boundaries[2] && value >= boundaries[1]) //40-60%
		{
			if(!maxIsBad) { return "\u00A7e"; }
			else 		  { return "\u00A7e"; }
		}
		else if(value < boundaries[1] && value >= boundaries[0]) //20-40%
		{
			if(!maxIsBad) { return "\u00A7c"; }
			else 		  { return "\u00A7a"; }
		}
		else if(value < boundaries[0]) //0-20%
		{
			if(!maxIsBad) { return "\u00A74"; }
			else 		  { return "\u00A72"; }
		}
		//80-100%
		if(!maxIsBad) { return "\u00A72"; }
		else 		  { return "\u00A74"; }
	}
	
	public static String formatDistance(double distance, boolean baseIsMeters, int decimals, boolean notBelowMeter)
	{
		double returnDistance = distance;
		String unit = "cm";
		
		if(baseIsMeters)
			returnDistance *= 100;
		
		if(returnDistance >= 100 && returnDistance < 100000 || (returnDistance < 100 && notBelowMeter == true))
		{
			returnDistance /= 100;
			unit = "m";
		}
		else if(returnDistance >= 100000 && returnDistance < 100000000)
		{
			returnDistance /= 100000;
			unit = "km";
		}
		else if(returnDistance >= 100000000)
		{
			returnDistance /= 100000000;
			unit = "mega-m";
		}
		
		return MathHelper.roundWithTrail(returnDistance, decimals) + " " + unit;
	}
	
	public static String getPlayerStatus(double healthPercentage, double foodPercentage)
	{
		double combinedValue = (healthPercentage * foodPercentage);
		boolean hasPoison = minecraft.player.hasEffect(MobEffects.POISON);
		
		if(GlobalProperties.isStatusLethal == true &&
			 ((combinedValue > 0.2 && !hasPoison) || !GlobalProperties.enableLethalFlashing))
		{
			
			GlobalProperties.isStatusLethal = false;
			RenderHandler.StatusAlpha = 255;
		}
		
		if(hasPoison == false)
		{
			if(combinedValue > 0.8)				{ return "\u00A72Healthy"; }
			else if(combinedValue <= 0.8 
					 && combinedValue > 0.6)	{ return "\u00A7aGood"; }
			else if(combinedValue <= 0.6
					 && combinedValue > 0.4)	{ return "\u00A7eRisky"; }
			else if(combinedValue <= 0.4
					 && combinedValue > 0.2)	{ return "\u00A7cCritical"; }
		}
		
		if(GlobalProperties.enableLethalFlashing == true)
			GlobalProperties.isStatusLethal = true;
		
		return "\u00A74Lethal";
	}
	
	public static <T> String appendIfNotNull(String input, T test, String append)
	{
		if(test == null)
			return input;
		
		return input + append;
	}
	
	public static String appendIfNotZero(String input, ItemStack test, String append)
	{
		if(test.getCount() == 0)
			return input;
		
		return input + append;
	}
	
	public static String appendIfHasDurability(String input, ItemStack test, String append)
	{
		if(test.getCount() == 0 || test.getMaxDamage() <= 0)
			return input;
		
		return input + append;
	}
}

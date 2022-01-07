package com.mydoomsite.statsmod.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.mydoomsite.statsmod.lib.ReferenceType;

import net.minecraft.world.item.ItemStack;

public class MathHelper
{
	public static final double SeaLevel = 63.0;
	
	public static double round(double value, int decimals)
	{
	    if(decimals < 0)
	    	decimals = 0;
	    
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(decimals, RoundingMode.HALF_UP);
	    
	    return bd.doubleValue();
	}
	
	public static String roundWithTrail(double value, int decimals)
	{
		value = MathHelper.round(value, decimals);
		
		if((double)(value - (int)value) == 0) //No decimals
			return String.valueOf((int)value);
		
		String numberFormatString = "#0.";
		
		for(int d=0; d<decimals; d++)
			numberFormatString += "0";
		
		NumberFormat formatter = new DecimalFormat(numberFormatString, StringHelper.DecimalSymbols);
		return formatter.format(value);
	}
	
	public static int calculateXpForLevel(int level)
	{
		if(level <= 16)
		{
			return (int)Math.pow(level, 2) + 6*level;
		}
		else if(level > 16 && level <= 31)
		{
			return (int)(((2.5*Math.pow(level, 2)) - (40.5*(double)level)) + 360.0);
		}
		else //Level 32 and up
		{
			return (int)(((4.5*Math.pow(level, 2)) - (162.5*(double)level)) + 2220.0);
		}
	}
	
	public static double damagePercentage(ItemStack item)
	{
		return MathHelper.damagePercentage(item, null);
	}
	
	public static double damagePercentage(ItemStack item, ReferenceType<Integer> nonNulls)
	{
		if(item == null || item.getCount() == 0 || item.getDamageValue() < 0 || item.getMaxDamage() <= 0)
			return 0.0;
		
		if(nonNulls != null)
			nonNulls.Value += 1;
		
		return (item.getDamageValue() * 100.0) / item.getMaxDamage();
	}
	
	public static double safeDivide(double a, double b)
	{
		if(b == 0.0)
			return 0.0;
		
		return a / b;
	}
}

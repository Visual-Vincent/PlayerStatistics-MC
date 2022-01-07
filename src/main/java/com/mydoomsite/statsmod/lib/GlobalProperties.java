package com.mydoomsite.statsmod.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.*;

public class GlobalProperties
{
	public static boolean doDistanceCalculations = true;
	public static boolean renderList = true;
	public static boolean renderCoords = true;

	public static Vec3 playerLastTickPos;
	public static double playerSpeed;
	public static BlockPos measureStart;
	
	public static double distanceTravelled = 0.0;
	public static double distanceFallen = 0.0;
	public static double deathTimer = 0.0;
	public static final double deathTimerMax = 6000.0; //5 minutes.
	
	public static boolean isStatusLethal = false;
	public static boolean enableLethalFlashing = true;
	public static double damageTaken = 0.0;
	
	public static float listScale = 1.0F;
	public static int AdditionalStatistics = 2;
	public static final int AdditionalStatisticsCount = 3; //0 = None, also counts as a state.
}

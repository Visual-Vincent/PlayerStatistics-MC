package com.mydoomsite.statsmod.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mojang.blaze3d.platform.Window;
import com.mydoomsite.statsmod.Main.Drawing;
import com.mydoomsite.statsmod.Main.RenderedLine;
import com.mydoomsite.statsmod.helpers.MathHelper;
import com.mydoomsite.statsmod.helpers.StringHelper;
import com.mydoomsite.statsmod.lib.GlobalProperties;
import com.mydoomsite.statsmod.lib.ReferenceType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;


public class RenderHandler
{
    public static RenderHandler Instance = new RenderHandler();
    private static Minecraft minecraft = Minecraft.getInstance();

    public static final float SwordBaseDamage = 3.0f; //From the constructor of SwordItem.
    
    public static int StatusAlpha = 255;
    public static int DeathTimerAlpha = 255;
    private static ReferenceType<Integer> armorPieces = new ReferenceType<Integer>(0);
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void CustomizeGuiOverlayEvent(CustomizeGuiOverlayEvent.DebugText event)
    {
        if ((minecraft.isWindowActive() || (minecraft.screen != null && (minecraft.screen instanceof ChatScreen))) && !minecraft.options.renderDebug)
        {
            if(!GlobalProperties.renderList && !GlobalProperties.renderCoords)
                return;
            
            Vec3 playerPos = minecraft.player.position();
            
            if(GlobalProperties.renderCoords)
            {
                String coords = 
                    MathHelper.roundWithTrail(playerPos.x, 2) + ", " +
                    MathHelper.roundWithTrail(playerPos.y, 2) + ", " +
                    MathHelper.roundWithTrail(playerPos.z, 2);
                
                Drawing.DrawTextScaled(event.getPoseStack(), coords, 4.0f*(1 / GlobalProperties.listScale), 4.0f*(1 / GlobalProperties.listScale), 0xFFFFFF, GlobalProperties.listScale, true);
            }
            
            if(!GlobalProperties.renderList)
                return;
            
            Window window = event.getWindow();
            
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            
            long TimeOfDay = minecraft.level.getDayTime() % 24000;
            int TimeLeftSeconds;
            String NextWhat;
            boolean isDay = TimeOfDay < 12000;
            
            if(TimeOfDay < 12000) //Next sunset.
            {
                TimeLeftSeconds = (int)((12000-TimeOfDay) / 20);
                NextWhat = "sunset";
            }
            else //Next dawn.
            {
                TimeOfDay -= 12000;
                TimeLeftSeconds = (int)((12000-TimeOfDay) / 20);
                NextWhat = "dawn";
            }
            
            String TimeString = TimeLeftSeconds/60 + ":" + (TimeLeftSeconds%60)/10 + TimeLeftSeconds%10;
            String DeathDropsString = "";
            
            if(GlobalProperties.deathTimer > 0.0)
            {
                int DeathDropsSeconds = (int)(GlobalProperties.deathTimer / 20.0);
                DeathDropsString = DeathDropsSeconds/60 + ":" + (DeathDropsSeconds%60)/10 + DeathDropsSeconds%10;
                
                Drawing.DrawCenteredText(
                    new RenderedLine(GlobalProperties.deathTimer > 0.0 ? "Death drops de-spawn: " + StringHelper.getColorFromValue(GlobalProperties.deathTimer, GlobalProperties.deathTimerMax, false) + DeathDropsString : "", DeathTimerAlpha), 
                    window, event.getPoseStack(), 0, 8, 0xFFFFFFF, GlobalProperties.listScale, true
                );
            }
            
            BlockPos playerBlockPos = minecraft.player.blockPosition();
            
            if(GlobalProperties.measureStart != null)
            {
                double diffX = playerBlockPos.getX() - GlobalProperties.measureStart.getX();
                double diffY = playerBlockPos.getY() - GlobalProperties.measureStart.getY();
                double diffZ = playerBlockPos.getZ() - GlobalProperties.measureStart.getZ();
                
                double dist = MathHelper.round(Math.sqrt(diffX*diffX + diffY*diffY + diffZ*diffZ), 2);
                
                Drawing.DrawCenteredText(
                    new RenderedLine("Distance: \u00A72" + dist + "m"), 
                    window, event.getPoseStack(), 0, 8 + minecraft.font.lineHeight + 2, 0xFFFFFFF, GlobalProperties.listScale, true
                );
            }
            
            float xpPercentage = minecraft.player.experienceProgress * 100;
            
            BlockPos CurrentBlock = new BlockPos(playerPos.x, playerPos.y, playerPos.z);
            
            int LightLevelSky 	= minecraft.level.getBrightness(LightLayer.SKY, CurrentBlock);
            int LightLevelBlock = minecraft.level.getBrightness(LightLayer.BLOCK, CurrentBlock);
            
            HitResult crosshairObj = minecraft.hitResult;
            
            ItemStack ArmorBoots 	  = minecraft.player.getInventory().getArmor(0);
            ItemStack ArmorLeggings   = minecraft.player.getInventory().getArmor(1);
            ItemStack ArmorChestplate = minecraft.player.getInventory().getArmor(2);
            ItemStack ArmorHelmet 	  = minecraft.player.getInventory().getArmor(3);
            
            String armorDisplay = "";
            double armorPercentage = 0.0;
            
            armorPieces.Value = 0;
            
            double ArmorBootsDamage 	 = MathHelper.damagePercentage(ArmorBoots,		armorPieces);
            double ArmorLegginsDamage 	 = MathHelper.damagePercentage(ArmorLeggings,	armorPieces);
            double ArmorChestplateDamage = MathHelper.damagePercentage(ArmorChestplate, armorPieces);
            double ArmorHelmetDamage 	 = MathHelper.damagePercentage(ArmorHelmet,		armorPieces);
            
            armorPercentage = 	ArmorBootsDamage + 
                                ArmorLegginsDamage + 
                                ArmorChestplateDamage + 
                                ArmorHelmetDamage;
            
            armorPercentage = MathHelper.safeDivide(armorPercentage, armorPieces.Value);
            
            armorDisplay = "\u00A7f(";
            armorDisplay = StringHelper.appendIfHasDurability(armorDisplay, ArmorHelmet, 	 StringHelper.getColorFromValue(ArmorHelmetDamage,		100.0, true) + "H");
            armorDisplay = StringHelper.appendIfHasDurability(armorDisplay, ArmorChestplate, StringHelper.getColorFromValue(ArmorChestplateDamage,	100.0, true) + "C");
            armorDisplay = StringHelper.appendIfHasDurability(armorDisplay, ArmorLeggings,	 StringHelper.getColorFromValue(ArmorLegginsDamage,		100.0, true) + "L");
            armorDisplay = StringHelper.appendIfHasDurability(armorDisplay, ArmorBoots,		 StringHelper.getColorFromValue(ArmorBootsDamage,		100.0, true) + "B");
            armorDisplay += "\u00A7f)";
            
            if(armorPieces.Value == 0) {
                armorDisplay = "";
            }
            
            ItemStack heldItemMainHand = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack heldItemOffHand  = minecraft.player.getItemInHand(InteractionHand.OFF_HAND);
            
            float directionYaw = minecraft.player.getRotationVector().y + 180.0f;
            float directionDegreesYaw = Math.abs(directionYaw) % 360.0f;
            float directionDegrees = (directionYaw >= 0.0f) ? directionDegreesYaw : (360.0f - directionDegreesYaw);
            String direction = (int)Math.floor(directionDegrees) + "";
            
            if(directionDegrees > 337.5f || directionDegrees <= 22.5f) {
                direction = direction + " N";
            }
            else if(directionDegrees > 22.5f && directionDegrees <= 67.5f) {
                direction = direction + " NE";
            }
            else if(directionDegrees > 67.5f && directionDegrees <= 112.5f) {
                direction = direction + " E";
            }
            else if(directionDegrees > 112.5f && directionDegrees <= 157.5f) {
                direction = direction + " SE";
            }
            else if(directionDegrees > 157.5f && directionDegrees <= 202.5f) {
                direction = direction + " S";
            }
            else if(directionDegrees > 202.5f && directionDegrees <= 247.5f) {
                direction = direction + " SW";
            }
            else if(directionDegrees > 247.5f && directionDegrees <= 292.5f) {
                direction = direction + " W";
            }
            else if(directionDegrees > 292.5f && directionDegrees <= 337.5f) {
                direction = direction + " NW";
            } 
            
            RenderedLine[] Statistics = new RenderedLine[] {
                new RenderedLine("\u00A7aPlayer Statistics"),
                new RenderedLine("\u00A7a--------------"),
                
                new RenderedLine("Direction: \u00A7a" + direction),
                new RenderedLine("Status: " + StringHelper.getPlayerStatus(MathHelper.round(minecraft.player.getHealth()/20.0, 2), MathHelper.round(minecraft.player.getFoodData().getFoodLevel()/20.0, 2)), StatusAlpha),
                new RenderedLine("Health: " + StringHelper.colorIntValue((int)((minecraft.player.getHealth()/2.0)*10.0), 100, false) + "%"),
                new RenderedLine("Food:   " + StringHelper.colorIntValue((int)((minecraft.player.getFoodData().getFoodLevel()/2.0)*10.0), 100, false) + "%"),
                new RenderedLine("Air:     " + StringHelper.colorAirValue((minecraft.player.getAirSupply()/3)>=0 ? (minecraft.player.getAirSupply()/3) : 0, 100, false) + "%"),
                new RenderedLine(playerPos.y >= MathHelper.SeaLevel ? "MASL:   \u00A7a" + MathHelper.roundWithTrail(playerPos.y - MathHelper.SeaLevel, 2) + " m" : "MBSL:   \u00A7a" + MathHelper.roundWithTrail(MathHelper.SeaLevel - playerPos.y, 2) + " m"),
                new RenderedLine("Xp:      \u00A7a" + MathHelper.round(xpPercentage, 2) + "%"),
                
                new RenderedLine("Light:   " + (LightLevelSky > 7 ? "\u00A7a" : "\u00A7c") + LightLevelSky + " " + (LightLevelBlock > 7 ? "\u00A7a" : "\u00A7c") + LightLevelBlock),
                new RenderedLine("Speed:     \u00A7a" + (GlobalProperties.doDistanceCalculations ? MathHelper.roundWithTrail(20*GlobalProperties.playerSpeed, 2) + " m/s" : "\u00A74-")),
                new RenderedLine("Travelled: \u00A7a" + (GlobalProperties.doDistanceCalculations ? StringHelper.formatDistance(GlobalProperties.distanceTravelled, true, 2, false) : "\u00A74-")),
                new RenderedLine("Fallen:      \u00A7a" + (GlobalProperties.doDistanceCalculations ? StringHelper.formatDistance(GlobalProperties.distanceFallen, true, 2, false) : "\u00A74-")),
                
                new RenderedLine("Dmg taken:  \u00A7b" + MathHelper.roundWithTrail(GlobalProperties.damageTaken, 1) + " hp"),
                
                new RenderedLine("Next " + NextWhat + ":  " + StringHelper.getColorFromValue(TimeOfDay, 12000, isDay) + TimeString),
                
                // Be last
                new RenderedLine(" "),
                new RenderedLine(armorPieces.Value > 0 ? "Armor (avg.): " + StringHelper.colorDoubleValue(armorPercentage, 100.0, 2, true) + "% " + armorDisplay : ""),
                new RenderedLine(heldItemMainHand != null && heldItemMainHand.getMaxDamage() > 0 ? heldItemMainHand.getHoverName().getString() + ": " + StringHelper.colorDoubleValue(((double)heldItemMainHand.getDamageValue()*100.0)/(double)heldItemMainHand.getMaxDamage(), 100, 2, true) + "%" : ""),
                new RenderedLine(heldItemOffHand != null && heldItemOffHand.getMaxDamage() > 0 ? heldItemOffHand.getHoverName().getString() + ": " + StringHelper.colorDoubleValue(((double)heldItemOffHand.getDamageValue()*100.0)/(double)heldItemOffHand.getMaxDamage(), 100, 2, true) + "%" : "")
            };
            Drawing.DrawTextLines(Statistics, window, event.getPoseStack(), 1, 8, height / 2, 0xFFFFFF, GlobalProperties.listScale, true);
            
            
            int rStatIndex = 0;
            RenderedLine[] rightStatistics = new RenderedLine[48];
            boolean hasToolStatistics = false;
            
            // Right now you may be asking yourself:
            //    "Why is this guy using a regular array instead of an ArrayList<>?"
            //
            // The answer is simple:
            //    Even though an ArrayList<> is much more flexible than a regular array, 
            //    I want to ensure that this mod affects performance as little as possible.
            //    Thus, I sacrifice readability and flexibility in order to get a little
            //    (even though it is very little) performance gain.
            
            
            // Statistics about the held item
            if(GlobalProperties.AdditionalStatistics == 2 && heldItemMainHand != null && heldItemMainHand.isEnchanted() == true)
            {
                ListTag toolEnchantments = heldItemMainHand.getEnchantmentTags();
                if(toolEnchantments != null)
                {
                    int enchantmentCount = toolEnchantments.size();
                    
                    rightStatistics[rStatIndex] = new RenderedLine("\u00A7a" + heldItemMainHand.getHoverName().getString());	rStatIndex ++;
                    rightStatistics[rStatIndex] = new RenderedLine("\u00A7a------------"); 							            rStatIndex ++;
                    
                    Item heldItem = heldItemMainHand.getItem();
                    
                    float SwordDamage = (heldItem instanceof SwordItem) ? (3.0f + ((SwordItem)heldItem).getDamage() + 1.0f) : 0.0f;
                    float SwordDamageFactor = 0.0f;
                    int SwordDamageIndex = rStatIndex; rStatIndex ++;
                    rightStatistics[SwordDamageIndex] = new RenderedLine("");
                    
                    for(int i = 0; i < enchantmentCount; i++)
                    {
                        CompoundTag enchantment = (CompoundTag)toolEnchantments.get(i);
                        String id = enchantment.getString("id");
                        short level = enchantment.getShort("lvl");
                        
                        if(level > 0)
                        {
                            double sweepDamage;
                            switch(id)
                            {
                                case "minecraft:sharpness":
                                    if(EnchantmentCategory.WEAPON.canEnchant(heldItem) && heldItem instanceof SwordItem)
                                    {
                                        SwordItem Sword = (SwordItem)heldItem;
                                        SwordDamageFactor += (level - 1.0f) * 0.5f + 1.0f;
                                    }
                                break;
                                
                                case "minecraft:smite":
                                    rightStatistics[rStatIndex] = new RenderedLine("Undead: \u00A72+" + Math.round(((SwordDamage + level * 2.5f) / SwordDamage - 1.0f) * 100.0f) + "%"); rStatIndex ++;
                                break;
                                
                                case "minecraft:bane_of_arthropods":
                                    rightStatistics[rStatIndex] = new RenderedLine("Arthropods: \u00A72+" + Math.round(((SwordDamage + level * 2.5f) / SwordDamage - 1.0f) * 100.0f) + "%"); rStatIndex ++;
                                break;
                                
                                case "minecraft:knockback":
                                    rightStatistics[rStatIndex] = new RenderedLine("Knockback: \u00A72+" + (level * 3)); rStatIndex ++;
                                break;
                                
                                case "minecraft:fire_aspect":
                                    rightStatistics[rStatIndex] = new RenderedLine("Applied fire: \u00A72" + (level * 4) + "s"); rStatIndex ++;
                                break;
                                
                                case "minecraft:looting":
                                    rightStatistics[rStatIndex] = new RenderedLine("Maximum loot: \u00A72+" + level); rStatIndex ++;
                                break;
                                
                                case "minecraft:sweeping_edge":
                                    sweepDamage = 50.0D;
                                    if (level == 2) { sweepDamage = 67.0D; }
                                    else if (level >= 3) { sweepDamage = 75.0D; }
                                
                                    rightStatistics[rStatIndex] = new RenderedLine("Sweep: \u00A72+" + Math.round(sweepDamage) + "%"); rStatIndex ++;
                                break;
                                
                                case "minecraft:efficiency":
                                    rightStatistics[rStatIndex] = new RenderedLine("Mining speed: \u00A72" + Math.round(Math.pow(level, 2.0D) + 1.0D) + "x"); rStatIndex ++;
                                break;
                                
                                case "minecraft:unbreaking":
                                    rightStatistics[rStatIndex] = new RenderedLine("Durability: \u00A72" + (level + 1) + "x"); rStatIndex ++;
                                break;
                                
                                case "minecraft:fortune":
                                    rightStatistics[rStatIndex] = new RenderedLine("Maximum drops: \u00A72" + (level + 1) + "x"); rStatIndex ++;
                                break;
                                
                                case "minecraft:power":
                                    rightStatistics[rStatIndex] = new RenderedLine("Damage: \u00A72+" + Math.round(0.25D * (level + 1) * 100.0D) + "%"); rStatIndex ++;
                                break;
                                
                                case "minecraft:punch":
                                    rightStatistics[rStatIndex] = new RenderedLine("Knockback: \u00A72+" + (level * 3)); rStatIndex ++;
                                break;
                                
                                case "minecraft:flame":
                                    rightStatistics[rStatIndex] = new RenderedLine("Applied fire: \u00A725s"); rStatIndex ++;
                                break;
                            }
                        } 
                    }
                    
                    if(SwordDamageFactor > 0.0f)
                    {
                        SwordDamageFactor = (SwordDamage + SwordDamageFactor) / SwordDamage - 1.0f;
                        rightStatistics[SwordDamageIndex] = new RenderedLine("Damage: \u00A72+" + Math.round(SwordDamageFactor * 100.0f) + "%");
                    }
                    
                    hasToolStatistics = true;
                }
            }
            
            // Target identification
            if(GlobalProperties.AdditionalStatistics >= 1 && crosshairObj != null)
            {
                if(crosshairObj != null
                 && crosshairObj.getType() == HitResult.Type.ENTITY
                  && ((EntityHitResult)crosshairObj).getEntity() instanceof LivingEntity)
                {
                    LivingEntity TargetEntity = (LivingEntity)((EntityHitResult)crosshairObj).getEntity();
                    
                    double playX = playerPos.x;
                    double playY = playerPos.y;
                    double playZ = playerPos.z;
                    
                    Vec3 entityPos = TargetEntity.position();
                    
                    double entX = entityPos.x;
                    double entY = entityPos.y;
                    double entZ = entityPos.z;
                    
                    double diffX = entX - playX;
                    double diffZ = entZ - playZ;
                    
                    double EnemyDistance = Math.sqrt(diffX*diffX + diffZ*diffZ);
                    
                    if(hasToolStatistics == true)
                    {
                        rightStatistics[rStatIndex] = new RenderedLine(" "); rStatIndex ++;
                        rightStatistics[rStatIndex] = new RenderedLine(" "); rStatIndex ++;
                    }
                    
                    rightStatistics[rStatIndex] = new RenderedLine("\u00A74Target"); rStatIndex ++;
                    rightStatistics[rStatIndex] = new RenderedLine("\u00A74------"); rStatIndex ++;
                    rightStatistics[rStatIndex] = new RenderedLine("Name:     \u00A7b" + TargetEntity.getName().getString()); rStatIndex ++;
                    rightStatistics[rStatIndex] = new RenderedLine("Health:   " + StringHelper.colorIntValue((int)((TargetEntity.getHealth()/TargetEntity.getMaxHealth())*100.0f), 100, false) + "%"); rStatIndex ++;
                    rightStatistics[rStatIndex] = new RenderedLine("Distance: \u00A7b" + StringHelper.formatDistance(EnemyDistance, true, 2, true)); rStatIndex ++;
                    
                    // Specific mob statistics
                    if(TargetEntity.getClass().getSimpleName().contains("Creeper"))
                    {
                        try
                        {
                            int swell = (Integer)getInstanceField(TargetEntity, "f_32270_");
                            int swellMax = (Integer)getInstanceField(TargetEntity, "f_32271_");
                            
                            double explodeTics = swellMax - swell;
                            String explodeSecs = MathHelper.roundWithTrail(explodeTics / 20.0, 2);
                            
                            rightStatistics[rStatIndex] = new RenderedLine("\u00A74Explodes in: \u00A7b" + explodeSecs + " s"); rStatIndex ++;
                        }
                        catch(Throwable e) {}
                        
                        // fuseTime and timeSinceIgnited don't exist anymore
                        /*
                        try
                        {
                            //fuseTime - timeSinceIgnited
                            double explodeTics = (Integer)getInstanceField(TargetEntity, "field_82225_f") - (Integer)getInstanceField(TargetEntity, "field_70833_d");
                            String explodeSecs = MathHelper.roundWithTrail(explodeTics / 20.0, 2);
                            
                            rightStatistics[rStatIndex] = new RenderedLine("\u00A74Explodes in: \u00A7b" + explodeSecs + " s"); rStatIndex ++;
                        }
                        catch (Throwable e) {}
                        */
                    }
                    else if(TargetEntity instanceof AbstractHorse)
                    {
                        AbstractHorse horseEntity = (AbstractHorse)TargetEntity;
                        boolean horseIsTame = horseEntity.isTamed();
                        
                        rightStatistics[rStatIndex] = new RenderedLine("Tame:  " + (horseIsTame ? "\u00A7aYes" : "\u00A7cNo")); rStatIndex ++;
                        
                        if(horseIsTame == true) {
                            double jumpStrength = horseEntity.getCustomJump();
                            double jumpHeight = -0.1817584952D * Math.pow(jumpStrength, 3.0D) + 3.689713992D * Math.pow(jumpStrength, 2.0D) + 2.128599134D * jumpStrength - 0.343930367D;
                            
                            rightStatistics[rStatIndex] = new RenderedLine("Speed: \u00A7a" + MathHelper.round(horseEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 43.1718071434983D, 2) + " m/s"); rStatIndex ++;
                            rightStatistics[rStatIndex] = new RenderedLine("Jump:   \u00A7a" + MathHelper.round(jumpHeight, 2) + " m"); rStatIndex ++;
                        }
                    }
                }
                else if(crosshairObj != null
                         && crosshairObj.getType() == HitResult.Type.BLOCK)
                {
                    BlockPos blockpos = ((BlockHitResult)crosshairObj).getBlockPos();
                    if(blockpos != null)
                    {
                        BlockState blockstate = minecraft.level.getBlockState(blockpos);
                        Block block = blockstate.getBlock();
                        if(block instanceof CropBlock)
                        {
                            CropBlock crop = (CropBlock)block;
                            String cropName = "<error>";
                            int cropAge = -1;
                            int cropMaxAge = crop.getMaxAge();
                            float growthChance = -1.0f;
                            
                            try
                            {
                                cropName = ((ItemLike)getInstanceMethod(crop, "m_6404_")).asItem().getDefaultInstance().getHoverName().getString();
                            } catch(Throwable e) {}
                            try
                            {
                                if(block.getClass().equals(CropBlock.class))
                                {
                                    cropAge = (Integer)getInstanceMethod(crop, "m_52305_", new Class<?>[] {BlockState.class}, new Object[] {blockstate});
                                }
                                else
                                {
                                    cropAge = (Integer)getSuperInstanceMethod(crop, "m_52305_", new Class<?>[] {BlockState.class}, new Object[] {blockstate});
                                }
                                
                            } catch(Throwable e) {}
                            try
                            {
                                if(block.getClass().equals(CropBlock.class))
                                {
                                    growthChance = (Float)getInstanceMethod(crop, "m_52272_", new Class<?>[] {Block.class, BlockGetter.class, BlockPos.class}, new Object[] {block, minecraft.level, blockpos});
                                }
                                else
                                {
                                    growthChance = (Float)getSuperInstanceMethod(crop, "m_52272_", new Class<?>[] {Block.class, BlockGetter.class, BlockPos.class}, new Object[] {block, minecraft.level, blockpos});
                                }
                            } catch(Throwable e) {}
                            
                            
                            if(hasToolStatistics)
                            {
                                rightStatistics[rStatIndex] = new RenderedLine(" "); rStatIndex ++;
                                rightStatistics[rStatIndex] = new RenderedLine(" "); rStatIndex ++;
                            }
                            
                            rightStatistics[rStatIndex] = new RenderedLine("\u00A72Crop"); rStatIndex ++;
                            rightStatistics[rStatIndex] = new RenderedLine("\u00A72----"); rStatIndex ++;
                            rightStatistics[rStatIndex] = new RenderedLine("Name:       \u00A7b" + cropName); rStatIndex ++;
                            rightStatistics[rStatIndex] = new RenderedLine((cropAge >= 0 ? ("Stage:      \u00A7b" + cropAge + "/" + cropMaxAge) : "")); rStatIndex ++;
                            rightStatistics[rStatIndex] = new RenderedLine((growthChance >= 0.0f ? ("G. chance: \u00A7b" +  MathHelper.round((double)growthChance, 2) + "%") : "")); rStatIndex ++;
                        }
                    }
                }
            }
            
            if(rStatIndex > 0)
                Drawing.DrawTextLines(rightStatistics, window, event.getPoseStack(), 1, 65536, height / 2, 0xFFFFFF, GlobalProperties.listScale, true);
        }
    }
    
    public static Object getInstanceField(Object instance, String fieldName) throws Throwable
    {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
    
    public static Object getInstanceMethod(Object instance, String methodName) throws Throwable
    {
        return getInstanceMethod(instance, methodName, new Class<?>[] {}, new Object[] {});
    }
    
    public static Object getInstanceMethod(Object instance, String methodName, Class<?>[] argTypes, Object[] args) throws Throwable
    {
        Method method = instance.getClass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(instance, args);
    }
    
    public static Object getSuperInstanceMethod(Object instance, String methodName, Class<?>[] argTypes, Object[] args) throws Throwable
    {
        Method method = instance.getClass().getSuperclass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(instance, args);
    }
}
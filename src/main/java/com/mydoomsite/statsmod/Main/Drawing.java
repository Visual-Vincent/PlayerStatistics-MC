package com.mydoomsite.statsmod.Main;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mydoomsite.statsmod.lib.TextMetrics;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Drawing
{
	private static Minecraft minecraft = Minecraft.getInstance();

	private static TextMetrics getTextMetrics(RenderedLine[] lines)
	{
		int linesToDraw = 0;
		int textWidth = 0;
		
		for(int lineIndex = 0; lineIndex < lines.length; lineIndex++)
		{
			if(lines[lineIndex] == null)
				break;
			
			if(lines[lineIndex].Text.length() <= 0)
				continue;
			
			linesToDraw++;
			
			int lineWidth = minecraft.font.width(lines[lineIndex].Text);
			if(lineWidth > textWidth) {
				textWidth = lineWidth;
			}
		}
		
		return new TextMetrics(linesToDraw, textWidth);
	}
	
	public static void DrawTextLines(RenderedLine[] lines, Window window, PoseStack matrixStack, int lineOffset, int x, int y, int color, float scale, boolean shadow)
	{
		if(scale < 0.1F)
			scale = 1.0F;
		
		int fontHeight = (int)(minecraft.font.lineHeight * scale);
		TextMetrics metrics = getTextMetrics(lines);
		int linesToDraw = metrics.LinesToDraw;
		int linesSkipped = 0;
		int totalHeight = (linesToDraw * fontHeight) + ((linesToDraw - 1) * lineOffset);
		
		if(x == 65536) {
			x = (int)((window.getGuiScaledWidth() - 4) - (metrics.TextWidth * scale));
		}
		
		int startY = y - (totalHeight / 2);
		
		matrixStack.scale(scale, scale, scale);
		for(int lineIndex = 0; lineIndex < lines.length; lineIndex++)
		{
			if(lines[lineIndex] == null)
				break;
			
			if(lines[lineIndex].Text.length() == 0) {
				linesSkipped++;
				continue;
			}
			
			int lineY = startY + ((fontHeight * (lineIndex - linesSkipped)) + lineOffset);
			DrawText(matrixStack, lines[lineIndex].Text, x*(1 / scale), lineY*(1 / scale), color | (lines[lineIndex].Alpha << 24), shadow);
		}
		matrixStack.scale(1.0f / scale, 1.0f / scale, 1.0f / scale);
	}
	
	public static void DrawCenteredText(RenderedLine text, Window window, PoseStack matrixStack, int xOffset, int y, int color, float scale, boolean shadow)
	{
		int width = window.getGuiScaledWidth();
		int sWidth = minecraft.font.width(text.Text);
		int x = (width / 2) - ((int)(sWidth * scale) / 2) + xOffset;

		matrixStack.scale(scale, scale, scale);
		DrawText(matrixStack, text.Text, x*(1 / scale), y*(1 / scale), color | (text.Alpha << 24), shadow);
		matrixStack.scale(1.0f / scale, 1.0f / scale, 1.0f / scale);
	}
	
	public static void DrawText(PoseStack matrixStack, String text, float x, float y, int color, boolean shadow)
	{
		if(shadow)
			minecraft.font.drawShadow(matrixStack, text, x, y, color);
		
		minecraft.font.draw(matrixStack, text, x, y, color);
	}
	
	public static void DrawTextScaled(PoseStack matrixStack, String text, float x, float y, int color, float scale, boolean shadow)
	{
		matrixStack.scale(scale, scale, scale);
		DrawText(matrixStack, text, x, y, color, shadow);
		matrixStack.scale(1.0f / scale, 1.0f / scale, 1.0f / scale);
	}
	
	public static void sendChatMessageToClient(String message)
	{
		minecraft.player.displayClientMessage(Component.literal("\u00A7l\u00A7e[Statistics List]\u00A7r " + message), false); // false = Display in chat, true = Display above status bar
	}
}

package com.mydoomsite.statsmod.Main;

public class RenderedLine
{
	public String Text;
	public byte Alpha;
	
	public RenderedLine(String Text)
	{
		this.Text = Text;
		this.Alpha = (byte)255;
	}
	
	public RenderedLine(String Text, byte Alpha)
	{
		this.Text = Text;
		this.Alpha = Alpha;
	}
	
	public RenderedLine(String Text, int Alpha)
	{
		this.Text = Text;
		this.Alpha = (byte)Alpha;
	}
}

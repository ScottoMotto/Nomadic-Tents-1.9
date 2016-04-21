package com.yurtmod.dimension;

import com.yurtmod.dimension.StructureHelper.StructureType;
import com.yurtmod.main.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TentTeleporter extends Teleporter
{	
	private StructureType structure;
	private int yurtCornerX, yurtCornerY, yurtCornerZ;
	private double prevX, prevY, prevZ;
	private int prevDimID;
	private WorldServer worldServer;


	public TentTeleporter(int dimensionFrom, WorldServer worldTo, int cornerX, int cornerY, int cornerZ, double oldX, double oldY, double oldZ, StructureType type) 
	{
		super(worldTo);
		this.prevDimID = dimensionFrom;
		this.worldServer = worldTo;
		this.yurtCornerX = cornerX;
		this.yurtCornerY = cornerY;
		this.yurtCornerZ = cornerZ;
		this.prevX = oldX;
		this.prevY = oldY;
		this.prevZ = oldZ;
		this.structure = type;
	}

	@Override
	public void placeInPortal(Entity entity, float rotationYaw)
	{
		if(entity instanceof EntityPlayer)
		{
			double entityX;
			double entityY;
			double entityZ;
			float yaw;
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if(worldServer.provider.getDimension() == Config.DIMENSION_ID)
			{	
				entityX = this.yurtCornerX + 1.5D;
				entityY = this.yurtCornerY + 1.01D;
				entityZ = this.yurtCornerZ + this.structure.getDoorPosition() + 0.5D;
				yaw = -90F;
				// try to build a tent in that location (tent should check if it already exists)
				switch(this.structure)
				{
				case YURT_LARGE: case YURT_MEDIUM: case YURT_SMALL:
					new StructureYurt(this.structure).generateInTentDimension(prevDimID, worldServer, yurtCornerX, yurtCornerZ, prevX, prevY, prevZ);
					break;
				case TEPEE_LARGE: case TEPEE_MEDIUM: case TEPEE_SMALL:
					new StructureTepee(this.structure).generateInTentDimension(prevDimID, worldServer, yurtCornerX, yurtCornerZ, prevX, prevY, prevZ);
					break;
				default:
					StructureHelper.generatePlatform(worldServer, yurtCornerX, StructureHelper.FLOOR_Y - 3, yurtCornerZ, 8);
					System.out.println("Error: unhandled structure type resulted in empty platform");
					break;
				}
			}
			else
			{	
				entityX = this.prevX;
				entityY = this.prevY + 0.5D;
				entityZ = this.prevZ;
				yaw = entity.rotationYaw;
			}
			entity.setLocationAndAngles(entityX, entityY, entityZ, yaw, entity.rotationPitch);
		}
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, float f)
	{
		return true;
	}
	
	public String toString()
	{
		String out = "\n[TentTeleporter]\n";
		out += "structure=" + this.structure + "\n";
		out += "yurtCornerX=" + this.yurtCornerX + "\n";
		out += "yurtCornerZ=" + this.yurtCornerZ + "\n";
		out += "prevX=" + this.prevX + "\n";
		out += "prevY=" + this.prevY + "\n";
		out += "prevZ=" + this.prevZ + "\n";
		out += "prevDimID=" + this.prevDimID + "\n";
		return out;
	}
}
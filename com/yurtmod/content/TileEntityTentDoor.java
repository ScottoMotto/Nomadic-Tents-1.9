package com.yurtmod.content;

import com.yurtmod.dimension.StructureHelper;
import com.yurtmod.dimension.StructureHelper.StructureType;
import com.yurtmod.dimension.TentTeleporter;
import com.yurtmod.main.Config;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTentDoor extends TileEntity
{
	private static final String KEY_STRUCTURE_TYPE = "StructureTypeOrdinal";
	private static final String S_OFFSET_X = "TentOffsetX";
	private static final String S_OFFSET_Z = "TentOffsetZ";
	private static final String S_PLAYER_X = "PlayerPrevX";
	private static final String S_PLAYER_Y = "PlayerPrevY";
	private static final String S_PLAYER_Z = "PlayerPrevZ";
	private static final String S_PREV_DIM = "PreviousPlayerDimension";
	private StructureType structure;
	private int offsetX;
	private int offsetZ;	
	private double prevX, prevY, prevZ;
	private int prevDimID;
	
	public TileEntityTentDoor()
	{
		super();
		if(this.structure == null)
		{
			this.setStructureType(StructureType.YURT_SMALL);
		}
	}
	
	public TileEntityTentDoor(StructureType type)
	{
		super();
		this.structure = type;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		int ordinal = nbt.getInteger(KEY_STRUCTURE_TYPE);;
		this.structure = StructureType.values()[ordinal];
		this.offsetX = nbt.getInteger(S_OFFSET_X);
		this.offsetZ = nbt.getInteger(S_OFFSET_Z);
		this.prevX = nbt.getDouble(S_PLAYER_X);
		this.prevY = nbt.getDouble(S_PLAYER_Y);
		this.prevZ = nbt.getDouble(S_PLAYER_Z);
		this.prevDimID = nbt.getInteger(S_PREV_DIM);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger(KEY_STRUCTURE_TYPE, this.structure.ordinal());
		nbt.setInteger(S_OFFSET_X, this.offsetX);
		nbt.setInteger(S_OFFSET_Z, this.offsetZ);
		nbt.setDouble(S_PLAYER_X, prevX);
		nbt.setDouble(S_PLAYER_Y, prevY);
		nbt.setDouble(S_PLAYER_Z, prevZ);
		nbt.setInteger(S_PREV_DIM, this.getPrevDimension());
	}

	public void setStructureType(StructureType type) 
	{
		this.structure = type;
	}
	
	public StructureType getStructureType()
	{
		return this.structure;
	}

	public void setOffsetX(int toSet)
	{
		this.offsetX = toSet;
	}

	public int getOffsetX()
	{
		return this.offsetX;
	}

	public void setOffsetZ(int toSet)
	{
		this.offsetZ = toSet;
	}

	public int getOffsetZ()
	{
		return this.offsetZ;
	}

	public void setOverworldXYZ(double posX, double posY, double posZ)
	{
		this.prevX = posX;
		this.prevY = posY;
		this.prevZ = posZ;
	}
	
	public void setPrevDimension(int dimID)
	{
		this.prevDimID = dimID;
	}
	
	public int getPrevDimension()
	{
		return this.prevDimID;
	}

	public double[] getOverworldXYZ()
	{
		return new double[] {this.prevX, this.prevY, this.prevZ};
	}

	private int[] getXYZFromOffsets()
	{
		int x = this.offsetX * (StructureHelper.MAX_SQ_WIDTH);
		int y = StructureHelper.FLOOR_Y + 1;
		int z = this.offsetZ * (StructureHelper.MAX_SQ_WIDTH);
		return new int[] {x,y,z};
	}

	public boolean onPlayerActivate(EntityPlayer player)
	{
		if (!player.isRiding() && !player.isBeingRidden() && player.isNonBoss())
        {
            player.setPortal(pos);
			MinecraftServer mcServer = player.getServer();
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			// where the corresponding structure is in Tent dimension
			int[] corners = getXYZFromOffsets();
			int dimensionFrom = playerMP.worldObj.provider.getDimension();

			if(playerMP.timeUntilPortal > 0)
			{
				playerMP.timeUntilPortal = 10;
			}
			else if(playerMP.worldObj.provider.getDimension() != Config.DIMENSION_ID)
			{
				// remember the player's coordinates from the overworld
				this.setOverworldXYZ(playerMP.posX, playerMP.posY, playerMP.posZ);

				TentTeleporter tel = new TentTeleporter(
						dimensionFrom, mcServer.worldServerForDimension(Config.DIMENSION_ID), 
						corners[0], corners[1] - 1, corners[2],
						this.prevX, this.prevY, this.prevZ, this.structure);

				// teleport the player to Tent Dimension
				playerMP.timeUntilPortal = 10;	
				tel.placeInPortal(player, player.rotationYaw); // probably doesn't do it automatically...
				//mcServer.transferPlayerToDimension(playerMP, Config.DIMENSION_ID, tel);
				playerMP.changeDimension(Config.DIMENSION_ID); // probably has problems
				// debug:
				System.out.print("Created teleporter to Tent Dimension: " + tel.toString());
			}
			else if(playerMP.worldObj.provider.getDimension() == Config.DIMENSION_ID)
			{
				TentTeleporter tel = new TentTeleporter(
						dimensionFrom, mcServer.worldServerForDimension(this.getPrevDimension()), 
						corners[0], corners[1], corners[2],
						this.prevX, this.prevY, this.prevZ, this.structure);

				// teleport player to overworld
				playerMP.timeUntilPortal = 10;
				tel.placeInPortal(player, player.rotationYaw); // this probably doesn't work...
				//mcServer.transferPlayerToDimension(playerMP, this.getPrevDimension(), tel);
				// debug:
				System.out.print("Created teleporter to Overworld: " + tel.toString());
			}
			return true;
		}

		return false;
	}
}

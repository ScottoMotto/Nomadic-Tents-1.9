package com.yurtmod.dimension;

import com.yurtmod.content.Content;
import com.yurtmod.content.TileEntityTentDoor;
import com.yurtmod.dimension.StructureHelper.IYurtBlock;
import com.yurtmod.dimension.StructureHelper.StructureType;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureYurt
{
	public static final int WALL_HEIGHT = 3;

	/** Size of this particular yurt */
	private StructureType structure;

	public StructureYurt(StructureType type)
	{
		this.structure = type;
	}

	/**
	 * Allots a space for a sized yurt in the Tent Dimension.
	 * @param dimensionFrom the dimension the player left
	 * @param worldIn the world (in Yurt Dimension) to build in
	 * @param cornerX calculated by TileEntityYurtDoor
	 * @param cornerZ calculated by TileEntityYurtDoor
	 * @param prevX the players x-pos before teleporting to Yurt
	 * @param prevY the players y-pos before teleporting to Yurt
	 * @param prevZ the players z-pos before teleporting to Yurt
	 **/
	public boolean generateInTentDimension(int dimensionFrom, World worldIn, int cornerX, int cornerZ, double prevX, double prevY, double prevZ)
	{
		// check if the rest of the yurt has already been generated
		int doorZ = cornerZ;
		BlockPos corner = new BlockPos(cornerX, StructureHelper.FLOOR_Y, cornerZ);
		boolean ret = true;
		if(StructureHelper.generatePlatform(worldIn, corner, this.structure.getSqWidth()))
		{
			// make the yurt
			BlockPos doorPos = new BlockPos(cornerX, StructureHelper.FLOOR_Y + 1, cornerZ + this.structure.getDoorPosition());
			switch(this.structure)
			{
			case YURT_SMALL:
				ret = generateSmallInDimension(worldIn, doorPos, StructureHelper.STRUCTURE_DIR);
				break;
			case YURT_MEDIUM:
				ret = generateMedInDimension(worldIn, doorPos, StructureHelper.STRUCTURE_DIR);
				break;
			case YURT_LARGE:
				ret = generateLargeInDimension(worldIn, doorPos, StructureHelper.STRUCTURE_DIR);
				break;
			default: break;
			}			
		}

		// set tile entity door information
		if(ret)
		{
			doorZ = cornerZ + this.structure.getDoorPosition();
			BlockPos tepos = new BlockPos(cornerX, StructureHelper.FLOOR_Y + 1, doorZ);
			TileEntity te = worldIn.getTileEntity(tepos);
			if(te != null && te instanceof TileEntityTentDoor)
			{
				TileEntityTentDoor tetd = (TileEntityTentDoor)te;
				int[] offsets = StructureHelper.getChunkOffsetsFromXZ(cornerX, cornerZ);
				tetd.setStructureType(this.structure);
				tetd.setOffsetX(offsets[0]);
				tetd.setOffsetZ(offsets[1]);
				tetd.setOverworldXYZ(prevX, prevY, prevZ);
				tetd.setPrevDimension(dimensionFrom);
				// debug:
				//System.out.println("OverworldXYZ = " + overworldX + "," + overworldY + "," + overworldZ);
				return ret;
			}
			else System.out.println("Error! Failed to get tile entity at " + tepos);
		}
		return false;
	}

	/** (Helper function) Warning: does not check canSpawnSmallYurt before generating */
	public static boolean generateSmallInOverworld(World worldIn, BlockPos doorBase, Block door, int dirForward)
	{
		return generateSmall(worldIn, doorBase, dirForward, door, Content.yurtWallFrame, Content.yurtRoofFrame, Blocks.air, false);
	}

	/** Helper function */
	public static boolean deleteSmall(World worldIn, BlockPos doorBase, int dirForward)
	{
		boolean flag = generateSmall(worldIn, doorBase, dirForward, Blocks.air, Blocks.air, Blocks.air, Blocks.air, false);
		if(worldIn.getTileEntity(doorBase) instanceof TileEntityTentDoor)
		{
			worldIn.removeTileEntity(doorBase);
		}
		if(worldIn.getTileEntity(doorBase.up(1)) instanceof TileEntityTentDoor)
		{
			worldIn.removeTileEntity(doorBase.up(1));
		}
		return flag;
	}
	
	/** (Helper function) Warning: does not check canSpawnSmallYurt before generating */
	public static boolean generateSmallInDimension(World worldIn, BlockPos doorBase, int dirForward)
	{
		return generateSmall(worldIn, doorBase, dirForward, Content.yurtDoorSmall, Content.yurtInnerWall, Content.yurtRoof, Content.barrier, true);
	}
	
	/** Warning: does not check canSpawnSmallYurt before generating */
	public static boolean generateSmall(World worldIn, BlockPos door, int dirForward, Block doorBlock, Block wallBlock, Block roofBlock, Block barrier, boolean hasNegativeFloor)
	{	
		if(!worldIn.isRemote)
		{
			// make layer 1
			StructureHelper.buildLayer(worldIn, door, dirForward, wallBlock, StructureHelper.yurtWallsSmall, WALL_HEIGHT);
			// make layer 2
			StructureHelper.buildLayer(worldIn, door.up(WALL_HEIGHT), dirForward, roofBlock, StructureHelper.yurtRoof1Small, 1);
			// place barrier block
			BlockPos pos = StructureHelper.getPosFromDoor(door, StructureHelper.yurtBarrierSmall[0], StructureHelper.yurtBarrierSmall[1], dirForward);
			// add last middle piece of roof
			worldIn.setBlockState(pos.up(WALL_HEIGHT + 1), barrier.getDefaultState());
			// refine platform
			if(hasNegativeFloor)
			{
				StructureHelper.refinePlatform(worldIn, door, StructureHelper.yurtWallsSmall);
			}
			// make door
			worldIn.setBlockState(door, doorBlock.getStateFromMeta(0), 3);
			worldIn.setBlockState(door.up(1), doorBlock.getStateFromMeta(1), 3);
			return true;
		}
		return false;
	}

	/** Helper function */
	public static boolean generateMedInDimension(World worldIn, BlockPos doorPos, int dirForward)
	{
		return generateMedium(worldIn, doorPos, dirForward, Content.yurtInnerWall, Content.yurtRoof, Content.barrier, true);
	}

	public static boolean generateMedium(World worldIn, BlockPos door, int dirForward, Block wallBlock, Block roofBlock, Block barrier, boolean hasNegativeFloor)
	{
		if(!worldIn.isRemote)
		{
			// make layer 1
			StructureHelper.buildLayer(worldIn, door, dirForward, wallBlock, StructureHelper.yurtWallsMed, WALL_HEIGHT);
			// make layer 2
			StructureHelper.buildLayer(worldIn, door.up(WALL_HEIGHT), dirForward, roofBlock, StructureHelper.yurtRoof1Med, 1);
			// make layer 3
			StructureHelper.buildLayer(worldIn, door.up(WALL_HEIGHT + 1), dirForward, roofBlock, StructureHelper.yurtRoof2Med, 1);
			// place barrier block
			BlockPos pos = StructureHelper.getPosFromDoor(door, StructureHelper.yurtBarrierMed[0], StructureHelper.yurtBarrierMed[1], dirForward);
			// add last middle piece of roof
			worldIn.setBlockState(pos.up(WALL_HEIGHT + 2), barrier.getDefaultState());
			// refine platform
			if(hasNegativeFloor)
			{
				StructureHelper.refinePlatform(worldIn, door, StructureHelper.yurtWallsMed);
			}
			// make door
			worldIn.setBlockState(door, Content.yurtDoorMed.getStateFromMeta(0), 3);
			worldIn.setBlockState(door.up(1), Content.yurtDoorMed.getStateFromMeta(1), 3);
			return true;
		}
		return false;
	}

	/** Helper function */
	public static boolean generateLargeInDimension(World worldIn, BlockPos door, int dirForward)
	{
		boolean flag = generateLarge(worldIn, door, dirForward, Content.yurtInnerWall, Content.yurtRoof, Content.barrier, true);
		StructureHelper.buildFire(worldIn, Blocks.netherrack, door.down(1).east(4));
		return flag;
	}

	public static boolean generateLarge(World worldIn, BlockPos door, int dirForward, Block wallBlock, Block roofBlock, Block barrier, boolean hasNegativeFloor)
	{
		if(!worldIn.isRemote)
		{
			// make layer 1
			StructureHelper.buildLayer(worldIn, door, dirForward, wallBlock, StructureHelper.yurtWallsLarge, WALL_HEIGHT);
			// make layer 2
			StructureHelper.buildLayer(worldIn, door.up(WALL_HEIGHT), dirForward, roofBlock, StructureHelper.yurtRoof1Large, 1);
			// make layer 3
			StructureHelper.buildLayer(worldIn, door.up(WALL_HEIGHT + 1), dirForward, roofBlock, StructureHelper.yurtRoof2Large, 1);
			// make layer 4
			StructureHelper.buildLayer(worldIn, door.up(WALL_HEIGHT + 2), dirForward, roofBlock, StructureHelper.yurtRoof3Large, 1);
			// place barrier block
			BlockPos pos = StructureHelper.getPosFromDoor(door, StructureHelper.yurtBarrierLarge[0], StructureHelper.yurtBarrierLarge[1], dirForward);
			// add last middle piece of roof
			worldIn.setBlockState(pos.up(WALL_HEIGHT + 3), barrier.getDefaultState());
			// refine platform
			if(hasNegativeFloor)
			{
				StructureHelper.refinePlatform(worldIn, door, StructureHelper.yurtWallsLarge);
			}
			// make door
			worldIn.setBlockState(door, Content.yurtDoorLarge.getStateFromMeta(0), 3);
			worldIn.setBlockState(door.up(1), Content.yurtDoorLarge.getStateFromMeta(1), 3);
			return true;
		}
		return false;
	}

	public static boolean canSpawnSmallYurt(World worldIn, BlockPos door, int dirForward)
	{
		BlockPos pos = door;
		// check outer walls
		for(int layer = 0; layer < WALL_HEIGHT; layer++)
		{
			for(int[] coord : StructureHelper.yurtWallsSmall)
			{
				pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dirForward);
				if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(layer)))
				{
					return false;
				}
			}
		}
		// check most of roof
		for(int[] coord : StructureHelper.yurtRoof1Small)
		{
			pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dirForward);
			if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(WALL_HEIGHT)))
			{
				return false;
			}
		}
		pos = StructureHelper.getPosFromDoor(door, StructureHelper.yurtBarrierLarge[0], StructureHelper.yurtBarrierLarge[1], dirForward);
		// check last middle piece of roof
		if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(WALL_HEIGHT + 3)))
		{
			return false;
		}
		return true;
	}
	
	/** Returns -1 if not valid. Returns direction if is valid: 0=SOUTH=z++; 1=WEST=x--; 2=NORTH=z--; 3=EAST=x++  */
	public static int isValidSmallYurt(World worldIn, BlockPos doorBase) 
	{
		return isValidSmallYurt(worldIn, doorBase.getX(), doorBase.getY(), doorBase.getZ());
	}

	/** Returns -1 if not valid. Returns direction if is valid: 0=SOUTH=z++; 1=WEST=x--; 2=NORTH=z--; 3=EAST=x++  */
	public static int isValidSmallYurt(World worldIn, int doorX, int doorY, int doorZ)
	{
		BlockPos door = new BlockPos(doorX, doorY, doorZ);
		BlockPos pos = door;
		// check each direction
		for(int dir = 0; dir < 4; dir++)
		{
			boolean isValid = true;
			for(int layer = 0; layer < WALL_HEIGHT; layer++)
			{
				for(int[] coord : StructureHelper.yurtWallsSmall)
				{
					pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dir);
					Block at = worldIn.getBlockState(pos.up(layer)).getBlock();
					if(isValid && !(at instanceof IYurtBlock))
					{
						isValid = false;
					}
				}			
			}
			// check most of roof
			for(int[] coord : StructureHelper.yurtRoof1Small)
			{
				pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dir);
				Block at = worldIn.getBlockState(pos.up(WALL_HEIGHT)).getBlock();
				if(isValid && !(at instanceof IYurtBlock))
				{
					isValid = false;
				}
			}
			// check the last bit of roof
			//pos = StructureHelper.getPosFromDoor(door, StructureHelper.yurtBarrierSmall[0], StructureHelper.yurtBarrierSmall[1], dir);
			//Block at = worldIn.getBlockState(pos.up(WALL_HEIGHT + 1)).getBlock();
			//if(isValid && !(at instanceof IYurtBlock))
			//{
			//	isValid = false;
			//}

			// if it passed all the checks, it's a valid yurt
			if(isValid) return dir;
		}
		return -1;
	}
}

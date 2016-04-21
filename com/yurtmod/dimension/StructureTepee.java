package com.yurtmod.dimension;

import com.yurtmod.content.Content;
import com.yurtmod.content.TileEntityTentDoor;
import com.yurtmod.dimension.StructureHelper.ITepeeBlock;
import com.yurtmod.dimension.StructureHelper.StructureType;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureTepee 
{
	public static final int LAYER_DEPTH = 2;
	
	private final StructureType structure;
		
	public StructureTepee(StructureType type)
	{
		this.structure = type;
	}
	
	/**
	 * Allots a space for a sized tepee in the Tent Dimension.
	 * @param prevDimension the dimension id the player is leaving
	 * @param worldIn the world (in Tent Dimension) to build in
	 * @param cornerX calculated by TileEntityYurtDoor
	 * @param cornerZ calculated by TileEntityYurtDoor
	 * @param prevX the players x-pos before teleporting to Tepee
	 * @param prevY the players y-pos before teleporting to Tepee
	 * @param prevZ the players z-pos before teleporting to Tepee
	 **/
	public boolean generateInTentDimension(int prevDimension, World worldIn, int cornerX, int cornerZ, double prevX, double prevY, double prevZ)
	{
		// debug:
		// System.out.println("generating in dimension " + worldIn.provider.getDimensionId() + "; cornerX=" + cornerX + "; cornerZ=" + cornerZ);
		// check if the rest of the yurt has already been generated
		BlockPos corner = new BlockPos(cornerX, StructureHelper.FLOOR_Y, cornerZ);
		int doorZ = cornerZ;
		boolean ret = true;
		if(StructureHelper.generatePlatform(worldIn, corner, this.structure.getSqWidth()))
		{
			BlockPos doorPos = new BlockPos(cornerX, StructureHelper.FLOOR_Y + 1, cornerZ + this.structure.getDoorPosition());
			// make the yurt
			switch(this.structure)
			{
			case TEPEE_SMALL:
				ret = this.generateSmallInDimension(worldIn, doorPos);
				break;
			case TEPEE_MEDIUM:
				ret = this.generateMedInDimension(worldIn, doorPos);
				break;
			case TEPEE_LARGE:
				ret = this.generateLargeInDimension(worldIn, doorPos);
				break;
			default: break;
			}			
		}

		// set tile entity door information
		if(ret)
		{
			doorZ = cornerZ + this.structure.getDoorPosition();
			TileEntity te = worldIn.getTileEntity(new BlockPos(cornerX, StructureHelper.FLOOR_Y + 1, doorZ));
			if(te != null && te instanceof TileEntityTentDoor)
			{
				TileEntityTentDoor teyd = (TileEntityTentDoor)te;
				int[] offsets = StructureHelper.getChunkOffsetsFromXZ(cornerX, cornerZ);
				teyd.setStructureType(this.structure);
				teyd.setOffsetX(offsets[0]);
				teyd.setOffsetZ(offsets[1]);
				teyd.setOverworldXYZ(prevX, prevY, prevZ);
				teyd.setPrevDimension(prevDimension);
				// debug:
				//System.out.println("OverworldXYZ = " + overworldX + "," + overworldY + "," + overworldZ);
				return ret;
			}
			else System.out.println("Error! Failed to retrive TileEntityYurtDoor at " + cornerX + ", " + (StructureHelper.FLOOR_Y + 1) + ", " + doorZ);
		}
		return false;
	}

	private static boolean generateSmallInDimension(World worldIn, BlockPos doorBase) 
	{
		return generateSmall(worldIn, doorBase, StructureHelper.STRUCTURE_DIR, Content.tepeeDoorSmall, Content.tepeeWall, Content.barrier, true);
	}
	
	/** (Helper function) Warning: does not check canSpawnSmallTepee before generating */
	public static boolean generateSmallInOverworld(World worldIn, BlockPos doorBase, Block door, int dirForward)
	{
		return generateSmall(worldIn, doorBase, dirForward, door, Content.tepeeWallFrame, Blocks.air, false);
	}

	public static boolean deleteSmall(World worldIn, BlockPos pos, int dirForward)
	{
		boolean flag = generateSmall(worldIn, pos, dirForward, Blocks.air, Blocks.air, Blocks.air, false);
		if(worldIn.getTileEntity(pos) instanceof TileEntityTentDoor)
		{
			worldIn.removeTileEntity(pos);
		}
		if(worldIn.getTileEntity(pos.up(1)) instanceof TileEntityTentDoor)
		{
			worldIn.removeTileEntity(pos.up(1));
		}
		return flag;
	}

	public static boolean generateMedInDimension(World worldIn, BlockPos doorBase) 
	{
		return generateMedium(worldIn, doorBase, StructureHelper.STRUCTURE_DIR, Content.tepeeDoorMed, Content.tepeeWall, Content.barrier, true);
	}

	public static boolean generateLargeInDimension(World worldIn, BlockPos doorBase) 
	{
		boolean flag = generateLarge(worldIn, doorBase, StructureHelper.STRUCTURE_DIR, Content.tepeeDoorLarge, Content.tepeeWall, Content.barrier, true);
		StructureHelper.buildFire(worldIn, Blocks.netherrack, doorBase.down(1).east(4));
		return flag;
	}
	
	/** Warning: does not check canSpawnSmallYurt before generating */
	public static boolean generateSmall(World worldIn, BlockPos doorBase, int dirForward, Block doorBlock, Block wallBlock, Block barrier, boolean hasNegativeFloor)
	{	
		if(!worldIn.isRemote)
		{
			// debug:
			//System.out.println("generating Small Tepee");
			// make layer 1 and 2
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 0), dirForward, wallBlock, StructureHelper.tepeeLayer1Small);
			// make layer 3 and 4
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 1), dirForward, wallBlock, StructureHelper.tepeeLayer2Small);
			// make layer 5 and 6
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 2), dirForward, wallBlock, StructureHelper.tepeeLayer3Small);
			// place barrier block
			BlockPos pos = StructureHelper.getPosFromDoor(doorBase, StructureHelper.tepeeBarrierSmall[0], StructureHelper.tepeeBarrierSmall[1], dirForward);
			worldIn.setBlockState(pos.up(LAYER_DEPTH * 3), barrier.getDefaultState());
			// make dirt layer if required
			if(hasNegativeFloor && dirForward == StructureHelper.STRUCTURE_DIR)
			{
				StructureHelper.refinePlatform(worldIn, doorBase, StructureHelper.tepeeLayer1Small);
			}
			// make door
			worldIn.setBlockState(doorBase, doorBlock.getStateFromMeta(0), 3);
			worldIn.setBlockState(doorBase.up(1), doorBlock.getStateFromMeta(1), 3);
			return true;
		}
		return false;
	}
	
	public static boolean generateMedium(World worldIn, BlockPos doorBase, int dirForward, Block doorBlock, Block wallBlock, Block barrier, boolean hasNegativeFloor)
	{
		if(!worldIn.isRemote)
		{
			// debug:
			System.out.println("generating Medium Tepee");
			// make layer 1 and 2
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 0), dirForward, wallBlock, StructureHelper.tepeeLayer1Med);
			// make layer 3 and 4
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 1), dirForward, wallBlock, StructureHelper.tepeeLayer2Med);
			// make layer 5 and 6
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 2), dirForward, wallBlock, StructureHelper.tepeeLayer3Med);
			// place barrier block
			BlockPos pos = StructureHelper.getPosFromDoor(doorBase, StructureHelper.tepeeBarrierMed[0], StructureHelper.tepeeBarrierMed[1], dirForward);
			worldIn.setBlockState(pos.up(LAYER_DEPTH * 3), barrier.getDefaultState());
			// make dirt layer if required
			if(hasNegativeFloor && dirForward == StructureHelper.STRUCTURE_DIR)
			{
				StructureHelper.refinePlatform(worldIn, doorBase, StructureHelper.tepeeLayer1Med);
			}
			// make door
			worldIn.setBlockState(doorBase, doorBlock.getStateFromMeta(0), 3);
			worldIn.setBlockState(doorBase.up(1), doorBlock.getStateFromMeta(1), 3);
			return true;
		}
		return false;
	}
	
	public static boolean generateLarge(World worldIn, BlockPos doorBase, int dirForward, Block doorBlock, Block wallBlock, Block barrier, boolean hasNegativeFloor)
	{
		if(!worldIn.isRemote)
		{
			// debug:
			System.out.println("generating Large Tepee");
			// make layer 1 and 2
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 0), dirForward, wallBlock, StructureHelper.tepeeLayer1Large);
			// make layer 3 and 4
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 1), dirForward, wallBlock, StructureHelper.tepeeLayer2Large);
			// make layer 5 and 6
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 2), dirForward, wallBlock, StructureHelper.tepeeLayer3Large);
			// make layer 7 and 8
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 3), dirForward, wallBlock, StructureHelper.tepeeLayer4Large);
			// make layer 7 and 8
			StructureHelper.build2TepeeLayers(worldIn, doorBase.up(LAYER_DEPTH * 4), dirForward, wallBlock, StructureHelper.tepeeLayer5Large);
			// place barrier block
			BlockPos pos = StructureHelper.getPosFromDoor(doorBase, StructureHelper.tepeeBarrierLarge[0], StructureHelper.tepeeBarrierLarge[1], dirForward);
			worldIn.setBlockState(pos.up(LAYER_DEPTH * 5), barrier.getDefaultState());
			// make dirt layer if required
			if(hasNegativeFloor && dirForward == StructureHelper.STRUCTURE_DIR)
			{
				StructureHelper.refinePlatform(worldIn, doorBase, StructureHelper.tepeeLayer1Large);
			}
			// make door
			worldIn.setBlockState(doorBase, doorBlock.getStateFromMeta(0), 3);
			worldIn.setBlockState(doorBase.up(1), doorBlock.getStateFromMeta(1), 3);
			return true;
		}
		return false;
	}
	
	public static boolean canSpawnSmallTepee(World worldIn, BlockPos door, int dirForward)
	{
		BlockPos pos = door;
		// check outer walls
		for(int layer = 0; layer < LAYER_DEPTH; layer++)
		{
			for(int[] coord : StructureHelper.tepeeLayer1Small)
			{
				pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dirForward);
				if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(layer)))
				{
					return false;
				}
			}
			for(int[] coord : StructureHelper.tepeeLayer2Small)
			{
				pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dirForward);
				if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(LAYER_DEPTH + layer)))
				{
					return false;
				}
			}
			for(int[] coord : StructureHelper.tepeeLayer3Small)
			{
				pos = StructureHelper.getPosFromDoor(door, coord[0], coord[1], dirForward);
				if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(layer + LAYER_DEPTH * 2)))
				{
					return false;
				}
			}
		}		
		// check barrier space
		pos = StructureHelper.getPosFromDoor(door, StructureHelper.tepeeBarrierSmall[0], StructureHelper.tepeeBarrierSmall[1], dirForward);
		if(!StructureHelper.isReplaceableMaterial(worldIn, pos.up(LAYER_DEPTH * 3)))
		{
			return false;
		}
		return true;
	}
	
	public static int isValidSmallTepee(World worldIn, BlockPos doorBase)
	{
		BlockPos pos;
		// check each direction
		loopCheckDirection:
		for(int dir = 0; dir < 4; dir++)
		{
			boolean isValid = true;
			for(int layer = 0; isValid && layer < LAYER_DEPTH; layer++)
			{
				// debug:
				//System.out.println("Checking layer1 for y = " + (doorBaseY + layer) + " for dir = " + dir + "... isValid = " + isValid);
				for(int[] coord : StructureHelper.tepeeLayer1Small)
				{
					pos = StructureHelper.getPosFromDoor(doorBase, coord[0], coord[1], dir);
					Block at = worldIn.getBlockState(pos.up(layer)).getBlock();
					if(isValid && !(at instanceof ITepeeBlock))
					{
						isValid = false;
						continue loopCheckDirection;
					}
				}			
			}
			for(int layer = 0; isValid && layer < LAYER_DEPTH; layer++)
			{
				for(int[] coord : StructureHelper.tepeeLayer2Small)
				{
					// debug:
					//System.out.println("Checking layer2 for y = " + (doorBaseY + layer + LAYER_HEIGHT) + " for dir = " + dir + "... isValid = " + isValid);
					pos = StructureHelper.getPosFromDoor(doorBase, coord[0], coord[1], dir);
					Block at = worldIn.getBlockState(pos.up(layer + LAYER_DEPTH)).getBlock();
					if(isValid && !(at instanceof ITepeeBlock))
					{
						isValid = false;
						continue loopCheckDirection;
					}
				}			
			}
			for(int layer = 0; isValid && layer < LAYER_DEPTH; layer++)
			{
				for(int[] coord : StructureHelper.tepeeLayer3Small)
				{
					// debug:
					//System.out.println("Checking layer2 for y = " + (doorBaseY + layer + LAYER_HEIGHT) + " for dir = " + dir + "... isValid = " + isValid);
					pos = StructureHelper.getPosFromDoor(doorBase, coord[0], coord[1], dir);
					Block at = worldIn.getBlockState(pos.up(layer + LAYER_DEPTH * 2)).getBlock();
					if(isValid && !(at instanceof ITepeeBlock))
					{
						isValid = false;
						continue loopCheckDirection;
					}
				}			
			}
			// debug:
			//System.out.println("isValid=" + isValid + "; dir=" + dir);
			// if it passed all the checks, it's a valid yurt
			if(isValid) return dir;
		}

		return -1;
	}

	public static int isValidSmallTepee(World worldIn, int doorX, int doorBaseY, int doorZ) 
	{
		return isValidSmallTepee(worldIn, new BlockPos(doorX, doorBaseY, doorZ));	
	}
}

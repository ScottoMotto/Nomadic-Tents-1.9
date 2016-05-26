package com.yurtmod.structure;

import com.yurtmod.block.BlockTepeeWall;
import com.yurtmod.init.Content;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class StructureHelper 
{
	/** Structures are spaced this far apart for consistency and compatibility **/
	public static final int MAX_SQ_WIDTH = 16;
	/** Y-level for the floor of all tent structures in Tent Dimension **/
	public static final int FLOOR_Y = 70;
	/** Default facing for all tent structures in Tent Dimension **/
	public static final EnumFacing STRUCTURE_DIR = EnumFacing.EAST;
	

	/** Handles the structure type to call the correct {@code canSpawnHere} functions. Currently only handles *_SMALL **/
	public static boolean canSpawnStructureHere(World world, StructureType structure, BlockPos doorBase, EnumFacing dir)
	{
		switch(structure)
		{
		case TEPEE_LARGE: case TEPEE_MEDIUM: case TEPEE_SMALL:	return StructureTepee.canSpawnSmallTepee(world, doorBase, dir);
		case YURT_LARGE: case YURT_MEDIUM: case YURT_SMALL: 	return StructureYurt.canSpawnSmallYurt(world, doorBase, dir);
		case BEDOUIN_LARGE: case BEDOUIN_MEDIUM: case BEDOUIN_SMALL: return StructureBedouin.canSpawnSmallBedouin(world, doorBase, dir);
		}
		return false;
	}
	
	/** Handles the structure type to call the correct {@code isValidStructure} functions. Currently only handles *_SMALL **/
	public static EnumFacing isValidStructure(World world, StructureType structure, BlockPos doorBase)
	{
		switch(structure)
		{
		case TEPEE_LARGE: case TEPEE_MEDIUM: case TEPEE_SMALL:	return StructureTepee.isValidSmallTepee(world, doorBase);
		case YURT_LARGE: case YURT_MEDIUM: case YURT_SMALL: 	return StructureYurt.isValidSmallYurt(world, doorBase);
		case BEDOUIN_LARGE: case BEDOUIN_MEDIUM: case BEDOUIN_SMALL: return StructureBedouin.isValidSmallBedouin(world, doorBase);
		}
		return null;
	}
	
	/** Handles the structure type to call the correct {@code generateSmallInOverworld} function **/
	public static boolean generateSmallStructureOverworld(World world, StructureType structure, BlockPos doorBase, EnumFacing dir)
	{
		Block door = structure.getDoorBlock();
		world.scheduleUpdate(doorBase.up(1), door, 2);
		Chunk c = world.getChunkFromBlockCoords(doorBase);
		// debug:
		System.out.println("chunk at coords " + c.xPosition + ", " + c.zPosition + " based on " + doorBase);
		c.setChunkModified();
		switch(structure)
		{
		case TEPEE_LARGE: case TEPEE_MEDIUM: case TEPEE_SMALL:	return StructureTepee.generateSmallInOverworld(world, doorBase, door, dir);
		case YURT_LARGE: case YURT_MEDIUM: case YURT_SMALL: 	return StructureYurt.generateSmallInOverworld(world, doorBase, door, dir);
		case BEDOUIN_LARGE: case BEDOUIN_MEDIUM: case BEDOUIN_SMALL: return StructureBedouin.generateSmallInOverworld(world, doorBase, door, dir);
		}
		return false;
	}
	
	/** Handles the structure type to call the correct {@code generateSmallInOverworld} function **/
	public static boolean deleteSmallStructure(World world, StructureType structure, BlockPos doorBase, EnumFacing dir)
	{
		switch(structure)
		{
		case TEPEE_LARGE: case TEPEE_MEDIUM: case TEPEE_SMALL:	return StructureTepee.deleteSmall(world, doorBase, dir);
		case YURT_LARGE: case YURT_MEDIUM: case YURT_SMALL: 	return StructureYurt.deleteSmall(world, doorBase, dir);
		case BEDOUIN_LARGE: case BEDOUIN_MEDIUM: case BEDOUIN_SMALL: return StructureBedouin.deleteSmall(world, doorBase, dir);
		}
		return false;
	}
	
	public static boolean isReplaceableMaterial(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		return isReplaceableMaterial(state.getMaterial()) && !state.getBlock().hasTileEntity(state);
	}
	
	/** Structure blocks are allowed to replace blocks of these materials */
	public static boolean isReplaceableMaterial(Material m)
	{
		return m == Material.air || m == Material.plants 
			|| m == Material.lava || m == Material.water 
			|| m == Material.leaves || m == Material.snow 
			|| m == Material.vine;
	}
	
	/** Fill the locations given by an array {{x1,z1}} with given block and given metadata **/
	public static void buildLayer(World worldIn, BlockPos door, EnumFacing dirForward, Block block, int metadata, int[][] coordinates, int numLayers)
	{
		for(int layer = 0; layer < numLayers; layer++)
		{
			for(int[] coord : coordinates)
			{
				BlockPos pos = getPosFromDoor(door, coord[0], coord[1], dirForward).up(layer);
				// BUG #4: Client does not get notified of this block change, even with 3 as the flag.
				worldIn.setBlockState(pos, block.getStateFromMeta(metadata), 2);
				// try to force blocks to show on client... hoped this would fix bug #4, but no luck
				//if(!TentDimension.isTentDimension(worldIn))
				{
					//worldIn.scheduleUpdate(pos, block, 1 + worldIn.rand.nextInt(10));
					//worldIn.scheduleBlockUpdate(pos, block, 1 + worldIn.rand.nextInt(20), 0);
				}
			}
		}
	}
	
	/** Fill the locations given by an array {{x1,z1}} with given block and default metadata **/
	public static void buildLayer(World worldIn, BlockPos door, EnumFacing dirForward, Block block, int[][] coordinates, int numLayers)
	{
		buildLayer(worldIn, door, dirForward, block, 0, coordinates, numLayers);
	}

	public static void build2TepeeLayers(World worldIn, BlockPos pos, EnumFacing dirForward, Block wallBlock, int[][] coordinates)
	{
		int meta = wallBlock instanceof BlockTepeeWall ? BlockTepeeWall.getMetaForRandomPattern(worldIn.rand) : 0;
		buildLayer(worldIn, pos, dirForward, wallBlock, meta, coordinates, 1);
		meta = 0;
		buildLayer(worldIn, pos.up(1), dirForward, wallBlock, meta, coordinates, 1);
	}
	
	/** Builds a 2-block-deep platform from (cornerX, cornerY - 1, cornerZ) 
	 * to (cornerX + sqWidth, cornerY, cornerZ + sqWidth), with the top layer
	 * regular dirt and the bottom layer indestructible dirt
	 **/
	public static boolean generatePlatform(World worldIn, int cornerX, int cornerY, int cornerZ, int sqWidth)
	{
		return generatePlatform(worldIn, new BlockPos(cornerX, cornerY, cornerZ), sqWidth);
	}
	
	/** Builds a 2-block-deep platform from (cornerX, cornerY - 1, cornerZ) 
	 * to (cornerX + sqWidth, cornerY, cornerZ + sqWidth), with the top layer
	 * regular dirt and the bottom layer indestructible dirt
	 **/
	public static boolean generatePlatform(World worldIn, BlockPos corner, int sqWidth)
	{
		if(worldIn.isAirBlock(corner) && worldIn.isAirBlock(corner.down(1)))
		{
			// make a base from corner x,y,z to +x,y,+z
			for(int i = 0; i < sqWidth; i++)
			{
				for(int j = 0; j < sqWidth; j++)
				{
					BlockPos at = new BlockPos(corner.getX() + i, corner.getY(), corner.getZ() + j);
					if(worldIn.isAirBlock(at)) worldIn.setBlockState(at, Blocks.dirt.getDefaultState(), 2);
					worldIn.setBlockState(at.down(1), Content.indestructibleDirt.getDefaultState(), 2);
				}
			}
			return true;
		}
		return false;
	}
	
	/** Places indestructible dirt just below all wall blocks **/
	public static void refinePlatform(World worldIn, BlockPos door, int[][] layer0)
	{
		for(int[] coord : layer0)
		{
			BlockPos at = door.add(coord[0], -1, coord[1]);
			worldIn.setBlockState(at, Content.indestructibleDirt.getDefaultState(), 2);
		}
	}
	
	public static void refinePlatform(World worldIn, int doorX, int doorY, int doorZ, int[][] layer0)
	{
		refinePlatform(worldIn, new BlockPos(doorX, doorY, doorZ), layer0);
	}
	
	/** Sets the passed coordinates to {@link fuel} and places fire above it **/
	public static void buildFire(World world, IBlockState fuel, IBlockState fire, BlockPos pos)
	{
		world.setBlockState(pos, fuel);
		world.setBlockState(pos.up(), Blocks.fire.getDefaultState(), 3);
	}
	
	/** helper method for {@link #buildFire(World, IBlockState, IBlockState, BlockPos)} **/
	public static void buildFire(World world, Block fuel, BlockPos pos)
	{
		buildFire(world, fuel.getDefaultState(), Blocks.fire.getDefaultState(), pos);
	}
	
	/** dirForward 0=SOUTH=z++; 1=WEST=x--; 2=NORTH=z--; 3=EAST=x++ */
	public static BlockPos getPosFromDoor(BlockPos doorPos, int disForward, int disRight, EnumFacing forward)
	{
		EnumFacing right = forward.rotateY();
		return doorPos.offset(forward, disForward).offset(right, disRight);
	}
	
	/** Adds passed values to the array. Requires an int[][] in format {{x1, z1}} **/
	private static int[][] getShiftedArray(int[][] original, int dX, int dZ)
	{
		int[][] clone = new int[original.length][2];
		for(int i = 0, len = original.length; i < len; i++)
		{
			clone[i][0] = original[i][0] + dX;
			clone[i][1] = original[i][1] + dZ;
		}
		return clone;
	}
	
	/** Calculates what chunk offset x, z to give a door or item **/
	public static int[] getChunkOffsetsFromXZ(int actualX, int actualZ)
	{
		int offsetX = actualX / (StructureHelper.MAX_SQ_WIDTH);
		int offsetZ = actualZ / (StructureHelper.MAX_SQ_WIDTH);
		return new int[] {offsetX, offsetZ};
	}
	
	////////////////////////////////////////////////////////
	//////////////////// BLUEPRINTS ////////////////////////
	////////////////////////////////////////////////////////
	
	/// format: {{numForward=dX, numRight=dZ}} from door ///
	
	/********************* SMALL YURT **********************/
	public static final int[][] yurtWallsSmall = new int[][]
	{
		{0,1},{0,0},{0,-1},{1,-2},{2,-2},{3,-2},{4,-1},{4,0},{4,1},{3,2},{2,2},{1,2}
	};
	public static final int[][] yurtRoofSmall = new int[][]
	{
		{1,1},{1,0},{1,-1},{2,-1},{3,-1},{3,0},{3,1},{2,1},{0,0},{2,-2},{4,0},{2,2}
	};
	// middle 1 block
	public static final int[] yurtBarrierSmall = new int[] {2,0};
	
	/******************** MEDIUM YURT **********************/
	public static final int[][] yurtWallsMed = new int[][]
	{
		{0,-1},{0,0},{0,1},{1,2},{2,3},{3,3},{4,3},{5,2},{6,1},{6,0},{6,-1},{5,-2},{4,-3},{3,-3},{2,-3},{1,-2}
	};	
	public static final int[][] yurtRoof1Med = new int[][]
	{
		{1,-1},{1,0},{1,1},{2,2},{3,2},{4,2},{5,1},{5,0},{5,-1},{4,-2},{3,-2},{2,-2}
	};
	public static final int[][] yurtRoof2Med = new int[][] 
	{
		{2,-1},{2,0},{2,1},{3,1},{4,1},{4,0},{4,-1},{3,-1}
	};
	public static final int[] yurtBarrierMed = new int[] {3,0};
	
	/******************** LARGE YURT **********************/
	public static final int[][] yurtWallsLarge = new int[][]
	{
		{0,-2},{0,-1},{0,0},{0,1},{0,2},{1,3},{2,4},{3,4},{4,4},{5,4},{6,4},{7,3},{8,2},{8,1},{8,0},{8,-1},{8,-2},
		{7,-3},{6,-4},{5,-4},{4,-4},{3,-4},{2,-4},{1,-3}
	};	
	public static final int[][] yurtRoof1Large = new int[][]
	{
		{1,-2},{1,-1},{1,0},{1,1},{1,2},{2,2},{2,3},{3,3},{4,3},{5,3},{6,3},{6,2},{7,2},{7,1},{7,0},
		{7,-1},{7,-2},{6,-2},{6,-3},{5,-3},{4,-3},{3,-3},{2,-3},{2,-2}
	};
	public static final int[][] yurtRoof2Large = new int[][] 
	{
		{2,-1},{2,0},{2,1},{3,1},{3,2},{4,2},{5,2},{5,1},{6,1},{6,0},{6,-1},{5,-1},{5,-2},{4,-2},{3,-2},{3,-1},
	};
	public static final int[][] yurtRoof3Large = new int[][]
	{
		{3,0},{4,1},{5,0},{4,-1}
	};
	public static final int[] yurtBarrierLarge = new int[] {4,0};
	
	/******************** SMALL TEPEE **********************/
	public static final int[][] tepeeLayer1Small = yurtWallsSmall;
	public static final int[][] tepeeLayer2Small = new int[][]
	{
		{1,-1},{1,0},{1,1},{2,1},{3,1},{3,0},{3,-1},{2,-1}
	};
	public static final int[][] tepeeLayer3Small = new int[][]
	{
		{1,0},{2,1},{3,0},{2,-1}
	};
	public static final int[] tepeeBarrierSmall = new int[] {2,0};
	
	/******************** MEDIUM TEPEE **********************/
	public static final int[][] tepeeLayer1Med = yurtWallsMed;
	public static final int[][] tepeeLayer2Med = getShiftedArray(tepeeLayer1Small, 1, 0);
	public static final int[][] tepeeLayer3Med = getShiftedArray(tepeeLayer2Small, 1, 0);
	public static final int[] tepeeBarrierMed = new int[] {3,0};
	
	/******************** LARGE TEPEE **********************/
	public static final int[][] tepeeLayer1Large = yurtWallsLarge;
	public static final int[][] tepeeLayer2Large = new int[][]
	{
		{0,-1},{0,0},{0,1},{1,2},{2,3},{3,4},{4,4},{5,4},{6,3},{7,2},{8,1},{8,0},{8,-1},{7,-2},{6,-3},{5,-4},{4,-4},{3,-4},{2,-3},{1,-2}
	};
	public static final int[][] tepeeLayer3Large = getShiftedArray(tepeeLayer1Med, 1, 0);
	public static final int[][] tepeeLayer4Large = getShiftedArray(tepeeLayer2Med, 1, 0);
	public static final int[][] tepeeLayer5Large = getShiftedArray(tepeeLayer3Med, 1, 0);
	public static final int[] tepeeBarrierLarge = new int[] {4,0};
	
	/********************* SMALL BEDOUIN *********************/
	public static final int[][] bedWallsSmall = yurtWallsSmall;
	public static final int[][] bedRoof1Small = new int[][]
	{
		{0,0},{1,1},{2,1},{3,1},{4,0},{1,-1},{2,-1},{3,-1}
	};
	public static final int[][] bedRoof2Small = new int[][]
	{
		{1,0},{2,0},{3,0}
	};
	/********************* MEDIUM BEDOUIN *********************/
	public static final int[][] bedWallsMed = new int[][]
	{
		{0,-2},{0,-1},{0,0},{0,1},{0,2},
		{1,3},{2,3},{3,3},{4,3},{5,3},
		{6,-2},{6,-1},{6,0},{6,1},{6,2},
		{1,-3},{2,-3},{3,-3},{4,-3},{5,-3}
		
	};
	public static final int[][] bedRoof1Med = new int[][]
	{
		{0,-1},{0,0},{0,1},{1,2},{2,2},{3,2},{4,2},{5,2},
		{6,-1},{6,0},{6,1},{1,-2},{2,-2},{3,-2},{4,-2},{5,-2}
	};
	public static final int[][] bedRoof2Med = new int[][]
	{
		{0,0},{1,1},{2,1},{3,1},{4,1},{5,1},
		{6,0},{1,-1},{2,-1},{3,-1},{4,-1},{5,-1}
	};
	public static final int[][] bedRoof3Med = new int[][]
	{
		{1,0},{2,0},{3,0},{4,0},{5,0}
	};
	/********************* LARGE BEDOUIN *********************/
	public static final int[][] bedWallsLarge = new int[][]
	{
		{0,-3},{0,-2},{0,-1},{0,0},{0,1},{0,2},{0,3},
		{1,4},{2,4},{3,4},{4,4},{5,4},{6,4},{7,4},
		{8,-3},{8,-2},{8,-1},{8,0},{8,1},{8,2},{8,3},
		{1,-4},{2,-4},{3,-4},{4,-4},{5,-4},{6,-4},{7,-4}
	};
	public static final int[][] bedRoof1Large = new int[][]
	{
		{0,-2},{0,-1},{0,0},{0,1},{0,2},
		{1,3},{2,3},{3,3},{4,3},{5,3},{6,3},{7,3},
		{8,-2},{8,-1},{8,0},{8,1},{8,2},
		{1,-3},{2,-3},{3,-3},{4,-3},{5,-3},{6,-3},{7,-3}
	};
	public static final int[][] bedRoof2Large = new int[][]
	{
		{0,-1},{0,0},{0,1},{8,-1},{8,0},{8,1},
		{1,2},{2,2},{3,2},{4,2},{5,2},{6,2},{7,2},
		{1,-2},{2,-2},{3,-2},{4,-2},{5,-2},{6,-2},{7,-2}
	};
	public static final int[][] bedRoof3Large = new int[][]
	{
		{0,0},{8,0},
		{1,1},{2,1},{3,1},{4,1},{5,1},{6,1},{7,1},
		{1,-1},{2,-1},{3,-1},{4,-1},{5,-1},{6,-1},{7,-1}
	};
	public static final int[][] bedRoof4Large = new int[][]
	{
		{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0}
	};
}

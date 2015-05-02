package org.spongepowered.common.mixin.core.world.gen.populators;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenFlowers;

import org.spongepowered.api.data.types.PlantType;
import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.util.weighted.WeightedCollection;
import org.spongepowered.api.util.weighted.WeightedObject;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Flowers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.flowpowered.math.vector.Vector3i;

@Mixin(WorldGenFlowers.class)
public abstract class MixinWorldGenFlowers implements Flowers {
	
	private WeightedCollection<WeightedObject<PlantType>> flowers;
	private VariableAmount count;
	
	@Shadow public abstract void setGeneratedBlock(BlockFlower p_175914_1_, BlockFlower.EnumFlowerType p_175914_2_);
	@Shadow public abstract boolean generate(World worldIn, Random rand, BlockPos position);

    @Inject(method = "<init>(Lnet/minecraft/block/BlockFlower;Lnet/minecraft/block/BlockFlower$EnumFlowerType;)V", at = @At("RETURN"))
    public void onConstructed(BlockFlower block, BlockFlower.EnumFlowerType type, CallbackInfo ci) {
        this.flowers = new WeightedCollection<WeightedObject<PlantType>>();
        this.count = VariableAmount.fixed(2);
    }

	@Override
	public void populate(Chunk chunk, Random random) {
		World world = (World) chunk.getWorld();
        Vector3i min = chunk.getBlockMin();
        BlockPos chunkPos = new BlockPos(min.getX(), min.getY(), min.getZ());
		
		int x, y, z;
		int n = this.count.getFlooredAmount(random);
		BlockPos blockpos;
		
		for (int i = 0; i < n; ++i) {
            x = random.nextInt(16) + 8;
            z = random.nextInt(16) + 8;
            y = random.nextInt(world.getHeight(chunkPos.add(x, 0, z)).getY() + 32);
            blockpos = chunkPos.add(x, y, z);
            BlockFlower.EnumFlowerType enumflowertype = (BlockFlower.EnumFlowerType) (Object) this.flowers.get(random);
            BlockFlower blockflower = enumflowertype.getBlockType().getBlock();

            if (enumflowertype != null && blockflower.getMaterial() != Material.air)
            {
                setGeneratedBlock(blockflower, enumflowertype);
                generate(world, random, blockpos);
            }
        }
	}

	@Override
	public VariableAmount getFlowersPerChunk() {
		return this.count;
	}

	@Override
	public void setFlowersPerChunk(VariableAmount count) {
		this.count = count;
	}

	@Override
	public WeightedCollection<WeightedObject<PlantType>> getFlowerTypes() {
		return this.flowers;
	}

}

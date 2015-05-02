/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.world;

import static org.spongepowered.common.data.DataTransactionBuilder.builder;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.gen.BiomeBuffer;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.GeneratorPopulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.data.SpongeBlockUtil;
import org.spongepowered.common.data.SpongeManipulatorRegistry;
import org.spongepowered.common.interfaces.IMixinWorld;
import org.spongepowered.common.interfaces.blocks.IMixinBlock;
import org.spongepowered.common.util.SpongeHooks;
import org.spongepowered.common.util.gen.FastChunkBuffer;
import org.spongepowered.common.util.gen.ObjectArrayMutableBiomeArea;

import java.util.Collection;
import java.util.List;

@NonnullByDefault
@Mixin(net.minecraft.world.chunk.Chunk.class)
public abstract class MixinChunk implements Chunk {

    private Vector3i chunkPos;
    private Vector3i chunkMin;
    private Vector3i chunkMax;
    private Vector3i chunkSize;

    private ChunkCoordIntPair chunkCoordIntPair;

    @Shadow private net.minecraft.world.World worldObj;
    @Shadow public int xPosition;
    @Shadow public int zPosition;
    @Shadow private boolean isChunkLoaded;
    @Shadow private boolean isTerrainPopulated;

    @Inject(method = "<init>(Lnet/minecraft/world/World;II)V", at = @At("RETURN"), remap = false)
    public void onConstructed(World world, int x, int z, CallbackInfo ci) {
        this.chunkPos = new Vector3i(x, 0, z);
        this.chunkMin = new Vector3i(x * 16, 0, z * 16);
        this.chunkMax = new Vector3i(x * 16 + 15, 255, z * 16 + 15);
        this.chunkSize = new Vector3i(16, 256, 16);
        this.chunkCoordIntPair = new ChunkCoordIntPair(x, z);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/ChunkPrimer;II)V", at = @At("RETURN"), remap = false)
    public void onNewlyGenerated(World world, ChunkPrimer primer, int chunkX, int chunkZ, CallbackInfo ci) {
        // The constructor with the ChunkPrimer in it is only used for newly
        // generated chunks, so we can call the generator populators here

        // Calling the generator populators here has the benefit that the chunk
        // can be modified before light is calculated and that implementations
        // of IChunkProvider provided by mods will very likely still work well

        List<GeneratorPopulator> genpop = ((IMixinWorld) world).getGeneratorPopulators();
        List<GeneratorPopulator> biomegenpop = Lists.newArrayList();

        BiomeGenBase[] biomeArray = world.getWorldChunkManager().getBiomeGenAt(null, chunkX * 16, chunkZ * 16, 16, 16, true);
        List<BiomeGenBase> encountered = Lists.newArrayList();
        for (BiomeGenBase biome : biomeArray) {
            if (encountered.contains(biome)) {
                continue;
            }
            biomegenpop.addAll(((BiomeType) biome).getGeneratorPopulators());
            encountered.add(biome);
        }

        if (!genpop.isEmpty() || !biomegenpop.isEmpty()) {
            FastChunkBuffer buffer = new FastChunkBuffer((net.minecraft.world.chunk.Chunk) (Object) this);
            BiomeBuffer biomes = new ObjectArrayMutableBiomeArea(biomeArray, new Vector2i(chunkX * 16, chunkZ * 16), new Vector2i(16, 16));
            for (GeneratorPopulator populator : biomegenpop) {
                populator.populate((org.spongepowered.api.world.World) world, buffer, biomes);
            }
            for (GeneratorPopulator populator : genpop) {
                populator.populate((org.spongepowered.api.world.World) world, buffer, biomes);
            }
        }
    }

    @SideOnly(Side.SERVER)
    @Inject(method = "onChunkLoad()V", at = @At("RETURN"))
    public void onChunkLoadInject(CallbackInfo ci) {
        SpongeHooks.logChunkLoad(this.worldObj, this.chunkPos);
    }

    @SideOnly(Side.SERVER)
    @Inject(method = "onChunkUnload()V", at = @At("RETURN"))
    public void onChunkUnloadInject(CallbackInfo ci) {
        SpongeHooks.logChunkUnload(this.worldObj, this.chunkPos);
    }

    @Override
    public Vector3i getPosition() {
        return this.chunkPos;
    }

    @Override
    public boolean isLoaded() {
        return this.isChunkLoaded;
    }

    @Override
    public boolean isPopulated() {
        return this.isTerrainPopulated;
    }

    @Override
    public boolean loadChunk(boolean generate) {
        WorldServer worldserver = (WorldServer) this.worldObj;
        net.minecraft.world.chunk.Chunk chunk = null;
        if (worldserver.theChunkProviderServer.chunkExists(this.xPosition, this.zPosition) || generate) {
            chunk = worldserver.theChunkProviderServer.loadChunk(this.xPosition, this.zPosition);
        }

        return chunk != null;
    }

    @Override
    public org.spongepowered.api.world.World getWorld() {
        return (org.spongepowered.api.world.World) this.worldObj;
    }

    @Override
    public Location getFullBlock(Vector3i position) {
        return getFullBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public Location getFullBlock(int x, int y, int z) {
        return getWorld().getFullBlock(xPosition * 16 + x, y, zPosition * 16 + z);
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(int x, int y, int z, Class<T> dataClass) {
        Optional<SpongeBlockUtil<T>> blockUtilOptional = SpongeManipulatorRegistry.getInstance().getBlockUtil(dataClass);
        if (blockUtilOptional.isPresent()) {
            return blockUtilOptional.get().fromBlockPos(this.worldObj, new BlockPos(x, y, z));
        }
        return Optional.absent();
    }


    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(int x, int y, int z, Class<T> manipulatorClass) {
        Optional<SpongeBlockUtil<T>> blockUtilOptional = SpongeManipulatorRegistry.getInstance().getBlockUtil(manipulatorClass);
        if (blockUtilOptional.isPresent()) {
            return blockUtilOptional.get().fromBlockPos(this.worldObj, new BlockPos(x, y, z));
        }
        return Optional.absent();
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(int x, int y, int z, Class<T> manipulatorClass) {
        Optional<SpongeBlockUtil<T>> blockUtilOptional = SpongeManipulatorRegistry.getInstance().getBlockUtil(manipulatorClass);
        if (blockUtilOptional.isPresent()) {
            return blockUtilOptional.get().remove(this.worldObj, new BlockPos(x, y, z));
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(int x, int y, int z, T manipulatorData, DataPriority priority) {
        Optional<SpongeBlockUtil<T>> blockUtilOptional = SpongeManipulatorRegistry.getInstance().getBlockUtil((Class<T>) (Class) manipulatorData
                .getClass());
        if (blockUtilOptional.isPresent()) {
            return blockUtilOptional.get().setData(this.worldObj, new BlockPos(x, y, z), manipulatorData, priority);
        }
        return builder().result(DataTransactionResult.Type.FAILURE).build();
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators(int x, int y, int z) {
        final BlockPos blockPos = new BlockPos(x, y, z);
        return ((IMixinBlock) getBlock(x, y, z)).getManipulators(this.worldObj, blockPos);
    }

    @Override
    public boolean contains(Location location) {
    	if(location.getExtent().equals(this.worldObj)) {
    		if(location.getX() >= xPosition * 16 && location.getX() < (xPosition + 1)*16 || location.getZ() >= zPosition * 16 && location.getZ() < (zPosition + 1)*16) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    @Override
    public Vector3i getBlockMin() {
        return this.chunkMin;
    }

    @Override
    public Vector3i getBlockMax() {
        return this.chunkMax;
    }

    @Override
    public Vector3i getBlockSize() {
        return this.chunkSize;
    }

}

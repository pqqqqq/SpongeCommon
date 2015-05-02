/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
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
package org.spongepowered.common.mixin.core.world.gen;

import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import org.spongepowered.api.util.gen.BiomeBuffer;
import org.spongepowered.api.util.gen.MutableBlockBuffer;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.GeneratorPopulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.util.gen.ChunkBufferPrimer;

@Mixin(MapGenBase.class)
public abstract class MixinMapGenBase implements GeneratorPopulator {

    @Shadow
    public abstract void func_175792_a(IChunkProvider chunkProvider, net.minecraft.world.World worldIn, int x, int z, ChunkPrimer chunkData);

    @Override
    public void populate(World world, MutableBlockBuffer buffer, BiomeBuffer biomes) {
        int x = buffer.getBlockMin().getX() / 16;
        int z = buffer.getBlockMin().getZ() / 16;
        func_175792_a(((net.minecraft.world.World) world).getChunkProvider(), (net.minecraft.world.World) world, x, z, new ChunkBufferPrimer(buffer));
    }

}

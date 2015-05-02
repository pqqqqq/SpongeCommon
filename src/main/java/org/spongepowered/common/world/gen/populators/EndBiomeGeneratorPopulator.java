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
package org.spongepowered.common.world.gen.populators;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.gen.BiomeBuffer;
import org.spongepowered.api.util.gen.MutableBlockBuffer;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.GeneratorPopulator;

public class EndBiomeGeneratorPopulator implements GeneratorPopulator {

    @Override
    public void populate(World world, MutableBlockBuffer buffer, BiomeBuffer biomes) {
        BlockState iblockstate = BlockTypes.END_STONE.getDefaultState();
        for (int i = 0; i < 16; ++i) {
            int x = i + buffer.getBlockMin().getX();
            for (int j = 0; j < 16; ++j) {
                int z = j + buffer.getBlockMin().getZ();

                for (int l = 255; l >= 0; --l) {
                    int y = l + buffer.getBlockMin().getY();
                    BlockState iblockstate2 = buffer.getBlock(x, y, z);

                    if (iblockstate2.getType() == BlockTypes.STONE) {

                        buffer.setBlock(x, y, z, iblockstate);
                    }
                }
            }
        }
    }

}

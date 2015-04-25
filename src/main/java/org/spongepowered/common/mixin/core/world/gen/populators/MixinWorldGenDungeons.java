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
package org.spongepowered.common.mixin.core.world.gen.populators;

import net.minecraft.item.ItemStack;

import net.minecraft.util.WeightedRandomChestContent;
import com.flowpowered.math.vector.Vector3i;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.api.data.manipulators.MobSpawnerData;
import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.util.weighted.WeightedCollection;
import org.spongepowered.api.util.weighted.WeightedItem;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Dungeon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.Sponge;

import java.util.Random;

@Mixin(WorldGenDungeons.class)
public abstract class MixinWorldGenDungeons extends WorldGenerator implements Dungeon {

    private VariableAmount attempts;
    private MobSpawnerData data;
    private WeightedCollection<WeightedItem> items;

    @Inject(method = "<init>(I)V", at = @At("RETURN"))
    public void onConstructed(int type, CallbackInfo ci) {
        this.attempts = VariableAmount.fixed(8);
        this.data = Sponge.getSpongeRegistry().getManipulatorRegistry().getBuilder(MobSpawnerData.class).get().create();
        this.items = new WeightedCollection<WeightedItem>();
    }

    private void populateItemsFromVanilla() {
        for (WeightedRandomChestContent item : WorldGenDungeons.CHESTCONTENT) {
            ItemStack stack = item.theItemId;
            VariableAmount quantity =
                    VariableAmount.baseWithRandomAddition(item.theMinimumChanceToGenerateItem, item.theMaximumChanceToGenerateItem
                            - item.theMinimumChanceToGenerateItem + 1);
        }
    }

    @Override
    public void populate(Chunk chunk, Random random) {
        World world = (World) chunk.getWorld();
        Vector3i min = chunk.getBlockMin();
        BlockPos chunkPos = new BlockPos(min.getX(), min.getY(), min.getZ());

        int n = this.attempts.getFlooredAmount(random);
        int x, y, z;

        for (int i = 0; i < n; ++i)
        {
            x = random.nextInt(16) + 8;
            y = random.nextInt(256);
            z = random.nextInt(16) + 8;
            generate(world, random, chunkPos.add(x, y, z));
        }
    }

    @Override
    public VariableAmount getAttemptsPerChunk() {
        return this.attempts;
    }

    @Override
    public void setAttemptsPerChunk(VariableAmount attempts) {
        this.attempts = attempts;
    }

    @Override
    public MobSpawnerData getSpawnerData() {
        return this.data;
    }

    @Override
    public WeightedCollection<WeightedItem> getPossibleContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VariableAmount getNumberOfItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setNumberOfItems(VariableAmount count) {
        // TODO Auto-generated method stub

    }

}

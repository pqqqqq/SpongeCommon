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
package org.spongepowered.common.world.gen.builders;

import org.spongepowered.api.world.gen.PopulatorFactory;
import org.spongepowered.api.world.gen.populators.BigMushroom;
import org.spongepowered.api.world.gen.populators.BlockBlob;
import org.spongepowered.api.world.gen.populators.Cactus;
import org.spongepowered.api.world.gen.populators.DesertWell;
import org.spongepowered.api.world.gen.populators.DoublePlant;
import org.spongepowered.api.world.gen.populators.Dungeon;
import org.spongepowered.api.world.gen.populators.EnderCrystalPlatform;
import org.spongepowered.api.world.gen.populators.Flowers;
import org.spongepowered.api.world.gen.populators.Forest;
import org.spongepowered.api.world.gen.populators.Glowstone;
import org.spongepowered.api.world.gen.populators.HugeTree;
import org.spongepowered.api.world.gen.populators.IcePath;
import org.spongepowered.api.world.gen.populators.IceSpike;
import org.spongepowered.api.world.gen.populators.JungleBush;
import org.spongepowered.api.world.gen.populators.Lake;
import org.spongepowered.api.world.gen.populators.Melons;
import org.spongepowered.api.world.gen.populators.Ore;
import org.spongepowered.api.world.gen.populators.Pumpkin;
import org.spongepowered.api.world.gen.populators.RandomFire;
import org.spongepowered.api.world.gen.populators.RandomLiquids;
import org.spongepowered.api.world.gen.populators.Reeds;
import org.spongepowered.api.world.gen.populators.SeaFloor;
import org.spongepowered.api.world.gen.populators.Shrub;
import org.spongepowered.api.world.gen.populators.Vines;
import org.spongepowered.api.world.gen.populators.WaterLily;

public class SpongePopulatorFactory implements PopulatorFactory {

    @Override
    public BigMushroom.Builder createBigMushroomPopulator() {
        return new BigMushroomBuilder();
    }

    @Override
    public BlockBlob.Builder createBlockBlockPopulator() {
        return new BlockBlobBuilder();
    }

    @Override
    public Cactus.Builder createCactusPopulator() {
        return new CactusBuilder();
    }

    @Override
    public DesertWell.Builder createDesertWellPopulator() {
        return new DesertWellBuilder();
    }

    @Override
    public DoublePlant.Builder createDoublePlantPopulator() {
        return new DoublePlantBuilder();
    }

    @Override
    public Dungeon.Builder createDungeonPopulator() {
        return new DungeonBuilder();
    }

    @Override
    public EnderCrystalPlatform.Builder createEnderCrystalPlatformPopulator() {
        return new EnderCrystalPlatformBuilder();
    }

    @Override
    public Flowers.Builder createFlowerPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Forest.Builder createForestPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Glowstone.Builder createGlowstonePopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HugeTree.Builder createHugeTreePopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IcePath.Builder createIcePathPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IceSpike.Builder createIceSpikePopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JungleBush.Builder createJungleBushPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Lake.Builder createLakePopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Melons.Builder createMelonPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Ore.Builder createOrePopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pumpkin.Builder createPumpkinPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RandomFire.Builder createRandomFirePopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RandomLiquids.Builder createRandomLiquidsPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reeds.Builder createReedsPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SeaFloor.Builder createSeaFloorPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Shrub.Builder createShrubPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vines.Builder createVinesPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WaterLily.Builder createWaterLilyPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

}

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

import net.minecraft.world.gen.feature.WorldGenDesertWells;

import static com.google.common.base.Preconditions.checkArgument;
import org.spongepowered.api.world.gen.populators.DesertWell;
import org.spongepowered.api.world.gen.populators.DesertWell.Builder;

public class DesertWellBuilder implements DesertWell.Builder {

    private double p;

    public DesertWellBuilder() {
        reset();
    }

    @Override
    public Builder probability(double p) {
        checkArgument(Double.isNaN(p), "The probability must be a number.");
        checkArgument(Double.isInfinite(p), "The probability cannot be infinite.");
        this.p = p;
        return this;
    }

    @Override
    public Builder reset() {
        this.p = 0.001;
        return this;
    }

    @Override
    public DesertWell build() throws IllegalStateException {
        DesertWell populator = (DesertWell) new WorldGenDesertWells();
        populator.setSpawnProbability(this.p);
        return populator;
    }

}

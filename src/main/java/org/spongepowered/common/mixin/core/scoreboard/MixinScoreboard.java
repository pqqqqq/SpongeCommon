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
package org.spongepowered.common.mixin.core.scoreboard;

import com.google.common.base.Optional;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.interfaces.IMixinScoreObjective;
import org.spongepowered.common.scoreboard.SpongeDisplaySlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NonnullByDefault
@Mixin(Scoreboard.class)
@Implements(@Interface(iface = org.spongepowered.api.scoreboard.Scoreboard.class, prefix = "scoreboard$"))
public abstract class MixinScoreboard {

    @Shadow public Map<String, ScoreObjective> scoreObjectives;
    @Shadow public ScoreObjective[] objectiveDisplaySlots;
    @Shadow public Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias;
    @Shadow public Map<String, Map<ScoreObjective, net.minecraft.scoreboard.Score>> entitiesScoreObjectives;

    @Shadow public abstract void func_96522_a(ScoreObjective objective);
    @Shadow public abstract void removeObjective(ScoreObjective objective);

    public Optional<Objective> scoreboard$getObjective(String name) {
        return Optional.fromNullable((Objective) this.scoreObjectives.get(name));
    }

    public Optional<Objective> scoreboard$getObjective(DisplaySlot slot) {
        return Optional.fromNullable((Objective) (this.objectiveDisplaySlots[((SpongeDisplaySlot) slot).getIndex()]));
    }

    public void scoreboard$addObjective(Objective objective, DisplaySlot displaySlot) {
        if (!this.scoreObjectives.containsValue(objective)) {
            throw new IllegalStateException("The specified objective does not exist on this scoreboard!");
        }
        for (int i = 0; i < this.objectiveDisplaySlots.length; i++) {
            if (this.objectiveDisplaySlots[i] == objective) {
                this.objectiveDisplaySlots[i] = null;
                break;
            }

        }
        this.objectiveDisplaySlots[((SpongeDisplaySlot) displaySlot).getIndex()] = (ScoreObjective) objective;
    }

    public void scoreboard$addObjective(Objective objective) {
        if (this.scoreObjectives.containsKey(objective) || this.scoreObjectives.containsKey(objective.getName())) {
            throw new IllegalArgumentException("The specified objective already exists on this scoreboard!");
        }
        ScoreObjective scoreObjective = (ScoreObjective) objective;
        List<ScoreObjective> objectives = this.scoreObjectiveCriterias.get(scoreObjective.getCriteria());
        if (objective == null) {
            objectives = new ArrayList<ScoreObjective>();
            this.scoreObjectiveCriterias.put(scoreObjective.getCriteria(), objectives);
        }
        objectives.add(scoreObjective);
        ((IMixinScoreObjective) scoreObjective).addToScoreboard((org.spongepowered.api.scoreboard.Scoreboard) this);
        this.func_96522_a(scoreObjective);
    }

    public Set<Objective> scoreboard$getObjectivesByCriteria(Criterion criteria) {
        return new HashSet(this.scoreObjectiveCriterias.get((ScoreObjective) criteria));
    }

    public Set<Objective> scoreboard$getObjectives() {
        return new HashSet(this.scoreObjectives.values());
    }

    public void scoreboard$removeObjective(Objective objective) {
        ((IMixinScoreObjective) objective).removeFromScoreboard((org.spongepowered.api.scoreboard.Scoreboard) this);
        this.removeObjective((ScoreObjective) objective);
    }

    public Set<Score> scoreboard$getScores(String entry) {
        if (this.entitiesScoreObjectives.get(entry) == null) {
            return new HashSet<Score>();
        }
        return new HashSet(this.entitiesScoreObjectives.get(entry).values());
    }

    public void removeScores(Text name) {
        for (Objective objective: (Collection<Objective>) (Object) this.scoreObjectives.values()) {
            objective.removeScore(objective.getScore(name));
        }
    }
}

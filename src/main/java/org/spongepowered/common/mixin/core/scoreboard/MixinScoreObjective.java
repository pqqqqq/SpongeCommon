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

import com.google.common.collect.Maps;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.interfaces.IMixinScore;
import org.spongepowered.common.interfaces.IMixinScoreObjective;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NonnullByDefault
@Mixin(ScoreObjective.class)
@Implements(@Interface(iface = Objective.class, prefix = "objective$"))
public abstract class MixinScoreObjective implements IMixinScoreObjective {

    @Shadow public String displayName;

    @Shadow public IScoreObjectiveCriteria objectiveCriteria;

    @Shadow public Scoreboard theScoreboard;

    @Shadow public IScoreObjectiveCriteria.EnumRenderType renderType;

    @Shadow public abstract String getName();

    public Set<Scoreboard> scoreboards = new HashSet<Scoreboard>();

    @Intrinsic
    public String objective$getName() {
        return this.getName();
    }

    public Text objective$getDisplayName() {
        return Texts.fromLegacy(this.displayName);
    }

    public void objective$setDisplayName(Text displayName) {
        if (Texts.toPlain(displayName).length() > 32) {
            throw new IllegalArgumentException("displayName is greater than 32 characters!");
        }
        this.displayName = Texts.toLegacy(displayName);
        this.theScoreboard.func_96532_b((ScoreObjective) (Object) this);
    }

    public Criterion objective$getCriterion() {
        return (Criterion) this.objectiveCriteria;
    }

    public ObjectiveDisplayMode objective$getDisplayMode() {
        return (ObjectiveDisplayMode) (Object) renderType;
    }

    public void objective$setDisplayMode(ObjectiveDisplayMode mode) {
        this.renderType = (IScoreObjectiveCriteria.EnumRenderType) (Object) mode;
        this.theScoreboard.func_96532_b((ScoreObjective) (Object) this);
    }

    public Set<Score> objective$getScores() {
        Set<net.minecraft.scoreboard.Score> scores = new HashSet();
        for (Scoreboard scoreboard: this.scoreboards) {
            for (Map<ScoreObjective, net.minecraft.scoreboard.Score> scoreMap: (Collection<Map<ScoreObjective, net.minecraft.scoreboard.Score>>) scoreboard.entitiesScoreObjectives.values()) {
                scores.addAll(scoreMap.values());
            }
        }
        return (Set<Score>) (Object) scores;
    }

    public void objective$addScore(Score score) {
        for (Scoreboard scoreboard: this.scoreboards) {
            Map<ScoreObjective, net.minecraft.scoreboard.Score> scoreMap = (Map) scoreboard.entitiesScoreObjectives.get(Texts.toLegacy(score.getName()));
            if (scoreMap == null) {
                scoreMap = Maps.newHashMap();
                scoreboard.entitiesScoreObjectives.put(Texts.toLegacy(score.getName()), scoreMap);
            }
            // Since the SpongeAPI contract is that modifying an objective affects all scoreboards
            // it's added to, mod-added scores will be overridden.
            if (scoreMap.containsKey(this) && ((IMixinScore) scoreMap.get(this)).spongeCreated()) {
                throw new IllegalArgumentException("A score already exists with the name " + score.getName());
            }
            scoreMap.put((ScoreObjective) (Objective) this, (net.minecraft.scoreboard.Score) score);
        }
    }

    public Score objective$getScore(Text name) {
        List<Scoreboard> unsetScoreboards = new ArrayList<Scoreboard>();
        // Scan through all the scoreboards this objective is present on. If a score is found that's
        // been last set through the API, then return it.
        //
        // If no suitable scores are found, either because the score doesn't exist, or all of the existing
        // scores have been modified in some way, then a new score is created, and added to all scoreboards
        // that didn't have a mod-set score.
        for (Scoreboard scoreboard: this.scoreboards) {
            Map<ScoreObjective, net.minecraft.scoreboard.Score> scoreMap = (Map) scoreboard.entitiesScoreObjectives.get(Texts.toLegacy(name));
            if (scoreMap != null) {
                net.minecraft.scoreboard.Score score = scoreMap.get(this);
                if (score != null) {
                    if (((IMixinScore) score).spongeSet()) {
                        return (Score) (Object) score;
                    }
                    continue;
                }
            } else {
                scoreMap = Maps.newHashMap();
                scoreboard.entitiesScoreObjectives.put(Texts.toLegacy(name), scoreMap);
            }
            unsetScoreboards.add(scoreboard);
        }

        // Since the score is created by Sponge, there's no one candidate for the Vanilla field 'theScoreboard'.
        // If this objective is on at least one scoreboard, the first one is picked. Otherwise, it will be set
        // when this objective is first added to a scoreboard.
        Scoreboard dummyScoreboard = this.scoreboards.isEmpty() ? this.scoreboards.iterator().next() : null;
        net.minecraft.scoreboard.Score score = new net.minecraft.scoreboard.Score(dummyScoreboard, (ScoreObjective) (Objective) this, Texts.toLegacy(name));
        ((IMixinScore) score).setSpongeCreated();

        for (Scoreboard scoreboard: unsetScoreboards) {
            ((Map) scoreboard.entitiesScoreObjectives.get(Texts.toLegacy(name))).put(this, score);
        }
        return (Score) (Object) (score);
    }

    public Set<org.spongepowered.api.scoreboard.Scoreboard> objective$getScoreboards() {
        return (HashSet<org.spongepowered.api.scoreboard.Scoreboard>) ((HashSet<org.spongepowered.api.scoreboard.Scoreboard>) (Object) this.scoreboards).clone();
    }

    @Override
    public void addToScoreboard(org.spongepowered.api.scoreboard.Scoreboard spongeScoreboard) {
        Scoreboard scoreboard = (Scoreboard) spongeScoreboard;
        if (this.theScoreboard == null) {
            this.theScoreboard = scoreboard;
        }
        this.scoreboards.add(scoreboard);

        for (Map<ScoreObjective, net.minecraft.scoreboard.Score> scoreMap: (Collection<Map<ScoreObjective, net.minecraft.scoreboard.Score>>) scoreboard.entitiesScoreObjectives.values()) {
            net.minecraft.scoreboard.Score score = scoreMap.get(this);
            if (score != null && !((IMixinScore) score).spongeCreated()) {
                score.
            }
        }
    }
}

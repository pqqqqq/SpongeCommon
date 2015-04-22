package org.spongepowered.common.mixin.core.scoreboard;

import net.minecraft.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.IMixinScore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NonnullByDefault
@Mixin(Score.class)
@Implements(@Interface(iface = org.spongepowered.api.scoreboard.Score.class, prefix = "score$"))
public abstract class MixinScore implements IMixinScore {

    @Shadow public String scorePlayerName;
    @Shadow public int scorePoints;

    @Shadow public abstract void setScorePoints(int points);

    public boolean spongeCreated;

    public boolean spongeSet;

    public Set<Objective> objectives = new HashSet<Objective>();

    public Text score$getName() {
        return Texts.fromLegacy(this.scorePlayerName);
    }

    public int score$getScore() {
        return this.scorePoints;
    }

    public void score$setScore(int score) {
        this.setScorePoints(score);
        this.spongeSet = true;
    }

    public Set<Objective> score$getObjectives() {
        return this.objectives;
    }

    public boolean spongeCreated() {
        return this.spongeCreated;
    }

    public void setSpongeCreated() {
        this.spongeCreated = true;
    }

    public boolean spongeSet() {
        return this.spongeSet;
    }

    @Inject(method = "setScorePoints(I)V;", at = @At("RETURN"))
    public void onSetScorePoints(CallbackInfo ci) {
        this.spongeSet = false;
    }

}

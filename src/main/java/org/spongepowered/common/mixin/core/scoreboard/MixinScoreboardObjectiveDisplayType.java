package org.spongepowered.common.mixin.core.scoreboard;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@NonnullByDefault
@Mixin(IScoreObjectiveCriteria.EnumRenderType.class)
@Implements(@Interface(iface = ObjectiveDisplayMode.class, prefix = "mode$"))
public abstract class MixinScoreboardObjectiveDisplayType {

    @Shadow public String field_178798_d;

    public String mode$getId() {
        return this.field_178798_d;
    }

    public String mode$getName() {
        return this.field_178798_d;
    }

}

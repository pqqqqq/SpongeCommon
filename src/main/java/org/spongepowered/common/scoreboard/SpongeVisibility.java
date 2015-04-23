package org.spongepowered.common.scoreboard;

import net.minecraft.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;

public class SpongeVisibility implements Visibility {

    private Team.EnumVisible handle;

    public SpongeVisibility(Team.EnumVisible handle) {
        this.handle = handle;
    }

    @Override
    public String getId() {
        return this.handle.name();
    }

    @Override
    public String getName() {
        return this.handle.toString();
    }

    public Team.EnumVisible getHandle() {
        return this.handle;
    }
}

package org.spongepowered.common.scoreboard;

import com.google.common.base.Optional;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.text.format.TextColor;

public class SpongeDisplaySlot implements DisplaySlot {

    private String name;
    private Optional<TextColor> textColor;
    private int id;

    public SpongeDisplaySlot(String name, TextColor textColor, int id) {
        this.name = name;
        this.textColor = Optional.fromNullable(textColor);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Optional<TextColor> getTeamColor() {
        return this.textColor;
    }

    public int getIndex() {
        return this.id;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SpongeDisplaySlot other = (SpongeDisplaySlot) obj;
        if (this.getTeamColor() != other.getTeamColor()) {
            return false;
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }


}
package org.spongepowered.common.mixin.core.scoreboard;

import com.google.common.base.Optional;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.registry.SpongeGameRegistry;
import org.spongepowered.common.scoreboard.SpongeVisibility;
import org.spongepowered.common.text.format.SpongeTextColor;

import java.util.HashSet;
import java.util.Set;

@NonnullByDefault
@Mixin(ScorePlayerTeam.class)
@Implements(@Interface(iface = Team.class, prefix = "team$"))
public abstract class MixinTeam {

    @Shadow public String registeredName;
    @Shadow public String teamNameSPT;
    @Shadow public EnumChatFormatting chatFormat;
    @Shadow public String namePrefixSPT;
    @Shadow public String colorSuffix;
    @Shadow public boolean allowFriendlyFire;
    @Shadow public boolean canSeeFriendlyInvisibles;
    @Shadow public net.minecraft.scoreboard.Team.EnumVisible field_178778_i; // nameTagVisibility
    @Shadow public net.minecraft.scoreboard.Team.EnumVisible field_178776_j; // deathMessageVisiblity
    @Shadow public Set<String> membershipSet;

    public String team$getName() {
        return this.registeredName;
    }

    public Text team$getDisplayName() {
        return Texts.fromLegacy(this.teamNameSPT);
    }

    public TextColor team$getColor() {
        return ((SpongeGameRegistry) Sponge.getGame().getRegistry()).enumChatColor.get(this.chatFormat);
    }

    public void team$setColor(TextColor color) {
        if (color == TextColors.RESET) {
            throw new IllegalArgumentException("TextColors.RESET cannot be used as a team color!");
        }
        this.chatFormat = ((SpongeTextColor) color).getHandle();
    }

    public void team$setDisplayName(Text displayName) {
        if (Texts.toLegacy(displayName).length() > 32) {
            System.out.println("Team display name length cannot be greater than 32 characters!");
        }
        this.teamNameSPT = Texts.toLegacy(displayName);
    }

    public Text team$getPrefix() {
        return Texts.fromLegacy(this.namePrefixSPT);
    }

    public void team$setPrefix(Text prefix) {
        if (Texts.toLegacy(prefix).length() > 16) {
            throw new IllegalArgumentException("Prefix length cannot be greater than 16 characters!");
        }
        this.namePrefixSPT = Texts.toLegacy(prefix);
    }

    public Text team$getSuffix() {
        return Texts.fromLegacy(this.colorSuffix);
    }

    public void team$setSuffix(Text suffix) {
        if (Texts.toLegacy(suffix).length() > 16) {
            throw new IllegalArgumentException("Suffix length cannot be greater than 16 characters!");
        }
        this.colorSuffix = Texts.toLegacy(suffix);
    }

    public boolean team$allowFriendlyFire() {
        return this.allowFriendlyFire;
    }

    @Overwrite
    public void team$setAllowFriendlyFire(boolean enabled) {
        this.allowFriendlyFire = enabled;
    }

    public boolean team$canSeeFriendlyInvisibles() {
        return this.canSeeFriendlyInvisibles;
    }

    public void team$setCanSeeFriendlyInvisibles(boolean enabled) {
        this.canSeeFriendlyInvisibles = enabled;
    }

    public Visibility team$getNameTagVisibility() {
        return ((SpongeGameRegistry) Sponge.getGame().getRegistry()).enumVisible.get(this.field_178778_i);
    }

    public void team$setNameTagVisibility(Visibility visibility) {
        this.field_178778_i = ((SpongeVisibility) visibility).getHandle();
    }

    public Visibility team$getDeathTextVisibility() {
        return ((SpongeGameRegistry) Sponge.getGame().getRegistry()).enumVisible.get(this.field_178776_j );
    }

    public void team$setDeathTextVisibility(Visibility visibility) {
        this.field_178776_j = ((SpongeVisibility) visibility).getHandle();
    }

    public Set<User> team$getUsers() {
        // User isn't implemented yet, so only online players can be returned currenlly
        Set<User> users = new HashSet<User>();
        for (String name: this.membershipSet) {
            Optional<Player> player = Sponge.getGame().getServer().getPlayer(name);
            if (player.isPresent()) {
                users.add(player.get());
            } else {
                // Well, we tried ¯\_(ツ)_/¯
            }
        }
        return users;
    }

    public void team$addUser(User user) {
        this.membershipSet.add(user.getName()); // hai
    }

    public boolean removeUser(User user) {
        return this.membershipSet.remove(user.getName()); // bai
    }
}

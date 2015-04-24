package org.spongepowered.common.scoreboard;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.ObjectiveBuilder;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

public class SpongeObjectiveBuilder implements ObjectiveBuilder {

    private String name;
    private Text displayName;
    private Criterion criterion;
    private ObjectiveDisplayMode objectiveDisplayMode = ObjectiveDisplayModes.INTEGER;
    private Map<String, Score> entries = new HashMap<String, Score>();

    @Override
    public ObjectiveBuilder name(String name) {
        checkNotNull(name, "Name cannot be null");
        this.name = name;
        return this;
    }

    @Override
    public ObjectiveBuilder displayName(Text displayName) {
        checkNotNull(displayName, "DisplayName cannot be null");
        this.displayName = displayName;
        return this;
    }

    @Override
    public ObjectiveBuilder criterion(Criterion criterion) {
        checkNotNull(criterion, "Criterion cannot be null");
        this.criterion = criterion;
        return this;
    }

    @Override
    public ObjectiveBuilder objectiveDisplayMode(ObjectiveDisplayMode objectiveDisplayMode) {
        checkNotNull(objectiveDisplayMode, "ObjectiveDisplayMode cannot be null");
        this.objectiveDisplayMode = objectiveDisplayMode;
        return this;
    }

    @Override
    public ObjectiveBuilder entries(Map<String, Score> entries) {
        checkNotNull(entries, "Entries cannot be null");
        this.entries = entries;
        return this;
    }

    @Override
    public ObjectiveBuilder reset() {
        this.name = null;
        this.displayName = null;
        this.criterion = null;
        this.objectiveDisplayMode = ObjectiveDisplayModes.INTEGER;
        this.entries = new HashMap<String, Score>();
        return this;
    }

    @Override
    public Objective build() throws IllegalStateException {
        checkState(this.name != null, "Name cannot be null");
        checkState(displayName != null, "DisplayName cannot be null");
        checkState(criterion != null, "Criterion cannot be null");

        ScoreObjective objective = new ScoreObjective(null, this.name, (IScoreObjectiveCriteria) this.criterion);
        objective.setDisplayName(Texts.toLegacy(this.displayName));
        objective.setRenderType((IScoreObjectiveCriteria.EnumRenderType) (Object) this.objectiveDisplayMode);
        objective.
    }
}

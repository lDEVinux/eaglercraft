// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api.score;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class Scoreboard {
	private String name;
	private Position position;
	private final Map<String, Objective> objectives;
	private final Map<String, Score> scores;
	private final Map<String, Team> teams;

	public Collection<Objective> getObjectives() {
		return Collections.unmodifiableCollection((Collection<? extends Objective>) this.objectives.values());
	}

	public Collection<Score> getScores() {
		return Collections.unmodifiableCollection((Collection<? extends Score>) this.scores.values());
	}

	public Collection<Team> getTeams() {
		return Collections.unmodifiableCollection((Collection<? extends Team>) this.teams.values());
	}

	public void addObjective(final Objective objective) {
		Preconditions.checkNotNull((Object) objective, (Object) "objective");
		Preconditions.checkArgument(!this.objectives.containsKey(objective.getName()), "Objective %s already exists in this scoreboard", new Object[] { objective.getName() });
		this.objectives.put(objective.getName(), objective);
	}

	public void addScore(final Score score) {
		Preconditions.checkNotNull((Object) score, (Object) "score");
		this.scores.put(score.getItemName(), score);
	}

	public void addTeam(final Team team) {
		Preconditions.checkNotNull((Object) team, (Object) "team");
		Preconditions.checkArgument(!this.teams.containsKey(team.getName()), "Team %s already exists in this scoreboard", new Object[] { team.getName() });
		this.teams.put(team.getName(), team);
	}

	public Team getTeam(final String name) {
		return this.teams.get(name);
	}

	public void removeObjective(final String objectiveName) {
		this.objectives.remove(objectiveName);
	}

	public void removeScore(final String scoreName) {
		this.scores.remove(scoreName);
	}

	public void removeTeam(final String teamName) {
		this.teams.remove(teamName);
	}

	public void clear() {
		this.name = null;
		this.position = null;
		this.objectives.clear();
		this.scores.clear();
		this.teams.clear();
	}

	public String getName() {
		return this.name;
	}

	public Position getPosition() {
		return this.position;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Scoreboard)) {
			return false;
		}
		final Scoreboard other = (Scoreboard) o;
		if (!other.canEqual(this)) {
			return false;
		}
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		Label_0065: {
			if (this$name == null) {
				if (other$name == null) {
					break Label_0065;
				}
			} else if (this$name.equals(other$name)) {
				break Label_0065;
			}
			return false;
		}
		final Object this$position = this.getPosition();
		final Object other$position = other.getPosition();
		Label_0102: {
			if (this$position == null) {
				if (other$position == null) {
					break Label_0102;
				}
			} else if (this$position.equals(other$position)) {
				break Label_0102;
			}
			return false;
		}
		final Object this$objectives = this.getObjectives();
		final Object other$objectives = other.getObjectives();
		Label_0139: {
			if (this$objectives == null) {
				if (other$objectives == null) {
					break Label_0139;
				}
			} else if (this$objectives.equals(other$objectives)) {
				break Label_0139;
			}
			return false;
		}
		final Object this$scores = this.getScores();
		final Object other$scores = other.getScores();
		Label_0176: {
			if (this$scores == null) {
				if (other$scores == null) {
					break Label_0176;
				}
			} else if (this$scores.equals(other$scores)) {
				break Label_0176;
			}
			return false;
		}
		final Object this$teams = this.getTeams();
		final Object other$teams = other.getTeams();
		if (this$teams == null) {
			if (other$teams == null) {
				return true;
			}
		} else if (this$teams.equals(other$teams)) {
			return true;
		}
		return false;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Scoreboard;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final Object $name = this.getName();
		result = result * 31 + (($name == null) ? 0 : $name.hashCode());
		final Object $position = this.getPosition();
		result = result * 31 + (($position == null) ? 0 : $position.hashCode());
		final Object $objectives = this.getObjectives();
		result = result * 31 + (($objectives == null) ? 0 : $objectives.hashCode());
		final Object $scores = this.getScores();
		result = result * 31 + (($scores == null) ? 0 : $scores.hashCode());
		final Object $teams = this.getTeams();
		result = result * 31 + (($teams == null) ? 0 : $teams.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Scoreboard(name=" + this.getName() + ", position=" + this.getPosition() + ", objectives=" + this.getObjectives() + ", scores=" + this.getScores() + ", teams=" + this.getTeams() + ")";
	}

	public Scoreboard() {
		this.objectives = new HashMap<String, Objective>();
		this.scores = new HashMap<String, Score>();
		this.teams = new HashMap<String, Team>();
	}
}

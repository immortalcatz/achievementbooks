package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.facade.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AchievementStorage {

	private final Map<String, AchievementData> storage = new HashMap<String, AchievementData>();

	public void append(AchievementData data) {
		if (storage.containsKey(data.username())) {
			storage.remove(data.username());
		}
		storage.put(data.username(), data);
	}

	public AchievementData forPlayer(Player player) {
		final String name = player.getDisplayName();
		return forPlayer(name);
	}

	public Set<String> players() {
		return storage.keySet();
	}

	public AchievementData forPlayer(String name) {
		if (storage.containsKey(name)) {
			return storage.get(name);
		}

		final AchievementData achievementData = new AchievementData(name);
		this.append(achievementData);

		return achievementData;
	}

	public void clear() {
		storage.clear();
	}

	@Override
	public final boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof AchievementStorage)) {
			return false;
		}

		AchievementStorage that = (AchievementStorage) o;

		return storage != null ? storage.equals(that.storage) : that.storage == null;
	}

	@Override
	public final int hashCode() {
		return storage != null ? storage.hashCode() : 0;
	}
}

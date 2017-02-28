package com.stateshifterlabs.achievementbooks.data;

public class PageElement {

	private String achievement;
	private String description;
	private String header;
	private String mod;
	private boolean checked = false;
	private int id;

	public PageElement(int id) {
		this.id = id;
	}

	public String achievement() {

		return achievement;
	}

	public String formattedAchievement() {

		return String.format("%s %s", achievement, formattedMod());
	}

	public void withAchievement(String achievement) {
		this.achievement = achievement;
	}

	public void withDescription(String description) {
		this.description = description;
	}

	public void withHeader(String header) {
		this.header = header;
	}

	public void toggleState() {
		this.checked = !this.checked;
	}

	public boolean checked() {
		return this.checked;
	}

	public void withMod(String mod) {
		this.mod = mod;
	}

	public Type type() {
		if (achievement != null) {
			return Type.ACHIEVEMENT;
		}

		if (header != null) {
			return Type.HEADER;
		}

		return Type.TEXT;
	}

	public String formattedDescription() {
		return String.format("%s", description);
	}

	public String description() {
		return description;
	}

	public String formattedHeader() {
		return String.format("%s", header);
	}

	public String header() {
		return header;
	}

	public String mod() {
		return mod;
	}

	public String formattedMod() {
		if (null == mod) {
			return "";
		}
		return String.format("§1§o[%s]§r", mod);
	}

	public boolean done() {
		return false;
	}

	public void toggleState(boolean checked) {
		this.checked = checked;
	}

	public int id() {
		return id;
	}

	public boolean hasDescription() {
		return description() != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PageElement that = (PageElement) o;

		if (checked != that.checked) {
			return false;
		}
		if (id != that.id) {
			return false;
		}
		if (achievement != null ? !achievement.equals(that.achievement) : that.achievement != null) {
			return false;
		}
		if (description != null ? !description.equals(that.description) : that.description != null) {
			return false;
		}
		if (header != null ? !header.equals(that.header) : that.header != null) {
			return false;
		}
		return mod != null ? mod.equals(that.mod) : that.mod == null;
	}

	@Override
	public int hashCode() {
		int result = achievement != null ? achievement.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (header != null ? header.hashCode() : 0);
		result = 31 * result + (mod != null ? mod.hashCode() : 0);
		result = 31 * result + (checked ? 1 : 0);
		result = 31 * result + id;
		return result;
	}


	public enum Type {
		HEADER, ACHIEVEMENT, TEXT
	}

}

package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.sdgslicer;

import soot.Unit;

import java.util.HashSet;
import java.util.Set;

public class GlobalVisit {
	private Set<Unit> contextSensitivity;
	private Set<Unit> visited;

	public GlobalVisit(Set<Unit> contextSensitivity) {
		this.contextSensitivity = contextSensitivity;
		this.visited = new HashSet<>();
	}

	public Set<Unit> getContextSensitivity() {
		return this.contextSensitivity;
	}

	public Set<Unit> getVisited() {
		return this.visited;
	}
}
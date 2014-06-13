package com.bc.stats.service.impl;

import com.bc.stats.service.StatsService;

public class StatsServiceImpl implements StatsService {

	public double calculatePER() {
		Long threePointMakes = 0L;
		Long fieldGoalMakes = 0L;
		Long assists = 0L;
		Long freeThrowMakes = 0L;
		double minutes = 0;
		Long rebounds = 0L;
		Long defensiveRebounds = 0L;
		Long teamAssists = 0L;
		Long teamFieldGoals = 0L;
		double leagueFactor = getLeagueFactor();
		double unadjustedPER = 1/minutes * 
				(threePointMakes + (2/3 * assists) + 
				(2-leagueFactor*(teamAssists/teamFieldGoals)*fieldGoalMakes));
		return 0;
	}
	
	private double getLeagueFactor() {
		Long leagueAssists = 0L;
		Long leagueFieldGoals = 0L;
		Long leagueFreeThrows = 0L;
		return (2/3-((.5*leagueAssists/leagueFieldGoals)/(2*leagueFieldGoals/leagueFreeThrows)));
	}
}

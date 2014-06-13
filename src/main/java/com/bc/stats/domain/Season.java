package com.bc.stats.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Season {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private LocalDate startDate;

   private LocalDate endDate;

   @OneToMany
   private List<TeamSeason> teamSeasons = new ArrayList<TeamSeason>();

   @OneToMany
   private List<PlayerSeason> playerSeasons = new ArrayList<PlayerSeason>();

   public Long getId() {
      return id;
   }

   public LocalDate getStartDate() {
      return startDate;
   }

   public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
   }

   public LocalDate getEndDate() {
      return endDate;
   }

   public void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
   }

   public List<TeamSeason> getTeamSeasons() {
      return teamSeasons;
   }

   public void setTeamSeasons(List<TeamSeason> teamSeasons) {
      this.teamSeasons = teamSeasons;
   }

   public List<PlayerSeason> getPlayerSeasons() {
      return playerSeasons;
   }

   public void setPlayerSeasons(List<PlayerSeason> playerSeasons) {
      this.playerSeasons = playerSeasons;
   }

}

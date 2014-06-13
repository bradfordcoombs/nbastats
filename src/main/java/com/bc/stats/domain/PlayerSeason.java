package com.bc.stats.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PlayerSeason {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn
   private Player player;

   @ManyToOne
   @JoinColumn
   private Team team;

   @ManyToOne
   @JoinColumn
   private Season season;

   public Long getId() {
      return id;
   }

   public Player getPlayer() {
      return player;
   }

   public void setPlayer(Player player) {
      this.player = player;
   }

   public Team getTeam() {
      return team;
   }

   public void setTeam(Team team) {
      this.team = team;
   }

   public Season getSeason() {
      return season;
   }

   public void setSeason(Season season) {
      this.season = season;
   }
}

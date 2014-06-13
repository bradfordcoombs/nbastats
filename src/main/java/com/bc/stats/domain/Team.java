/**
 * 
 */
package com.bc.stats.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author Bradford Entity representing a team
 */
@Entity
public class Team {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @OneToMany(mappedBy = "team")
   private List<TeamSeason> seasons = new ArrayList<TeamSeason>();

   public Long getId() {
      return id;
   }

   public List<TeamSeason> getSeasons() {
      return seasons;
   }

   public void setSeasons(List<TeamSeason> seasons) {
      this.seasons = seasons;
   }
}

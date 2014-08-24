package com.bc.stats.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.bc.stats.domain.builder.PlayerBuilder;

@Entity
public class Player {

   @Id
   @GeneratedValue
   private Long id;

   @Column(unique = true)
   private Long nbaDotComId;

   @Column(unique = true)
   private Long espnDotComId;

   private String firstName;

   private String lastName;

   private LocalDate birthDate;

   private Long heightInInches;

   private Long weightInPounds;

   private String college;

   private String position;

   private LocalDate dateDrafted;

   private Long activeFrom;

   private Long activeTo;

   @OneToOne
   @JoinColumn(name = "combine_result_id", referencedColumnName = "id", insertable = false, updatable = false)
   @Fetch(FetchMode.JOIN)
   private CombineResults combineAnthropometricResult;

   public Player(PlayerBuilder builder) {
      this.nbaDotComId = builder.getNbaDotComId();
      this.espnDotComId = builder.getEspnDotComId();
      this.firstName = builder.getFirstName();
      this.lastName = builder.getLastName();
      this.birthDate = builder.getBirthDate();
      this.heightInInches = builder.getHeightInInches();
      this.weightInPounds = builder.getWeightInPounds();
      this.college = builder.getCollege();
      this.position = builder.getPosition();
      this.dateDrafted = builder.getDateDrafted();
      this.activeFrom = builder.getActiveFrom();
      this.activeTo = builder.getActiveTo();
      this.combineAnthropometricResult = builder.getCombineAnthropometricResult();
   }

   protected Player() {}

   public Long getNbaDotComId() {
      return nbaDotComId;
   }

   public Long getEspnDotComId() {
      return espnDotComId;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public Long getHeightInInches() {
      return heightInInches;
   }

   public Long getWeightInPounds() {
      return weightInPounds;
   }

   public String getCollege() {
      return college;
   }

   public String getPosition() {
      return position;
   }

   public LocalDate getDateDrafted() {
      return dateDrafted;
   }

   public CombineResults getCombineAnthropometricResult() {
      return combineAnthropometricResult;
   }

   public void setCombineAnthropometricResult(CombineResults combineAnthropometricResult) {
      this.combineAnthropometricResult = combineAnthropometricResult;
   }

   public void setNbaDotComId(Long nbaDotComId) {
      this.nbaDotComId = nbaDotComId;
   }

   public void setEspnDotComId(Long espnDotComId) {
      this.espnDotComId = espnDotComId;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public void setHeightInInches(Long heightInInches) {
      this.heightInInches = heightInInches;
   }

   public void setWeightInPounds(Long weightInPounds) {
      this.weightInPounds = weightInPounds;
   }

   public void setCollege(String college) {
      this.college = college;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public void setDateDrafted(LocalDate dateDrafted) {
      this.dateDrafted = dateDrafted;
   }
}

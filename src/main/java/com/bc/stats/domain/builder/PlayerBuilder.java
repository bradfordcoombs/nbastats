/**
 * 
 */
package com.bc.stats.domain.builder;

import java.time.LocalDate;

import com.bc.stats.domain.CombineResults;
import com.bc.stats.domain.Player;

/**
 * @author Bradford
 */
public class PlayerBuilder {

   private Long nbaDotComId;

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

   private CombineResults combineAnthropometricResult;

   public PlayerBuilder espnDotComId(Long espnDotComId) {
      this.espnDotComId = espnDotComId;
      return this;
   }

   public Long getEspnDotComId() {
      return espnDotComId;
   }

   public PlayerBuilder nbaDotComId(Long nbaDotComId) {
      this.nbaDotComId = nbaDotComId;
      return this;
   }

   public Long getNbaDotComId() {
      return nbaDotComId;
   }

   public PlayerBuilder firstName(String firstName) {
      this.firstName = firstName;
      return this;
   }

   public String getFirstName() {
      return firstName;
   }

   public PlayerBuilder lastName(String lastName) {
      this.lastName = lastName;
      return this;
   }

   public String getLastName() {
      return lastName;
   }

   public PlayerBuilder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
      return this;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public PlayerBuilder heightInInches(Long heightInInches) {
      this.heightInInches = heightInInches;
      return this;
   }

   public Long getHeightInInches() {
      return heightInInches;
   }

   public PlayerBuilder weightInPounds(Long weightInPounds) {
      this.weightInPounds = weightInPounds;
      return this;
   }

   public Long getWeightInPounds() {
      return weightInPounds;
   }

   public PlayerBuilder college(String college) {
      this.college = college;
      return this;
   }

   public String getCollege() {
      return college;
   }

   public PlayerBuilder position(String position) {
      this.position = position;
      return this;
   }

   public String getPosition() {
      return position;
   }

   public PlayerBuilder dateDrafted(LocalDate dateDrafted) {
      this.dateDrafted = dateDrafted;
      return this;
   }

   public LocalDate getDateDrafted() {
      return dateDrafted;
   }

   public PlayerBuilder activeFrom(Long activeFrom) {
      this.activeFrom = activeFrom;
      return this;
   }

   public Long getActiveFrom() {
      return activeFrom;
   }

   public PlayerBuilder activeTo(Long activeTo) {
      this.activeTo = activeTo;
      return this;
   }

   public Long getActiveTo() {
      return activeTo;
   }

   public PlayerBuilder combineAnthropometricResult(CombineResults combineAnthropometricResult) {
      this.combineAnthropometricResult = combineAnthropometricResult;
      return this;
   }

   public CombineResults getCombineAnthropometricResult() {
      return combineAnthropometricResult;
   }

   public Player build() {
      return new Player(this);
   }
}

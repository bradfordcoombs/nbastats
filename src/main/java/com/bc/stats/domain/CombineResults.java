/**
 * 
 */
package com.bc.stats.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.bc.stats.domain.builder.CombineResultsBuilder;

/**
 * @author Bradford
 */
@Entity
public class CombineResults {

   @Id
   @GeneratedValue
   private Long id;

   private Double bodyFatPercentage;

   private Double handLengthInInches;

   private Double handWidthInInches;

   private Double heightWithoutShoesInInches;

   private Double heightWithShoesInInches;

   private Double weightInPounds;

   private Double wingspanInInches;

   private Double standingReachInInches;

   public CombineResults(CombineResultsBuilder builder) {
      this.handLengthInInches = builder.getHandLengthInInches();
      this.handWidthInInches = builder.getHandWidthInInches();
      this.heightWithoutShoesInInches = builder.getHeightWithoutShoesInInches();
      this.heightWithShoesInInches = builder.getHeightWithShoesInInches();
      this.weightInPounds = builder.getWeightInPounds();
      this.wingspanInInches = builder.getWingspanInInches();
      this.standingReachInInches = builder.getWingspanInInches();
   }

   protected CombineResults() {}

   public Double getBodyFatPercentage() {
      return bodyFatPercentage;
   }

   public void setBodyFatPercentage(Double bodyFatPercentage) {
      this.bodyFatPercentage = bodyFatPercentage;
   }

   public Double getHandLengthInInches() {
      return handLengthInInches;
   }

   public void setHandLengthInInches(Double handLengthInInches) {
      this.handLengthInInches = handLengthInInches;
   }

   public Double getHandWidthInInches() {
      return handWidthInInches;
   }

   public void setHandWidthInInches(Double handWidthInInches) {
      this.handWidthInInches = handWidthInInches;
   }

   public Double getHeightWithoutShoesInInches() {
      return heightWithoutShoesInInches;
   }

   public void setHeightWithoutShoesInInches(Double heightWithoutShoesInInches) {
      this.heightWithoutShoesInInches = heightWithoutShoesInInches;
   }

   public Double getHeightWithShoesInInches() {
      return heightWithShoesInInches;
   }

   public void setHeightWithShoesInInches(Double heightWithShoesInInches) {
      this.heightWithShoesInInches = heightWithShoesInInches;
   }

   public Double getWeightInPounds() {
      return weightInPounds;
   }

   public void setWeightInPounds(Double weightInPounds) {
      this.weightInPounds = weightInPounds;
   }

   public Double getWingspanInInches() {
      return wingspanInInches;
   }

   public void setWingspanInInches(Double wingspanInInches) {
      this.wingspanInInches = wingspanInInches;
   }

   public Double getStandingReachInInches() {
      return standingReachInInches;
   }

   public void setStandingReachInInches(Double standingReachInInches) {
      this.standingReachInInches = standingReachInInches;
   }
}

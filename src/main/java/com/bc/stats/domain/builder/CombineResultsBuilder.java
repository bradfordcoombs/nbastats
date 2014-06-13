/**
 * 
 */
package com.bc.stats.domain.builder;

import com.bc.stats.domain.CombineResults;

/**
 * @author Bradford
 */
public class CombineResultsBuilder {

   private Double bodyFatPercentage;

   private Double handLengthInInches;

   private Double handWidthInInches;

   private Double heightWithoutShoesInInches;

   private Double heightWithShoesInInches;

   private Double weightInPounds;

   private Double wingspanInInches;

   private Double standingReachInInches;

   public Double getBodyFatPercentage() {
      return bodyFatPercentage;
   }

   public CombineResultsBuilder bodyFatPercentage(Double bodyFatPercentage) {
      this.bodyFatPercentage = bodyFatPercentage;
      return this;
   }

   public Double getHandLengthInInches() {
      return handLengthInInches;
   }

   public CombineResultsBuilder handLengthInInches(Double handLengthInInches) {
      this.handLengthInInches = handLengthInInches;
      return this;
   }

   public Double getHandWidthInInches() {
      return handWidthInInches;
   }

   public CombineResultsBuilder handWidthInInches(Double handWidthInInches) {
      this.handWidthInInches = handWidthInInches;
      return this;
   }

   public Double getHeightWithoutShoesInInches() {
      return heightWithoutShoesInInches;
   }

   public CombineResultsBuilder heightWithoutShoesInInches(Double heightWithoutShoesInInches) {
      this.heightWithoutShoesInInches = heightWithoutShoesInInches;
      return this;
   }

   public Double getHeightWithShoesInInches() {
      return heightWithShoesInInches;
   }

   public CombineResultsBuilder heightWithShoesInInches(Double heightWithShoesInInches) {
      this.heightWithShoesInInches = heightWithShoesInInches;
      return this;
   }

   public Double getWeightInPounds() {
      return weightInPounds;
   }

   public CombineResultsBuilder weightInPounds(Double weightInPounds) {
      this.weightInPounds = weightInPounds;
      return this;
   }

   public Double getWingspanInInches() {
      return wingspanInInches;
   }

   public CombineResultsBuilder wingspanInInches(Double wingspanInInches) {
      this.wingspanInInches = wingspanInInches;
      return this;
   }

   public Double getStandingReachInInches() {
      return standingReachInInches;
   }

   public CombineResultsBuilder standingReachInInches(Double standingReachInInches) {
      this.standingReachInInches = standingReachInInches;
      return this;
   }

   public CombineResults build() {
      return new CombineResults(this);
   }
}

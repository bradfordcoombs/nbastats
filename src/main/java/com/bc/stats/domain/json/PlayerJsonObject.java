/**
 * 
 */
package com.bc.stats.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Bradford
 */
public class PlayerJsonObject {
   @JsonProperty(value = "0")
   private Long nbaDotComId;

   @JsonProperty(value = "1")
   private String name;

   @JsonProperty(value = "2")
   private Boolean isActive;

   @JsonProperty(value = "3")
   private Long fromYear;

   @JsonProperty(value = "4")
   private Long toYear;

   public Long getNbaDotComId() {
      return nbaDotComId;
   }

   public String getName() {
      return name;
   }

   public Boolean getIsActive() {
      return isActive;
   }

   public Long getFromYear() {
      return fromYear;
   }

   public Long getToYear() {
      return toYear;
   }

   public void setNbaDotComId(Long nbaDotComId) {
      this.nbaDotComId = nbaDotComId;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setIsActive(Boolean isActive) {
      this.isActive = isActive;
   }

   public void setFromYear(Long fromYear) {
      this.fromYear = fromYear;
   }

   public void setToYear(Long toYear) {
      this.toYear = toYear;
   }

}

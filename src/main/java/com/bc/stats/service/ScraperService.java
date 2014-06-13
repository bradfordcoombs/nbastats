/**
 * 
 */
package com.bc.stats.service;

/**
 * @author Bradford
 */
public interface ScraperService {
   public void scrapeNbaDotComPlayerProfiles();

   public void scrapeNbaDotComCombineResults();

   public void scrapeNbaDotComCombineResults(String year);
}

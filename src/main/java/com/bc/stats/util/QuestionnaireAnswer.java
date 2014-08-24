package com.bc.stats.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuestionnaireAnswer {

   Map<Integer, Odds> draftOdds = new HashMap<Integer, Odds>();

   public static void main(String[] args) {
      QuestionnaireAnswer answer = new QuestionnaireAnswer();
      answer.initOdds();
      SimulationResult simResults = answer.new SimulationResult();
      for(int i = 0; i < 10000; i++) {
         answer.runSimulation(5, simResults);
      }
      System.out.println("#1 picks: " + simResults.getFirstPicks());
      System.out.println("Average seasons to 1st pick: " + simResults.getSeasonsToFirstPick() / simResults.getFirstPicks());
      System.out.println("#5 picks: " + simResults.getFifthPicks());
      System.out.println("Average seasons to 5th pick: " + simResults.getSeasonsToFifthPick() / simResults.getFifthPicks());
   }

   private void initOdds() {
      draftOdds.put(1, new Odds(250, 0));
      draftOdds.put(2, new Odds(199, 123));
      draftOdds.put(3, new Odds(156, 265));
      draftOdds.put(4, new Odds(119, 351));
      draftOdds.put(5, new Odds(88, 261));
      draftOdds.put(6, new Odds(63, 0));
      draftOdds.put(7, new Odds(43, 0));
      draftOdds.put(8, new Odds(28, 0));
      draftOdds.put(9, new Odds(17, 0));
      draftOdds.put(10, new Odds(11, 0));
      draftOdds.put(11, new Odds(8, 0));
      draftOdds.put(12, new Odds(7, 0));
      draftOdds.put(13, new Odds(6, 0));
      draftOdds.put(14, new Odds(5, 0));
   }

   /**
    * Run simulation. Once 1st or 5th pick are obtained, end the simulation. Otherwise, simulate next season and new
    * draft position.
    * 
    * @param position
    * @return
    */
   private void runSimulation(int position, SimulationResult simResult) {
      int pick = position;
      long seasons = 0;
      do {
         // determine pick in draft
         pick = draft(position);

         // simulate next season
         seasons++;
         position = simulateSeason(position);
      }
      while(pick != 5 && pick != 1);

      if(pick == 1) {
         simResult.addToFirstPicks();
         simResult.addToSeasonsToFirstPick(seasons);
      }
      else {
         simResult.addToFifthPicks();
         simResult.addToSeasonsToFifthPick(seasons);
      }
   }

   /**
    * Simulate season and determine new draft position. .6 probability to improve 3 spots (lowering draft position by
    * 3), .4 probability to fall 2 spots in the standings (move up 2 spots in draft position)
    * 
    * @param current
    *           position
    * @return draft position for the new season
    */
   private int simulateSeason(int position) {
      // move down 3 spots or up 2 spots based on odds
      position += new Random().nextInt(9) > 3 ? 3 : -2;

      // ensure team stays within bounds of 30 team league
      if(position < 1) {
         position = 1;
      }
      if(position > 30) {
         position = 30;
      }
      return position;
   }

   private int draft(int position) {
      // begin with pre-lottery order
      int pick = position;

      // if pick is not in the lottery, actual pick and draft position are the same
      if(pick > 14) {
         return pick;
      }

      // perform lottery drawing out of 1000 possibilities
      int randomDraw = new Random().nextInt(999) + 1;

      // get team's odds for 1st and 5th pick based on initial position
      Odds odds = draftOdds.get(position);

      // If lottery drawing falls within 1st pick odds, assign 1st pick
      if(randomDraw <= odds.getNumberOnePick()) {
         pick = 1;
      }
      // If drawing falls outside of 1st pick odds but within 5th pick odds, assign 5th pick
      else if(randomDraw <= odds.getNumberFivePick() + odds.getNumberOnePick()) {
         pick = 5;
      }
      // If neither 1st nor 5th, location of the pick doesn't matter, just ensure it's not 5th or 1st
      else pick = 14;
      return pick;
   }

   class Odds {
      int numberOnePick;

      int numberFivePick;

      public Odds(int numberOnePick, int numberFivePick) {
         this.numberOnePick = numberOnePick;
         this.numberFivePick = numberFivePick;
      }

      public void setNumberOnePick(int numberOnePick) {
         this.numberOnePick = numberOnePick;
      }

      public int getNumberOnePick() {
         return numberOnePick;
      }

      public void setNumberFivePick(int numberFivePick) {
         this.numberFivePick = numberFivePick;
      }

      public int getNumberFivePick() {
         return numberFivePick;
      }
   }

   class SimulationResult {
      int firstPicks = 0;

      long seasonsToFirstPick = 0;

      int fifthPicks = 0;

      long seasonsToFifthPick = 0;

      public int getFirstPicks() {
         return firstPicks;
      }

      public long getSeasonsToFirstPick() {
         return seasonsToFirstPick;
      }

      public int getFifthPicks() {
         return fifthPicks;
      }

      public long getSeasonsToFifthPick() {
         return seasonsToFifthPick;
      }

      public void addToFirstPicks() {
         firstPicks++;
      }

      public void addToSeasonsToFirstPick(long seasonsToFirstPick) {
         this.seasonsToFirstPick += seasonsToFirstPick;
      }

      public void addToFifthPicks() {
         fifthPicks++;
      }

      public void addToSeasonsToFifthPick(long seasonsToFifthPick) {
         this.seasonsToFifthPick += seasonsToFifthPick;
      }
   }
}

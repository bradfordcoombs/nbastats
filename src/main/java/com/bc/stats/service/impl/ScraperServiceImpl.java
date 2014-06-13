/**
 * 
 */
package com.bc.stats.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bc.stats.domain.CombineResults;
import com.bc.stats.domain.Player;
import com.bc.stats.domain.builder.CombineResultsBuilder;
import com.bc.stats.domain.builder.PlayerBuilder;
import com.bc.stats.domain.json.PlayerJsonObject;
import com.bc.stats.domain.repository.CombineResultsRepository;
import com.bc.stats.domain.repository.PlayerRepository;
import com.bc.stats.service.ScraperService;
import com.bc.stats.util.StringUtil;

/**
 * @author Bradford
 */
@Service("scraperService")
public class ScraperServiceImpl implements ScraperService {

   @Value("${nba.com.players}")
   private String nbaDotComPlayers;

   @Value("${nba.com.combine.results}")
   private String nbaDotComCombineResults;

   @Value("${nba.com.combine.results.year")
   private String nbaDotComCombineResultsYear;

   @Resource
   private CombineResultsRepository combineResultsRepository;

   @Resource
   private PlayerRepository playerRepository;

   @Override
   public void scrapeNbaDotComPlayerProfiles() {
      List<PlayerJsonObject> players = new ArrayList<PlayerJsonObject>();
      String url = "http://stats.nba.com/stats/commonallplayers/?LeagueID=00&Season=2013-14&IsOnlyCurrentSeason=0";
      CloseableHttpClient client = HttpClientBuilder.create().build();
      HttpGet getRequest = new HttpGet(url);
      getRequest.setHeader("Content-Type", "application/json");

      try {
         HttpResponse response = client.execute(getRequest);
         if(response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error Code : " + response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
         }

         ObjectMapper mapper = new ObjectMapper();
         String line = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
         JsonNode node = mapper.readTree(line).get("resultSets").findValue("rowSet");
         node.getElements().forEachRemaining(playerNode -> players.add(processPlayerNode(playerNode)));
         client.close();
      }
      catch(IOException e) {
         e.printStackTrace();
      }

      players.forEach(player -> processPlayerLink(player));
   }

   private PlayerJsonObject processPlayerNode(JsonNode node) {
      PlayerJsonObject player = new PlayerJsonObject();
      player.setNbaDotComId(node.get(0).asLong());
      player.setName(node.get(1).asText());
      player.setIsActive(node.get(2).asBoolean());
      player.setFromYear(node.get(3).asLong());
      player.setToYear(node.get(4).asLong());
      return player;
   }

   private void processPlayerLink(Element link) {
      try {
         // String href = link.attr("href");
         String absURL = link.absUrl("href");
         String[] name = link.text().split(",");
         String firstName = StringUtil.getSafeArrayValue(name, 1);
         String lastName = StringUtil.getSafeArrayValue(name, 0);
         Document doc = Jsoup.connect(absURL).get();
         String birthDateString = doc.getElementsByAttributeValue("itemprop", "birthDate").text();
         LocalDate birthDate = LocalDate.parse(birthDateString, DateTimeFormatter.ofPattern("MMMM d, yyyy"));
         String college = doc.select(".nbaStats span:contains(From) ~ .nbaStatsText").text();
         Long height = calculateSizeInInches(doc.select(".nbaVitalsTitle:contains(Height) ~ span.nbaHeight").text());
         Long weight = Long.parseLong(doc.select(".nbaVitalsTitle:contains(Weight) ~ span.nbaHeight").text());
         String dateDraftedString = doc.select(".nbaStats span:contains(Draft) ~ .nbaStatsText").text();
         LocalDate dateDrafted = StringUtil.equalsValueOrNullIgnoreCase(dateDraftedString, "undrafted") ? null : LocalDate.parse(dateDraftedString);

         Player player =
               new PlayerBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).college(college).heightInInches(height).weightInPounds(weight).dateDrafted(dateDrafted).build();
         playerRepository.save(player);
      }
      catch(IOException e) {

      }
   }

   @Override
   public void scrapeNbaDotComCombineResults() {
      try {
         Elements elements = Jsoup.connect(nbaDotComCombineResults).get().select("#SeasonYear li a");
         elements.forEach(year -> scrapeNbaDotComCombineResults(year.val()));
      }
      catch(IOException e) {

      }
   };

   @Override
   public void scrapeNbaDotComCombineResults(String year) {
      try {
         // http://stats.nba.com/draftCombineAnthro.html?pageNo=1&rowsPerPage=100&SeasonYear=2014-15
         List<Player> combineResults = new ArrayList<Player>();
         String url = MessageFormat.format(nbaDotComCombineResultsYear, 100, year);
         Document doc = Jsoup.connect(url).get();
         doc.select("#draftCombineGridContainer tr:not(.not-records)").forEach(measurements -> combineResults.add(processIndividualCombineResult(measurements)));
         playerRepository.save(combineResults);
      }
      catch(IOException e) {

      }
   }

   private Player processIndividualCombineResult(Element element) {
      String name = element.select("td.col-PLAYER_NAME").attr("data-value");
      String[] nameArray = name.split(" ");
      String firstName = StringUtil.getSafeArrayValue(nameArray, 0);
      String lastName = StringUtil.getSafeArrayValue(nameArray, 1);
      List<Player> players = playerRepository.findByLastNameAndFirstNameAllIgnoreCase(lastName, firstName);
      Player player = players.isEmpty() ? new PlayerBuilder().firstName(firstName).lastName(lastName).build() : players.get(0);
      if(player.getCombineAnthropometricResult() == null) {
         player.setCombineAnthropometricResult(buildPlayer(element));
      }
      return player;
   }

   private CombineResults buildPlayer(Element element) {
      element.select("td.col-POSITION").val();
      Double bodyFat = parseDouble(element.select("td.col-BODY_FAT_PCT").val());
      Double handLength = parseDouble(element.select("td.col-HAND_LENGTH").val());
      Double handWidth = parseDouble(element.select("td.col-HAND_WIDTH").val());
      Double heightWithoutShoes = parseDouble(element.select("td.col-HEIGHT_WO_SHOES").val());
      Double heightWithShoes = parseDouble(element.select("td.col-HEIGHT_W_SHOES").val());
      Double standingReach = parseDouble(element.select("td.col-STANDING_REACH").val());
      Double weight = parseDouble(element.select("td.col-WEIGHT").val());
      Double wingspan = parseDouble(element.select("td.col-WINGSPAN").val());

      CombineResults measurements =
            new CombineResultsBuilder().bodyFatPercentage(bodyFat).handLengthInInches(handLength).handWidthInInches(handWidth).heightWithoutShoesInInches(heightWithoutShoes)
                  .heightWithShoesInInches(heightWithShoes).standingReachInInches(standingReach).weightInPounds(weight).wingspanInInches(wingspan).build();
      return measurements;
   }

   private Long calculateSizeInInches(String height) {
      String[] heightSplit = height.split("'");
      Long feet = StringUtil.safeConvertStringArrayToLong(heightSplit, 0);
      Long inches = StringUtil.safeConvertStringArrayToLong(heightSplit, 1);
      Long heightInInches = (12 * feet) + inches;
      return heightInInches;
   }

   private Double parseDouble(String stringValue) {
      Double value;
      try {
         value = Double.parseDouble(stringValue);
      }
      catch(NumberFormatException e) {
         value = 0.0;
      }
      return value;
   }

   private String getNbaDotComCombineResultsURL() {
      return MessageFormat.format(nbaDotComCombineResults, 1, 100, "2014-15");
   }
}

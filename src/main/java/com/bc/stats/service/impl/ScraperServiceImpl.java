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
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
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
         node.getElements().forEachRemaining(playerNode -> processPlayer(playerNode));
         client.close();
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }

   private Player processPlayer(JsonNode node) {
      Long nbaDotComId = node.get(0).asLong();
      String[] name = node.get(1).asText().split(",");
      String firstName = StringUtil.getSafeArrayValue(name, 1).trim();
      String lastName = StringUtil.getSafeArrayValue(name, 0).trim();
      boolean isActive = node.get(2).asBoolean();
      Long fromYear = node.get(3).asLong();
      Long toYear = node.get(4).asLong();

      LocalDate birthDate = null;
      String college = "";
      Long height = null;
      Long weight = null;
      String dateDraftedString = "";
      LocalDate dateDrafted = null;

      try {
         if(isActive) {
            String url = "http://www.nba.com/" + "playerfile/" + firstName.toLowerCase() + "_" + lastName.toLowerCase();
            Document doc = Jsoup.connect(url).get();

            String birthDateString = doc.getElementsByAttributeValue("itemprop", "birthDate").text();
            birthDate = LocalDate.parse(birthDateString, DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            college = doc.select(".nbaStats span:contains(From) ~ .nbaStatsText").text();
            height = calculateSizeInInches(doc.select(".nbaVitalsTitle:contains(Height) ~ span.nbaHeight").text());
            weight = calculateWeight(doc.select(".nbaVitalsTitle:contains(Weight) ~ span.nbaHeight").text());
            dateDraftedString = doc.select(".nbaStats span:contains(Draft) ~ .nbaStatsText").text();
            dateDrafted = StringUtil.equalsValueOrNullIgnoreCase(dateDraftedString, "undrafted") ? null : LocalDate.parse(dateDraftedString);
         }
         else {
            String url = "http://stats.nba.com/stats/commonplayerinfo/?PlayerID=76001&SeasonType=Regular+Season&LeagueID=00";
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("PlayerID", Long.toString(nbaDotComId)));
            params.add(new BasicNameValuePair("SeasonType", "Regular+Season"));
            params.add(new BasicNameValuePair("LeagueID", "00"));
            postRequest.setEntity(new UrlEncodedFormEntity(params));
            postRequest.setHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(postRequest);
            if(response.getStatusLine().getStatusCode() != 200) {
               throw new RuntimeException("Failed : HTTP Error Code : " + response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
            }

            ObjectMapper mapper = new ObjectMapper();
            String line = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
            JsonNode node = mapper.readTree(line).get("resultSets").findValue("rowSet");
            node.getElements().forEachRemaining(playerNode -> processPlayer(playerNode));
            client.close();
            Document doc = Jsoup.connect(url).get();
            String birthDateString = doc.getElementById("born").text();
            birthDate = LocalDate.parse(birthDateString, DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            college = doc.getElementById("college").text();
            height = calculateSizeInInches(doc.getElementById("height").text());
            weight = calculateWeight(doc.getElementById("weight").text());
            // dateDraftedString = doc.select(".nbaStats span:contains(Draft) ~ .nbaStatsText").text();
            // dateDrafted = StringUtil.equalsValueOrNullIgnoreCase(dateDraftedString, "undrafted") ? null :
            // LocalDate.parse(dateDraftedString);
         }

      }
      catch(IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Player player =
            new PlayerBuilder().nbaDotComId(nbaDotComId).firstName(firstName).lastName(lastName).activeFrom(fromYear).activeTo(toYear).firstName(firstName).lastName(lastName).birthDate(birthDate)
                  .college(college).heightInInches(height).weightInPounds(weight).dateDrafted(dateDrafted).build();

      return player;
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

   private Long calculateWeight(String weight) {
      String[] weightSplit = weight.split(" ");
      return Long.parseLong(weightSplit[0]);
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

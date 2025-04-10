import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class XMLHandler extends DefaultHandler {

    long limiter = 1;
    long MAX_ENTRIES = 100_000;
    private Voter voter;
    private WorkTime workTime;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private HashMap<Voter, Integer> voterCount;
    private HashMap<Integer, WorkTime> voteStationWorkTimes;

    public XMLHandler() {
        voterCount = new HashMap<>();
        voteStationWorkTimes = new HashMap<>();
    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {
        try {
            DBConnection.executeMultiInsert();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter") && voter == null) {
                Date birthDay = birthDayFormat.parse(attributes.getValue("birthDay"));
                voter = new Voter(attributes.getValue("name"), birthDay);
            } else if (qName.equals("visit") && voter != null) {
                int count = voterCount.getOrDefault(voter, 0);
                voterCount.put(voter, count + 1);
                DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                String birthDay = df.format(voter.getBirthDay());
                DBConnection.countVoter(voter.getName(), birthDay, count + 1);
                limiter++;
            }

            if (limiter > MAX_ENTRIES) {
                DBConnection.executeMultiInsert();
                DBConnection.cleanQuery();
                limiter = 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("voter")) {
            voter = null;
        }
        if (qName.equals("visit")) {
            workTime = null;
        }
    }

    public void printDuplicatedVoters() {
        for (Voter voter : voterCount.keySet()) {
            int count = voterCount.get(voter);
            if (count > 1) {
                System.out.println(voter.toString() + " - " + count);
            }
        }
    }

    public void printVotingStationWorkTimes() {
        for (Integer votingStation : voteStationWorkTimes.keySet()) {
            WorkTime workTime = voteStationWorkTimes.get(votingStation);
            System.out.println("\t" + votingStation + " - " + workTime);
        }
    }

}

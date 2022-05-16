import static java.time.LocalDate.parse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class dbload {

    public static void main(String[] args) {
        // calculating time
        long initialT = System.nanoTime();
        try {
            // pass the page size "4096, 2048" any page size in arguments
            int pageSize = getPageSize(args);
            String fileName= getFileName(args);
            //input the file name path here before running
            File csvFile = new File(fileName);
            DataOutputStream writer = new DataOutputStream(new FileOutputStream("heap." + pageSize));
            startReadingFromFile(writer, csvFile, pageSize);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long endingT = System.nanoTime();
        long totalTime = (endingT - initialT) / 1000000;
        System.out.println("File is created in: " + totalTime + "ms");

    }
    // uploading csv file
    private static String getFileName(String[] args) {
        int pos = args.length - 1;
        try {

            return args[pos];
        } catch (Exception e) {
            System.out.println("Please Upload full file Path..."+ args.length);
            System.exit(0);
        }
        return null;
    }
// getting page size
    private static int getPageSize(String[] args) {
        int pos = args.length - 2;
        try {
            int x = Integer.parseInt(args[pos]);
            return x;
        } catch (Exception e) {
            System.out.println("page size must be a multiple of 2 eg: 1024,2048,4096..."+ args.length);
            System.exit(0);
        }
        return 0;
    }
    // reading the 11 feilds in the csv file
    public static void startReadingFromFile(DataOutputStream os, File file, int pSize) throws IOException, ParseException {

        BufferedReader reader = new BufferedReader(new FileReader(file));

        ArtistPage ap = new ArtistPage();
        Artist art = null;

        int pageSize = pSize;
        int sizeCount = 0;
        int artCount = 0;
        int pageNum = 0;
        String Line;
        int ArtSize = 0;
        try
        {
            //Converting to bytes
            while ((Line = reader.readLine()) != null && Line.length() != 0) {

                // if the file contains "URl" skip it
                String[] content = Line.split(",");
                if (content[0].equals("URI") && (content.length != 0)) continue;
                //if personname contains Null skip it
                String PersonName = content[1].substring(1, content[1].length() - 1);
                if (PersonName.equals("NULL")) continue;
                // make the personName bytes = 12 only
                byte[] personNameBytes = PersonName.getBytes("UTF-8");
                personNameBytes = Arrays.copyOf(personNameBytes, 12);


                // changing birthdate formats to yyyyMMdd , if birthdate is null skip
                String birthDate = content[23].substring(1, content[23].length() - 1);

                birthDate = changFormate(birthDate);
                if (birthDate.equals("NULL") || birthDate.contains("non_vocal_") || birthDate.startsWith("-") ||birthDate.contains("|") || birthDate.contains("V") ) continue;

                Date date  = new SimpleDateFormat("yyyyMMdd").parse(birthDate);
                long dateInSec = date.getTime();

                byte[] birthDateBytes = ByteBuffer.allocate(8).putLong(dateInSec).array();
                birthDateBytes = Arrays.copyOf(birthDateBytes,8 );


                // if birthplace contains Null make it UKNOWN
                String birthPlace_label = content[25].substring(1, content[25].length() - 1);
                if (birthPlace_label.equals("NULL")) birthPlace_label = "UNKNOWN";

                // make the birthPlace_label bytes = 8 only

                byte[] birthPlace_labelBytes = birthPlace_label.getBytes(StandardCharsets.UTF_8);
                birthPlace_labelBytes = Arrays.copyOf(birthPlace_labelBytes,12 );


                // if deathdate contains Null make skip it
                String deathDate = content[40].substring(1, content[40].length() - 1);
                deathDate= changFormate(deathDate);
                if (deathDate.equals("NULL") || deathDate.contains("non_vocal_") || deathDate.startsWith("-") ||deathDate.contains("|") || deathDate.contains("V") ) continue;

                // make the deathdate bytes = 11 only
                Date date2 = new SimpleDateFormat("yyyyMMdd").parse(deathDate);
                long dateInSec2 = date2.getTime();
                byte[] deathDateBytes = ByteBuffer.allocate(8).putLong(dateInSec2).array();
                deathDateBytes = Arrays.copyOf(deathDateBytes,8 );



                // if field_label contains Null make it UKNOWN
                String field_label = content[50].substring(1, content[50].length() - 1);
                if (field_label.equals("NULL")) field_label = "UNKNOWN";

                // make the field_label bytes = 13 only
                byte[] field_labelBytes = field_label.getBytes(StandardCharsets.UTF_8);
                field_labelBytes = Arrays.copyOf(field_labelBytes,13 );


                // if genre_label contains Null make it UKNOWN
                String genre_label = content[52].substring(1, content[52].length() - 1);
                if (genre_label.equals("NULL")) genre_label = "UNKNOWN";

                // make the genre_label bytes = 13 only
                byte[] genre_labelBytes = genre_label.getBytes(StandardCharsets.UTF_8);
                genre_labelBytes = Arrays.copyOf(genre_labelBytes,13 );


                // if instrument_label contains Null make it UKNOWN
                String instrument_label = content[62].substring(1, content[62].length() - 1);
                if (instrument_label.equals("NULL")) instrument_label = "UNKNOWN";

                // make the instrument_label bytes = 16 only
                byte[] instrument_labelBytes = instrument_label.getBytes(StandardCharsets.UTF_8);
                instrument_labelBytes = Arrays.copyOf(instrument_labelBytes,16 );


                // if nationality_label contains Null make it UKNOWN
                String nationality_label = content[73].substring(1, content[73].length() - 1);
                if (nationality_label.equals("NULL")) nationality_label = "UNKNOWN";

                // make the instrument_label bytes = 17 only
                byte[] nationality_labelBytes = nationality_label.getBytes(StandardCharsets.UTF_8);
                nationality_labelBytes = Arrays.copyOf(nationality_labelBytes,17 );


                // if Thumbnail contains Null make it UKNOWN
                String Thumbnail = content[124].substring(1, content[124].length() - 1);
                if (Thumbnail.equals("NULL")) Thumbnail = "UNKNOWN";

                // make the Thumbnail bytes = 50 only
                byte[] ThumbnailBytes = Thumbnail.getBytes(StandardCharsets.UTF_8);
                ThumbnailBytes = Arrays.copyOf(ThumbnailBytes,50 );



                int wikiPageID = convertToInt(content[133]);

                // if wikiPageID contains Null make it UKNOWN
                String num = Integer.toBinaryString( wikiPageID);

                // make the wikiPageID bytes = 4 only
                byte[] wikiPageIDBytes = num.getBytes(StandardCharsets.UTF_8);
                wikiPageIDBytes = Arrays.copyOf(wikiPageIDBytes,4 );



                String description = content[137].substring(1, content[137].length() - 1);

                // if description contains Null make it UKNOWN
                byte[] descriptionBytes = description.getBytes(StandardCharsets.UTF_8);
                descriptionBytes = Arrays.copyOf(descriptionBytes,103 );



                os.write(personNameBytes);
                os.write(birthDateBytes);
                os.write(birthPlace_labelBytes);
                os.write(deathDateBytes);
                os.write(field_labelBytes);
                os.write(genre_labelBytes);
                os.write(instrument_labelBytes);
                os.write(nationality_labelBytes);
                os.write(ThumbnailBytes);
                os.write(wikiPageIDBytes);
                os.write(descriptionBytes);
                // art should hold a fixed length which is 256
                art = new Artist(PersonName, dateInSec, birthPlace_label, dateInSec2, field_label, genre_label, instrument_label, nationality_label, Thumbnail, wikiPageID, description);
                //Checking space in page, if a page is full then create a new page
                ArtSize = 256;

                if ((ArtSize + sizeCount) <= pageSize) {
                    sizeCount = sizeCount + ArtSize;
                    artCount++;
                    ap.ArtistRecords.add(new ArrayList<Artist>());
                    ap.ArtistRecords.get(pageNum).add(art);
                } else {
                    pageNum++;
                    ap.ArtistRecords.add(new ArrayList<Artist>());
                    ap.ArtistRecords.get(pageNum).add(art);
                    sizeCount = ArtSize;
                }
              //  System.out.println("person name "+PersonName +" Date "+ date + "date inst: " + dateInSec);

            }
            reader.close();
            System.out.println("Total number of pages used: " + (ap.ArtistRecords.size() * 265) / pageSize);
            System.out.println("Total number of records loaded: " + ap.ArtistRecords.size() );
        }catch(Exception e){
            System.out.println(e);

        }
    }

    private static String changFormate(String birthDate) {

        if (birthDate.equals("NULL") || birthDate.contains("non_vocal_") || birthDate.startsWith("-") ||birthDate.contains("|") || birthDate.contains("V") ) return "NULL";
        if ((birthDate.length() != 25) && (birthDate.startsWith("{"))){
            birthDate = birthDate.substring(1,11);
            DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd")
                    .toFormatter(Locale.ENGLISH);
            birthDate = parse(birthDate, parseFormatter).format(DateTimeFormatter.BASIC_ISO_DATE);
            return birthDate;
        }
        if (birthDate.length() == 25) {
            birthDate = birthDate.substring(0, 10);
            DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd")
                    .toFormatter(Locale.ENGLISH);
            birthDate = parse(birthDate, parseFormatter).format(DateTimeFormatter.BASIC_ISO_DATE);
            return birthDate;

        } else if (birthDate.length() == 10) {
            if (birthDate.matches("[0-9]{2}(\\/)[0-9]{2}(\\/)[0-9]{4}")) {
                DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd/MM/yyyy")
                        .toFormatter(Locale.ENGLISH);
                birthDate = parse(birthDate, parseFormatter).format(DateTimeFormatter.BASIC_ISO_DATE);
                return birthDate;
            } else if (birthDate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("yyyy-MM-dd")
                        .toFormatter(Locale.ENGLISH);
                birthDate = parse(birthDate, parseFormatter).format(DateTimeFormatter.BASIC_ISO_DATE);

                if (birthDate.matches("^[A-Z]+$")) return "NULL";
                if (birthDate.contains("V")) return "NULL";

            } else return  "NUll";

        } else return  "NULL";

        return birthDate;

    }

    private static int convertToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

}





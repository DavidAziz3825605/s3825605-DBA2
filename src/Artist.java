public class Artist {
    String PersonName;
    Long birthDate;
    String birthPlace_label;
    Long deathDate;
    String field_label;
    String genre_label;
    String instrument_label;
    String nationality_label;
    String Thumbnail;
    int wikiPageID;
    String description;

    public Artist(String var1, Long var2, String var3, Long var4, String var5, String var6, String var7, String var8, String var9, int var10, String var11) {
        this.PersonName = var1;
        this.birthDate = var2;
        this.birthPlace_label = var3;
        this.deathDate = var4;
        this.field_label = var5;
        this.genre_label = var6;
        this.instrument_label = var7;
        this.nationality_label = var8;
        this.Thumbnail = var9;
        this.wikiPageID = var10;
        this.description = var11;
    }
}

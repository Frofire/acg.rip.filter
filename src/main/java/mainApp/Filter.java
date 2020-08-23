package mainApp;

public class Filter {
    private String title;
    private String subGroup;
    private String definition;
    private String subLanguage;
    private String lastUpdateDate;

    public Filter(String line){
        String col[] = line.split(",", -1);
        this.title = col[0];
        this.subGroup = col[1];
        this.definition = col[2];
        this.subLanguage = col[3];
        this.lastUpdateDate = col[4];
    }

    boolean filter(Item item){
        if (!item.getTitle().contains(this.title)) return false;
        if (!(this.subGroup == null)){
            if (!item.getTitle().contains(this.subGroup)) return false;
        }
        if (!(this.definition == null)){
            if (!item.getTitle().toUpperCase().contains(this.definition.toUpperCase())) return false;
        }
        if (!(this.subLanguage == null)){
            switch (this.subLanguage){
                case "CHT":
                    if (!(item.getTitle().contains("CHT") || item.getTitle().contains("繁") || item.getTitle().contains("BIG5"))) return false;
                    break;
                case "CHS":
                    if (!(item.getTitle().contains("CHS") || item.getTitle().contains("简") || item.getTitle().contains("GB"))) return false;
                    break;
                case "ENG":
                    if (!(item.getTitle().contains("ENG") || item.getTitle().contains("英"))) return false;
                    break;
                default:
                    if (!(item.getTitle().contains(this.subLanguage))) return false;
                    break;
            }
        }
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getSubLanguage() {
        return subLanguage;
    }

    public void setSubLanguage(String subLanguage) {
        this.subLanguage = subLanguage;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}

public class Valute {
    private String name;
    private String value;
    private String charCode;

    public Valute(String name, String value, String charCode){
        this.name = name;
        this.value = value;
        this.charCode = charCode;
    }

    public String getName(){
        return name;
    }

    public String getValue(){
        return value;
    }

    public String getCharCode(){
        return charCode;
    }
}

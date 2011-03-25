/**
 *
 *
 **/
public class Fact {
  public String name;
  public enum Type{
    STRING("s"), TEXT("t"), INT("i"), LONG("l"), FLOAT("f"), DOUBLE("d"), NONE("");

    public String dynField;

    Type(String dynField) {
      this.dynField = dynField;
    }

    public static Type lookup(String str){
      if (str.equalsIgnoreCase("S")){
        return STRING;
      } else if (str.equalsIgnoreCase("T")){
        return TEXT;
      } else if (str.equalsIgnoreCase("I")){
        return INT;
      } else if (str.equalsIgnoreCase("L")){
        return LONG;
      } else if (str.equalsIgnoreCase("F")){
        return FLOAT;
      } else if (str.equalsIgnoreCase("D")){
        return DOUBLE;
      } else {
        //TODO: warning here
        return NONE;
      }
    }
  }
  public Type type = Type.STRING;
  public String value;
}

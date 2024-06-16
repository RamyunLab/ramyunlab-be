package ramyunlab_be.globalExceptionHandler;

import java.util.Arrays;
import java.util.List;

public class Util {

  public static Boolean isValidBoolean (String param){
    List<String> trueValues = Arrays.asList("true", "1", "t", "yes", "y");
    List<String> falseValues = Arrays.asList("false", "0", "f", "no", "n");

    if(trueValues.contains(param)){
      return true;
    }else if(falseValues.contains(param)) {
      return false;
    }else{
      return null;
    }
  }
}

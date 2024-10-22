package ramyunlab_be.controller.validation;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BooleanListEditor extends PropertyEditorSupport {
  @Override
  public void setAsText(String text) {

    String[] items = text.split(",");
    List<Boolean> booleanList = new ArrayList<>();
    for (String item : items) {
      if ("true".equalsIgnoreCase(item) || "false".equalsIgnoreCase(item)){
        booleanList.add(Boolean.parseBoolean(item));
      }else if("1".equals(item)) {
        booleanList.add(true);
      }else if("0".equals(item)){
        booleanList.add(false);
      }
    }

    if (booleanList.isEmpty()) {
      setValue(null);
    } else {
      setValue(booleanList);
    }
  }
}

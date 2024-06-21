package ramyunlab_be.controller.validation;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

public class IntegerListEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {

      String[] items = text.split(",");
      List<Integer> integerList = new ArrayList<>();
      for (String item : items) {
        try {
          integerList.add(Integer.parseInt(item));
        } catch (NumberFormatException ignored) {
        }
      }

      if (integerList.isEmpty()) {
        setValue(null);
      } else {
        setValue(integerList);
      }
    }
  }

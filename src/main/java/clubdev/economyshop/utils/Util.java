package clubdev.economyshop.utils;

import java.util.List;
import java.util.ArrayList;
import ru.contentforge.formconstructor.form.element.SelectableElement;

public class Util {
    
    public static List<SelectableElement> count() {
        List<SelectableElement> elements = new ArrayList<>();
        for (int i = 1; i < 65; i++) {
            elements.add(new SelectableElement(String.valueOf(i), i));
        }
        return elements;
    }
}

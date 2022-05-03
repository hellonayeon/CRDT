package me.hellonayeon.crdt.sequence;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SequenceFunction {
    public static void add(List<Element> elems, String value, Long id) {
        elems.add(new Element(value, id));
        elems.sort(Comparator.comparing(Element::getId));
    }

    public static void remove(List<Long> rmIds, Long id) {
        rmIds.add(id);
        Collections.sort(rmIds);
    }

    public static void merge(List<Element> elems1, List<Element> elems2) {
        for (Element elem : elems2) {
            if (!elems1.contains(elem)) {
                elems1.add(elem);
            }
        }
    }

    public static void mergeRmIds(List<Long> rmIds1, List<Long> rmIds2) {
        for (Long id : rmIds2) {
            if (!rmIds1.contains(id)) {
                rmIds1.add(id);
            }
        }
    }

    public static void display(String tag, String data) {
        System.out.printf("%-20s%s%n", tag, data);
    }

    public static String getSeq(List<String> valueSeq) {
        StringBuilder sb = new StringBuilder();

        for (String value : valueSeq) {
            sb.append(value);
        }

        return sb.toString();
    }
}

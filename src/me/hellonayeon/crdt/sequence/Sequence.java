package me.hellonayeon.crdt.sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sequence {

    List<Element> elems = new ArrayList<>();
    List<Long> rmIds = new ArrayList<>();
    List<Long> idSeq = new ArrayList<>();
    List<String> valueSeq = new ArrayList<>();

    Long id;

    private void updateSeq() {
        for (Element elem : elems) {
            Long id = elem.getId();
            if (!rmIds.contains(id) && !idSeq.contains(id)) {
                idSeq.add(id);
            }
        }

        for (Long id : rmIds) {
            if (idSeq.contains(id)) {
                valueSeq.remove(idSeq.indexOf(id));
                idSeq.remove(id);
            }
        }
        Collections.sort(idSeq);

        for (Long id : idSeq) {
            for (Element elem : elems) {
                if (elem.getId().equals(id)) {
                    if (valueSeq.size() > idSeq.indexOf(id)) {
                        if (!elem.getValue().equals( valueSeq.get(idSeq.indexOf(id)) )) {
                            valueSeq.add(idSeq.indexOf(id), elem.getValue());
                        }
                    }
                    else {
                        valueSeq.add(elem.getValue());
                    }
                }

            }
        }
    }

    private void add(String value, Long id) {
        SequenceFunction.add(elems, value, id);
        updateSeq();
    }

    private void remove(Long id) {
        SequenceFunction.remove(rmIds, id);
        updateSeq();
    }

    private boolean query(Long id) {
        for (Element elem : elems) {
            if (elem.getId().equals(id)) {
                return !rmIds.contains(id);
            }
        }
        return false;
    }

    private void merge(Sequence sequence) {
        SequenceFunction.merge(elems, sequence.elems);
        updateSeq();
    }

    private void display() {
        SequenceFunction.display("elem list", elems.toString());
        SequenceFunction.display("remove id list", rmIds.toString());
        SequenceFunction.display("id seq", idSeq.toString());
        SequenceFunction.display("elem seq", valueSeq.toString());
    }

    private String getSeq() {
        return SequenceFunction.getSeq(valueSeq);
    }
}
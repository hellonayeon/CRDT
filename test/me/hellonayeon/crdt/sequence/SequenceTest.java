package me.hellonayeon.crdt.sequence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceTest {

    Sequence seq1;
    Sequence seq2;

    long id1a;
    long id1b;
    long id2c;
    long id2b;
    long id2d;

    @BeforeEach
    void 초기설정() {
        seq1 = new Sequence(UUID.randomUUID().toString());
        seq2 = new Sequence(UUID.randomUUID().toString());

        id1a = Sequence.idCounter.getAndIncrement();
        seq1.add("a", id1a);
        id1b = Sequence.idCounter.getAndIncrement();
        seq1.add("b", id1b);

        id2c = Sequence.idCounter.getAndIncrement();
        seq2.add("c", id2c);
        id2b = Sequence.idCounter.getAndIncrement();
        seq2.add("b", id2b);
        id2d = Sequence.idCounter.getAndIncrement();
        seq2.add("d", id2d);
    }

    @Test
    void 추가_확인() {
        assertEquals(seq1.getSeq(), "ab");
        assertEquals(seq2.getSeq(), "cbd");
    }

    @Test
    void 추가_확인_질의() {
        seq1.display();
        seq2.display();

        assertTrue(seq1.query(id1a));
        assertTrue(seq1.query(id1b));
        assertFalse(seq1.query(id2c));
        assertFalse(seq1.query(id2b));
        assertFalse(seq1.query(id2d));

        assertFalse(seq2.query(id1a));
        assertFalse(seq2.query(id1b));
        assertTrue(seq2.query(id2c));
        assertTrue(seq2.query(id2b));
        assertTrue(seq2.query(id2d));
    }

    @Test
    void 병합_확인() {
        seq1.merge(seq2);
        assertEquals(seq1.getSeq(), "abcbd");

        seq2.merge(seq1);
        assertEquals(seq2.getSeq(), "abcbd");

        assertEquals(seq1.getSeq(), seq2.getSeq());
    }

    @Test
    void 병합_확인_질의() {
        seq2.merge(seq1);
        assertTrue(seq2.query(id1a));
        assertTrue(seq2.query(id1b));
        assertTrue(seq2.query(id2c));
        assertTrue(seq2.query(id2d));
        assertTrue(seq2.query(id2b));

        seq1.merge(seq2);
        assertTrue(seq1.query(id1a));
        assertTrue(seq1.query(id1b));
        assertTrue(seq1.query(id2c));
        assertTrue(seq1.query(id2d));
        assertTrue(seq1.query(id2b));
    }

    @Test
    void 삭제_확인() {
        seq1.remove(id1b);
        assertEquals(seq1.getSeq(), "a");

        seq2.remove(id2b);
        seq2.remove(id2c);
        assertEquals(seq2.getSeq(), "d");
    }

    @Test
    void 삭제_확인_질의() {
        seq1.remove(id1b);
        assertTrue(seq1.query(id1a));
        assertFalse(seq1.query(id1b));
        assertFalse(seq1.query(id2b));
        assertFalse(seq1.query(id2c));
        assertFalse(seq1.query(id2d));

        seq2.remove(id2b);
        seq2.remove(id2c);
        assertFalse(seq2.query(id1a));
        assertFalse(seq2.query(id1b));
        assertFalse(seq2.query(id2b));
        assertFalse(seq2.query(id2c));
        assertTrue(seq2.query(id2d));
    }

    @Test
    void 삭제_후_병합_확인() {
        seq1.remove(id1b);

        seq2.remove(id2c);

        seq1.merge(seq2);
        assertEquals(seq1.getSeq(), "abd");

        seq2.merge(seq1);
        assertEquals(seq2.getSeq(), "abd");

        assertEquals(seq1.getSeq(), seq2.getSeq());
    }

    @Test
    void 삭제_후_병합_확인_질의() {
        seq1.remove(id1b);
        seq2.remove(id2c);

        seq1.merge(seq2);
        seq2.merge(seq1);

        assertTrue(seq1.query(id1a));
        assertFalse(seq1.query(id1b));
        assertTrue(seq1.query(id2b));
        assertFalse(seq1.query(id2c));
        assertTrue(seq1.query(id2d));

        assertTrue(seq2.query(id1a));
        assertFalse(seq2.query(id1b));
        assertTrue(seq2.query(id2b));
        assertFalse(seq2.query(id2c));
        assertTrue(seq2.query(id2d));
    }
}

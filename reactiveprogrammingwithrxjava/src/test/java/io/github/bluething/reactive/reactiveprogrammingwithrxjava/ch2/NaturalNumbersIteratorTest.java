package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Iterator;

public class NaturalNumbersIteratorTest {

    @Test
    public void nextReturnPlusOneInteger() {
        Iterator<BigInteger> integerIterator = new NaturalNumbersIterator();
        Assertions.assertEquals(BigInteger.ONE, integerIterator.next());
    }

    class NaturalNumbersIterator implements Iterator<BigInteger> {
        private BigInteger current = BigInteger.ZERO;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public BigInteger next() {
            current = current.add(BigInteger.ONE);
            return current;
        }
    }
}

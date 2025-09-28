package ru.nsu.vmarkidonov;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void mainCancelAction() {
        System.setIn(new ByteArrayInputStream("x+(1+5)*y\n3\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainEval() {
        System.setIn(new ByteArrayInputStream("x+(1+5)*y\n0\nx=1;y=2\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainEvalEmpty() {
        System.setIn(new ByteArrayInputStream("1+(1+5)*2\n0\n-\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainNotAssignmentVariable() {
        System.setIn(new ByteArrayInputStream("x+(1+5)*y\n0\n-\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainDerivative() {
        System.setIn(new ByteArrayInputStream("x/y\n1\nx\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainDerivativeCancel() {
        System.setIn(new ByteArrayInputStream("x/y\n1\n-\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainDerivativeNotAssignmentVariable() {
        System.setIn(new ByteArrayInputStream("x/y\n1\nz\n-".getBytes()));
        Main.main(null);
    }

    @Test
    void mainSimplify() {
        System.setIn(new ByteArrayInputStream("1+2\n2\n-".getBytes()));
        Main.main(null);
    }
}
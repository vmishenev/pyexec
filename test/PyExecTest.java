import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.company.Main.runExec;
import static org.junit.jupiter.api.Assertions.*;

class PyExecTest {

    @Test
    void test1() {
        var out = new PrintStream(new ByteArrayOutputStream());
        assertDoesNotThrow(() -> runExec(out, new ByteArrayInputStream("python3".getBytes())));
    }
}
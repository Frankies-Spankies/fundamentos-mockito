package guru.springframework;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class InLineMockTest {

    @Test
    void testInLineMock() {
        Map mapMock = mock(Map.class);
        assertEquals(0, mapMock.size());
    }


}

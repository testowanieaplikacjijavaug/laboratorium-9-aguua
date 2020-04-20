package Example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CarTest {

    private Car myFerrari;

    @BeforeEach
    public void setUp(){
        myFerrari = mock(Car.class);
    }

    @Test
    public void test_instance_car(){
        assertTrue(myFerrari instanceof Car);
    }

    @Test
    public void test_default_behavior_needsFuel(){
        assertFalse(myFerrari.needsFuel(), "New test double should return False as boolean");
    }

    @Test
    public void test_default_behavior_temperature(){
        assertEquals(0.0, myFerrari.getEngineTemperature(), "New test double should return 0.0");
    }

    @Test
    public void test_stubbing_mock(){
        when(myFerrari.needsFuel()).thenReturn(true);
        assertTrue(myFerrari.needsFuel());
    }

    @Test
    public void test_exception(){
        when(myFerrari.needsFuel()).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> {
            myFerrari.needsFuel();
        });
    }

    @Test
    public void testVerification(){
        myFerrari.driveTo("Kartuzy");
        myFerrari.needsFuel();

        verify(myFerrari).driveTo("Kartuzy");
        verify(myFerrari).needsFuel();
        assertFalse(myFerrari.needsFuel()); // Asercja nie powinna znajdować się z verify, ten test ma głównie za zadanie sprawdzenie czy atrapa wykonała metodę. Zbędne jest tutaj użycie asercji.
    }

    //NOWE TESTY
    @Test
    public void test_stubbing_temperature(){
        when(myFerrari.getEngineTemperature()).thenReturn(40.0);
        assertEquals(myFerrari.getEngineTemperature(), 40.0);
    }
    @Test
    public void test_temperature_exception() {
        doThrow(new RuntimeException()).when(myFerrari).getEngineTemperature();
        assertThrows(RuntimeException.class, () -> {
            myFerrari.getEngineTemperature();
        });
    }
    @Test
    public void test_drive_to_null_exception() {
        doThrow(new IllegalArgumentException()).when(myFerrari).driveTo(null);
        assertThrows(IllegalArgumentException.class, () -> myFerrari.driveTo(null));
    }
    @Test
    public void test_not_used_function() {
        when(myFerrari.needsFuel()).thenReturn(true);
        verify(myFerrari,never()).needsFuel();
    }

    @Test
    void test_multiple_calls(){
        when(myFerrari.getEngineTemperature()).thenReturn(40.0);
        myFerrari.getEngineTemperature();
        myFerrari.getEngineTemperature();
        myFerrari.getEngineTemperature();
        verify(myFerrari, times(3)).getEngineTemperature();
    }

    @AfterEach
    public void tearDown(){
        myFerrari = null;
    }

}

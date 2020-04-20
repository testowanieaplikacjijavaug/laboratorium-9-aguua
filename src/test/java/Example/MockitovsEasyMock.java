package Example;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockitovsEasyMock {

    private Car carMockito = Mockito.mock(Car.class);
    private Car carEasy = EasyMock.mock(Car.class);

    // 1) tylko EasyMock wymaga odtworzenia nauczonych zachowań (replay)
    // we wszystkich poniższych przykładach

    // 2) implementacje mockowania 
    //  Mockito   when(...).thenReturn(..)
    //  EasyMock  expect(...).andReturn(...)

    // 3) Weryfikacja (verify):
    // Mockito - dla wybranych metod
    // Easymock - dla wszytskich nauczonych metod
    @Test
    public void verify_mockito() {
        Mockito.when(carMockito.needsFuel()).thenReturn(true);
        Mockito.when(carMockito.getEngineTemperature()).thenReturn(20.0);

        carMockito.needsFuel();
        carMockito.getEngineTemperature();

        Mockito.verify(carMockito).needsFuel();
        // można weryfikować metody osobno,
        // każde verify odnosi się do pojedynczej metody, nie muszą być weryfikowane wszystkie zamockowane metody
        // Mockito.verify(carMockito).getEngineTemperature();

    }

    @Test
    public void verify_easymock() {
        EasyMock.expect(carEasy.needsFuel()).andReturn(true);
        EasyMock.expect(carEasy.getEngineTemperature()).andReturn(20.0);

        EasyMock.replay(carEasy);

        carEasy.needsFuel();
        carEasy.getEngineTemperature();

        // wszystkie zamockowane metody muszą być wywoływane, by weryfikacja się powiodła
        EasyMock.verify(carEasy);
    }

    // 4) Wielokrotny stubbing
    // EasyMock - trzeba wykonać stub metody tyle razy ile jest potem odtwarzana
    // Mockito -  wystarczy jedno stubowanie na wiele wywołań
    @Test
    public void multiple_mockito() {
        Mockito.when(carMockito.needsFuel()).thenReturn(true);

        assertEquals(true, carMockito.needsFuel());
        assertEquals(true, carMockito.needsFuel());
    }

    @Test
    public void multiple_easy() {
        EasyMock.expect(carEasy.needsFuel()).andReturn(true);
        EasyMock.expect(carEasy.needsFuel()).andReturn(true);
        EasyMock.replay(carEasy);

        assertEquals(true, carEasy.needsFuel());
        assertEquals(true, carEasy.needsFuel());
        // kolejna assercja bez nowego stub'a metody spowoduje AssertionError
        // 'Unexpected method call Car.needsFuel()'

    }

    // 5) tylko Mockito - częściowa atrapa spy - obiekt spy wywołuje rzeczywistą metodę, jeśli nie został jej nauczony
    List<Integer> spyList = Mockito.spy(new ArrayList<Integer>());
    List<Integer> mockList = Mockito.mock(List.class);

    @Test
    public void test_mock_no_stub() {
        mockList.add(10);
        assertThat(mockList.get(0)).isNull();
    }

    @Test
    public void test_spy_no_stub() {
        spyList.add(10);
        assertThat(spyList.get(0)).isEqualTo(10);
    }

}

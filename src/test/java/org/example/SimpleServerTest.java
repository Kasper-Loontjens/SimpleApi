package org.example;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleServerTest{

    @Test
    public void urlSplitTestIfSecondParmIsFirstUrl(){
        SimpleServer simpleServer = new SimpleServer();
        String[] s = simpleServer.splitURL("/greetings/3");
        String[] s2 = simpleServer.splitURL("/farewells/2");
        assertEquals("greetings",s[1]);
        assertEquals("farewells",s2[1]);
        assertNotEquals("greetings",s[2]);
        assertNotEquals("farewells",s2[2]);

    }

    @Test
    public void convertToUrlIntTestAboveAndBelow1to5(){
        SimpleServer simpleServer = new SimpleServer();
        assertEquals(3, simpleServer.convertToUrlInt("3"));
        assertEquals(5, simpleServer.convertToUrlInt("8"));
        assertEquals(1,simpleServer.convertToUrlInt("-8"));
        assertEquals(5,simpleServer.convertToUrlInt("Not convertible to int"));

    }

}
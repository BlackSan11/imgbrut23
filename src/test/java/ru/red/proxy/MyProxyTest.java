package ru.red.proxy;

import org.jsoup.Connection.Response;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MyProxyTest {

    @Test
    public void checkMe() throws IOException {

        MyProxy myProxy = new MyProxy("192.111.11.11", 1111);//mock(MyProxy.class);
        Response testResponse = mock(Response.class);

        assertFalse(myProxy.checkMe(testResponse));

        when(testResponse.statusCode()).thenReturn(200);
        myProxy = new MyProxy("192.111.11.11", 1111);//mock(MyProxy.class);
        assertTrue(myProxy.checkMe(testResponse));
    }

}
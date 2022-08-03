/*
package com.sunshine.mockito.lesson1.lesson6;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 12:03
 **//*

public class ArguementMatcherTest {
    @Test
    public void basiccTest() {
        ArrayList list = mock(ArrayList.class);
        when(list.get(eq(0))).thenReturn(100);

        assertThat(list.get(0),equalTo(100));
        assertThat(list.get(10),equalTo(null));


    }
}
*/

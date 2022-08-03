/*
package com.sunshine.mockito.lesson1;

import com.sunshine.StaticClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 02:59
 **//*

@PrepareForTest({StaticClass.class})
@RunWith(PowerMockRunner.class)
public class AssertMatcherTest {

    @Test
    public  void test()
    {
        int i = 10;
        assertThat(i,equalTo(10));
        assertThat(i,not(equalTo(20)));
        assertThat(i,is(10));
        assertThat(i,is(not(20)));
        assertThat(i,is(not(20)));



    }

    @Test
    public void test_static(){
        PowerMockito.mockStatic(StaticClass.class);
        PowerMockito.when(StaticClass.get()).thenReturn("10");
        System.out.println(StaticClass.get());
    }


    @Test
    public  void test2()
    {
        double price = 23.56d;
        assertThat(price,either(equalTo(23.56)).or(equalTo(12.52)));
//        assertThat(price,both(equalTo(23.56)).and(equalTo(120)));

        assertThat(price,anyOf(is(12.12),is(12.2),is(12.2),not(is(14.2)),not(12.4)));

        assertThat(price,allOf(is(23.56),not(12.4)));

        assertThat(Stream.of(1,2,3).anyMatch(i -> i>2),equalTo(true));
        assertThat(Stream.of(1,2,3).allMatch(i -> i>0),equalTo(true));
    }

    @Test
    public  void test3()
    {
        double price = 23.54d;
        assertThat("the double value is equal.",price,either(equalTo(23.56)).or(equalTo(12.52)));
    }
}
*/

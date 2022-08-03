/*
package com.sunshine.mockito.lesson1.lesson7;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 12:28
 * https://www.bilibili.com/video/BV1jJ411A7Sv?p=7&spm_id_from=pageDriver&vd_source=58d200406795737d96538e0a90478be6
 **//*

public class ArgumentMatcherTest {

    @Test
    public void testComplex(){
        Foo foo = mock(Foo.class);
        when(foo.function(Mockito.isA(Parent.class))).thenReturn(100);
        int result = foo.function(new Child1());

        assertThat(result, equalTo(100));

        assertThat(foo.function(new Child2()),equalTo(100));
        reset(this);

    }

    @Test
    public void testComplex2(){
        Foo foo = mock(Foo.class);
        when(foo.function(Mockito.isA(Child1.class))).thenReturn(100);
        int result = foo.function(new Child1());

        assertThat(result, equalTo(100));

        assertThat(foo.function(new Child2()),equalTo(0));
    }

    @Test
    public void testComplex3(){
        Foo foo = mock(Foo.class);
        when(foo.function(Mockito.isA(Child1.class))).thenReturn(100);
        int result = foo.function(new Child1());

        assertThat(result, equalTo(100));

        assertThat(foo.function(new Child2()),equalTo(0));
    }
}
*/

/*
package com.sunshine.mockito.lesson1.lesson2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 11:04
 **//*

public class DeepMockTest {

    @Mock
    private Lesson03Service lesson03Service;

    @Mock
    private Lesson03 lesson03;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    */
/**
     * stubbling
     *//*

    @Test
    public void  testDeepMockFail(){
        Lesson03 lesson03 = lesson03Service.get();
        lesson03.foo();
    }

    */
/**
     * stubbling
     *//*

    @Test
    public void  testDeepMock(){
        when(lesson03Service.get()).thenReturn(lesson03);
        Lesson03 lesson03 = lesson03Service.get();
        lesson03.foo();
    }

    @Mock(answer= Answers.RETURNS_DEEP_STUBS)
    private Lesson03Service lesson03Service2;

    @Test
    public void  testDeepMock2(){
        Lesson03 lesson03 = lesson03Service2.get();
        lesson03.foo();
    }

}
*/

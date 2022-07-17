package com.sunshine.lesson3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 11:25
 **/
@RunWith(MockitoJUnitRunner.class)
public class StubbingTest {


    private List<String> list;

    @Before
    public void  init() {
        this.list = mock(ArrayList.class);
    }

    @Test
    public void howToUserStubbing(){
        when(list.get(0)).thenReturn("first");
        assertThat(list.get(0),equalTo("first"));

        when(list.get(anyInt())).thenThrow(new RuntimeException());

        try {
            list.get(0); // throw RuntimeException
            fail(); // throw AssertionError
        }catch (Exception e){
            assertThat(e,instanceOf(RuntimeException.class));
        }
        System.out.printf("1");
    }


    @Test
    public void howToStubbingVoidMethod(){
        doNothing().when(list).clear();
        list.clear();

        verify(list,times(1)).clear();
//        verify(list,times(2)).clear();  TooFewActualInvocations

        doThrow(RuntimeException.class).when(list).clear();

        try {
            list.clear();
        } catch (Exception e) {
            assertThat(e,instanceOf(RuntimeException.class));
        }
//        doAnswer(Answers.RETURNS_SMART_NULLS).when(list).clear();

        doReturn(true).when(list).add(anyString());

        assertThat(list.add(anyString()),equalTo(true));

        doCallRealMethod().when(list).add("1");
        assertThat("add 1",list.add("2"));
//        assertThat(list.add(anyString()),equalTo(true));

    }


    @After
    public void destory() {
        reset(this.list);
    }
}

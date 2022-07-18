package com.sunshine.mockito.lesson1.lesson3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger logger = LoggerFactory.getLogger(StubbingTest.class);
    private List<String> list;

    @Before
    public void  init() {
        this.list = mock(ArrayList.class);
    }

    @Test
    public void stubbingWithRealCall(){
        StubbingService stubbingService = mock(StubbingService.class); // cglib生成代理类
        logger.info("stubbingService = {}",stubbingService.getClass());

        System.out.println( stubbingService.getS("10"));
        // 调用mock方法
        doReturn("102").when(stubbingService).getS(anyString());
        assertThat(stubbingService.getS(anyString()),equalTo("102"));
        // 调用真实的方法
        when(stubbingService.getI(anyInt())).thenCallRealMethod();
        assertThat(stubbingService.getI(10),equalTo(100));


    }

    /**
     * 定义返回值逻辑
     */
    @Test
    public void stubbingWithAnswer(){
        when(list.get(anyInt())).thenAnswer((Answer<String>) invocation -> {
            Integer argument = invocation.getArgument(0, Integer.class);
            return String.valueOf(argument *10);
        });
        assertThat(list.get(0),equalTo("0"));
        assertThat(list.get(99),equalTo("990"));
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

        when(list.get(0)).thenReturn("1");
        doReturn("2").when(list.get(1));

        assertThat(list.get(0),equalTo("1"));
    }



    @Test
    public void iteratorSubbing(){
        when(list.size()).thenReturn(1, 2, 3, 4);
        assertThat(list.size(),equalTo(1));
        assertThat(list.size(),equalTo(2));
        assertThat(list.size(),equalTo(3));
        assertThat(list.size(),equalTo(4));
        assertThat(list.size(),equalTo(4));
        when(list.size()).thenReturn(10).thenReturn(11).thenReturn(12);
        assertThat(list.size(),equalTo(10));
        assertThat(list.size(),equalTo(11));
        assertThat(list.size(),equalTo(12));
        assertThat(list.size(),equalTo(12));
    }

    @After
    public void destory() {
        reset(this.list);
    }
}

/*
package com.sunshine.mockito.lesson1.lesson6;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 上午 11:03
 **//*

@RunWith(MockitoJUnitRunner.class)
public class SpyingTest {



    */
/**
     * 既可以调用真实方法，也可以调用mock方法
     *//*

    @Test
    public void testSpy(){
        List<String> realList = new ArrayList<>();
        List<String> list = spy(realList);

        list.add("Mockito");
        list.add("PowerMock");
        // 调用真实的方法
        assertThat(list.get(0), equalTo("Mockito"));
        assertThat(list.get(1), equalTo("PowerMock"));
        assertThat(list.isEmpty(),equalTo(false));
        // 调用mock方法
        when(list.isEmpty()).thenReturn(true);
        when(list.size()).thenReturn(0);
        assertThat(list.get(0),equalTo("Mockito"));
        assertThat(list.get(1),equalTo("PowerMock"));
        assertThat(list.isEmpty(),equalTo(true));
        assertThat(list.size(),equalTo(0));
    }
}
*/

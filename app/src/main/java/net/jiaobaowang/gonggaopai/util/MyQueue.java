package net.jiaobaowang.gonggaopai.util;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/11/23.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class MyQueue {
    private LinkedList list = new LinkedList();
    public void clear()//销毁队列
    {
        list.clear();
    }
    public boolean QueueEmpty()//判断队列是否为空
    {
        return list.isEmpty();
    }
    public void enQueue(Object o)//进队
    {
        list.addLast(o);
    }
    public Object deQueue()//出队
    {
        if(!list.isEmpty())
        {
            return list.removeFirst();
        }
        return "队列为空";
    }
    public int QueueLength()//获取队列长度
    {
        return list.size();
    }
    public Object QueuePeek()//查看队首元素
    {
        return list.getFirst();
    }

    public Object QueueLast()//查看队尾元素
    {
        return list.getLast();
    }

    public ListIterator QueueAll()//查看队列元素
    {
        return list.listIterator();
    }

    public LinkedList getList() {
        return list;
    }

    @Override
    public String toString() {
        return "MyQueue{" +
                "list=" + list.toString() +
                '}';
    }
}

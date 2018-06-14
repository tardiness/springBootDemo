package com.test.thin;

import com.test.thin.test.master_worker.MasterDemo;
import com.test.thin.test.master_worker.TaskDemo;
import com.test.thin.test.master_worker.WorkerDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * @author: shishaopeng
 * @project: springbootdemo
 * @data: 2018/6/13
 * @modifyDate: 13:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class MasterTest {

    @Test
    public void masterTest() {
        MasterDemo master = new MasterDemo(new WorkerDemo(), 10);
        for (int i = 0; i < 100; i++) {
            TaskDemo task = new TaskDemo();
            task.setId(i);
            task.setName("任务" + i);
            task.setPrice(new Random().nextInt(10000));
            master.submit(task);
        }

        master.execute();

        while (true) {
            if (master.isComplete()) {
                System.out.println("执行的结果为: " + master.getResult());
                break;
            }
        }
    }
}

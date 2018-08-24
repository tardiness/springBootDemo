package com.test.thread3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/23
 * @modifyDate: 16:30
 * @Description:
 */
public class UserValidator {

    private String name;

    public UserValidator(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        String user = "test";
        String password = "test";

        UserValidator LDAP = new UserValidator("LDAP");
        UserValidator DataBase = new UserValidator("DataBase");

        TaskValidator taskValidator = new TaskValidator(LDAP,user,password);
        TaskValidator taskValidator1 = new TaskValidator(DataBase,user,password);

        List<TaskValidator> taskValidators = new ArrayList<>();
        taskValidators.add(taskValidator);
        taskValidators.add(taskValidator1);

        ExecutorService executorService = Executors.newCachedThreadPool();
        String result;

        try {
            result = executorService.invokeAny(taskValidators);
            System.out.printf("Main: Result : %s \n",result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.printf("Main : End of the Execution \n");

    }

    public boolean vaildate(String name,String password) {
        Random random = new Random();
        try {
            long duration = (long) (Math.random()*10);
            System.out.printf("Validator %s: validate a user during %d second \n",this.name,duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return random.nextBoolean();
    }

    public String getName() {
        return name;
    }
}

class TaskValidator implements Callable<String> {

    private UserValidator validator;
    private String name;
    private String password;

    public TaskValidator(UserValidator validator, String name, String password) {
        this.validator = validator;
        this.name = name;
        this.password = password;
    }

    @Override
    public String call() throws Exception {
        if (!validator.vaildate(name,password)) {
            System.out.printf("User %s has not been fund \n",validator.getName());
            throw new Exception("error validate user");
        }
        System.out.printf("%s :the user has been fund\n",validator.getName());
        return validator.getName();
    }
}
package com.test.thread1;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/9
 * @modifyDate: 13:46
 * @Description:
 */
public class Account {

    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public synchronized void addAmount(double amount) {
        double tmp = balance;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tmp += amount;
        balance = tmp;
    }

    public synchronized void subtractAmount(double amount) {
        double tmp = balance;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tmp -= amount;
        balance = tmp;
    }



    public static void main(String[] args) {
        Account account = new Account();
        account.setBalance(1000);

        Company company = new Company(account);
        Thread thread = new Thread(company);

        Bank bank = new Bank(account);
        Thread thread1 = new Thread(bank);

        System.out.printf("Account : initial Balance : %f\n",account.getBalance());
        thread.start();
        thread1.start();

        try {
            thread.join();
            thread1.join();
            System.out.printf("Account:final balance : %f\n",account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}

class Bank implements Runnable {

    private Account account;

    public Bank(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i=0;i<100;i++) {
            account.subtractAmount(1000);
        }
    }
}

class Company implements Runnable {

    private Account account;

    public Company(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i=0;i<100;i++) {
            account.addAmount(1000);
        }
    }
}

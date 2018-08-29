package com.test.thread5;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/29
 * @modifyDate: 10:39
 * @Description:
 */
public class Account {

    private AtomicLong balance;

    public Account() {
        this.balance = new AtomicLong();
    }

    public long getBalance() {
        return balance.get();
    }

    public void setBalance(long balance) {
        this.balance.set(balance);
    }

    public void addAmount(long amount) {
        this.balance.getAndAdd(amount);
    }

    public void substractAmount( long amount) {
        this.balance.getAndAdd(-amount);
    }


    public static void main(String[] args) {
        Account account = new Account();
        account.setBalance(1000);

        Company company = new Company(account);
        Thread thread = new Thread(company);

        Bank bank = new Bank(account);
        Thread thread1 = new Thread(bank);

        System.out.printf("Account : initial balance %d \n",account.getBalance());

        thread.start();
        thread1.start();

        try {
            thread.join();
            thread1.join();
            System.out.printf("Account : Final Balance %d \n",account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        for (int i=0;i<10;i++) {
            account.addAmount(1000);
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
        for (int i=0;i<10;i++) {
            account.substractAmount(1000);
        }
    }
}

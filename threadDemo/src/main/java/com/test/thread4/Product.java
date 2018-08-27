package com.test.thread4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class Product {

    public static void main(String[] args) {
        ProductListGenerator generator = new ProductListGenerator();
        List<Product> products = generator.generate(1000);
        ProductTask task = new ProductTask(products,0,products.size(),0.20);
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(task);
        do {
            System.out.printf("Main : thread count %s \n",pool.getActiveThreadCount());
            System.out.printf("Main : thread steal %s \n",pool.getStealCount());
            System.out.printf("Main : parallelism %s \n",pool.getParallelism());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        pool.shutdown();

        if (task.isCompletedAbnormally()) {
            System.out.printf("Main : the process has completed normally \n");
        }

        for (int i=0;i<products.size();i++) {
            Product product = products.get(i);
            if (product.getPrice() != 12) {
                System.out.printf("Product : %s : %f \n",product.getName(),product.getPrice());
            }
        }
        System.out.println("Main:end of program");
    }


    private String name;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class ProductListGenerator {
    public List<Product> generate(int size) {
        List<Product> ret = new ArrayList<>();

        for (int i=0;i<size;i++) {
            Product product = new Product();
            product.setName("Product" + i);
            product.setPrice(10);
            ret.add(product);
        }
        return ret;
    }
}

class ProductTask extends RecursiveAction {

    private static final long serialVersionUID = -2382015651069121152L;

    private List<Product> products;
    private int first;
    private int last;

    private double increment;


    public ProductTask(List<Product> products, int first, int last, double increment) {
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    @Override
    protected void compute() {
        if (last - first < 10) {
            updatePrices();
        } else {
            int middle = (last+first)/2;
            System.out.printf("Task:pending tasks: %s \n",getQueuedTaskCount());
            ProductTask t1 = new ProductTask(products,first,middle+1,increment);
            ProductTask t2 = new ProductTask(products,middle+1,last,increment);
            invokeAll(t1,t2);
        }
    }

    private void updatePrices() {
        for (int i = first;i<last;i++) {
            Product product = products.get(i);
            product.setPrice(product.getPrice() * (1+increment));
        }
    }
}
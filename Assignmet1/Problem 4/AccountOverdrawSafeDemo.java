// BankSimulation.java

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
    private String name;
    private double balance;
    private final Lock lock = new ReentrantLock(); // Lock for thread safety

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public void withdraw(double amount, String threadName) {


        lock.lock(); // Acquire the lock before accessing shared resource (balance)


        try {
            System.out.println(threadName + " trying to withdraw $" + amount);



            if (balance >= amount) {
                System.out.println(threadName + ": Withdrawing $" + amount);


                try {
                    Thread.sleep(100); // Simulate some processing time.
                }
                catch (InterruptedException ex) { }

                balance -= amount;
                System.out.println(threadName + ": Withdrawal successful. New balance: $" + balance);

            } else {
                System.out.println(threadName + ": Insufficient funds!");
            }
        } finally {
            lock.unlock(); //  release the lock in the finally block


        }
    }



    public double getBalance() {
        return balance;
    }
}

// overdrawing without thread safety
class AccountOverdrawDemo {
    public static void main(String[] args) {
        Account account = new Account("Joint Account", 1000);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.withdraw(250, Thread.currentThread().getName());
            }
        });

        Thread thread2 = new Thread(() -> {

            for (int i = 0; i < 5; i++) {
                account.withdraw(200, Thread.currentThread().getName());
            }
        });


        thread1.start();
        thread2.start();


        try{
            thread1.join();
            thread2.join(); 
        }
        catch (InterruptedException ex) {}



        System.out.println("Final balance (unsafe): $" + account.getBalance());
    }
}



// thread-safe withdrawals
class AccountOverdrawSafeDemo {


    public static void main(String[] args) {
        Account account = new Account("Joint Account", 1000);


        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.withdraw(250, Thread.currentThread().getName());
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.withdraw(200, Thread.currentThread().getName());
            }
        });

        thread1.start();
        thread2.start();



        try{
            thread1.join();
            thread2.join();
        }
        catch (InterruptedException ex) { /* We can ignore this */}


        System.out.println("Final balance (safe): $" + account.getBalance());
    }
}
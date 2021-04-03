package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.HashMap;
import java.util.Map;

public class Finance_Portfolio extends AbstractBehavior<Finance_Portfolio.Command> {

    public interface Command{}

public enum RespondBalance implements Command{INSTANCE}
    // combine this purchase and sell
    public static final class SellStock implements Command
    {
        String companyId;
        double companyValue;
        public SellStock(String companyId,double companyValue)
        {
            this.companyId=companyId;
            this.companyValue=companyValue;
        }
    }


    public static final class PurchaseStock implements Command {
        double companyValue;
        String  companyId;
        public PurchaseStock(String companyId,double companyValue){
            this.companyId=companyId;
            this.companyValue=companyValue;
        }
    }
    public static final class Transactions implements Command
    {
        Double cash;
        String action;
        public Transactions(Double cash,String action)
        {
            this.cash=cash;
            this.action=action;
        }
    }


    static Behavior<Finance_Portfolio.Command> create(Double cash,ActorRef<Finance_Main.Command> replyToMain) {
        return Behaviors.setup(context -> new Finance_Portfolio(context,cash,replyToMain));

    }
    private final ActorRef<Finance_Main.Command> replyToMain;
    private final Map<String, Double> wallet = new HashMap<>();
    private double cash = -1;

    public Finance_Portfolio(ActorContext<Command> context,Double cash,ActorRef<Finance_Main.Command> replyToMain) {
        super(context);
        this.replyToMain=replyToMain;
        this.cash=cash;
        wallet.put("Cash",this.cash);
//        getContext().getLog().info("Portfolio Created");

    }

    @Override
    public Receive<Command> createReceive() {

        return newReceiveBuilder()
                .onMessage(Transactions.class,this::onTransactions)
                .onMessage(PurchaseStock.class,this::onPurchaseStock)
                .onMessage(SellStock.class,this::onSellStock)
                .onMessageEquals(RespondBalance.INSTANCE,this::onRespondBalance)
                .build();
    }

    private Behavior<Command> onRespondBalance() {

        wallet.forEach((K,V) -> System.out.println( K + ", Value : " + V));
        return this;
    }


    private Behavior<Command> onSellStock(SellStock sellStock) {
        if (wallet.containsKey(sellStock.companyId)) {
            wallet.put(sellStock.companyId, wallet.get(sellStock.companyId) - 1);
            wallet.put("Cash", wallet.get("Cash") + sellStock.companyValue);
            wallet.values().removeIf(f -> f == 0f);
        }else {
//            getContext().getLog().info("You missed an opportunity you have no stocks from this company {}",sellStcok.companyId);
        }

        return this;
    }

    private Behavior<Command> onPurchaseStock(PurchaseStock purchaseStock) {
        if(purchaseStock.companyValue>wallet.get("Cash")) {
//            getContext().getLog().info("you need to deposit more cash");
        }
        else{
            // purchase stock
            wallet.put("Cash",wallet.get("Cash")-purchaseStock.companyValue);
            if (wallet.containsKey(purchaseStock.companyId)) {
                wallet.put(purchaseStock.companyId, wallet.get(purchaseStock.companyId) + 1.0);
            } else {
                wallet.put(purchaseStock.companyId, 1.0);
            }
            }
        return this;
    }



    private Behavior<Command> onTransactions(Transactions transactions) {
        switch (transactions.action){
            case "deposit":
                this.cash+=transactions.cash;
                getContext().getLog().info("your new balance is: {}",this.cash);
                break;
            case "withdraw":
                this.cash-=transactions.cash;
                getContext().getLog().info("your new balance is: {}",this.cash);
                break;
            default:
                System.out.println("No correct action given please");
                System.out.println("current available actions: deposit or withdraw");
        }
        return this;
    }




}

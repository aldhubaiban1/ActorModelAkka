package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.HashMap;
import java.util.Map;

public class Finance_Traders extends AbstractBehavior<Finance_Traders.Command> {

    public interface Command{}
    static Behavior<Command> create(ActorRef<Finance_Main.Command> replyToMain) {
        return Behaviors.setup(context -> new Finance_Traders(context,replyToMain));
    }
    public static final class SubNewValue implements Command
    {
        String companyId;
        double companyValue;
        ActorRef<Finance_Portfolio.Command> replyToPortfolio;
        public SubNewValue(String companyId,double companyValue,ActorRef<Finance_Portfolio.Command> replyToPortfolio)
        {
            this.companyId=companyId;
            this.companyValue=companyValue;
            this.replyToPortfolio=replyToPortfolio;
        }
    }

    private final ActorRef<Finance_Main.Command> replyToMain;
    private final Map<String, Double> companiesStocks = new HashMap<>();
    public Finance_Traders(ActorContext<Command> context,ActorRef<Finance_Main.Command> replyToMain) {
        super(context);
        this.replyToMain=replyToMain;
//        getContext().getLog().info("Trader Created");
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                        .onMessage(SubNewValue.class,this::onSubNewValue)
                        .build();
    }

    private Behavior<Command> onSubNewValue(SubNewValue subNewValue) {

        if (companiesStocks.containsKey(subNewValue.companyId)) {
            double oldValue = companiesStocks.get(subNewValue.companyId);
            double newValue = subNewValue.companyValue;
            double differencePercntage = 100 * ((newValue - oldValue) / (double) oldValue);
            if (differencePercntage<= -2)
            {
                subNewValue.replyToPortfolio.tell( new Finance_Portfolio.PurchaseStock(subNewValue.companyId,subNewValue.companyValue));
            }
            else if (differencePercntage>5)
            {
                subNewValue.replyToPortfolio.tell(new Finance_Portfolio.SellStock(subNewValue.companyId,subNewValue.companyValue));
            }
        }
        companiesStocks.put(subNewValue.companyId,subNewValue.companyValue);



        return this;
    }
}

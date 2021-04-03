package com.example;

import akka.actor.Actor;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class Finance_QuoteGenerator extends AbstractBehavior<Finance_QuoteGenerator.Command> {
    public interface Command{}

    public static  class ChangeValues implements Command
    {
         ActorRef<Finance_Portfolio.Command> replyToPortfolio;
         ActorRef<Finance_Traders.Command> replyToTraders;

        public ChangeValues(ActorRef<Finance_Portfolio.Command> replyToPortfolio,ActorRef<Finance_Traders.Command> replyToTraders)
        {
           this.replyToPortfolio=replyToPortfolio;
           this.replyToTraders=replyToTraders;
        }
    }
    public static class GetLatestValues implements Command
    {
        String companyId;
        double companyValue;
        public GetLatestValues(String companyId, double companyValue)
        {
            this.companyId=companyId;
            this.companyValue=companyValue;
        }
    }



    static Behavior<Finance_QuoteGenerator.Command> create(ActorRef<Finance_Main.Command> replyToMain) {
        return Behaviors.setup(context -> new Finance_QuoteGenerator(context,replyToMain));
    }


    private double RandomNumber (double min,double max){
         DecimalFormat df2 = new DecimalFormat("#.##");

        double random_double = Math.random() * (max - min + 1) + min;
        // two digits convert to String then convert back to double
        random_double=Double.valueOf(df2.format(random_double));
        return  random_double;
    }

    private final Map<String, ActorRef<Finance_Company.Command>> actorsReferance = new HashMap<>();
    private final ActorRef<Finance_Main.Command> replyToMain;

    public Finance_QuoteGenerator(ActorContext<Command> context,ActorRef<Finance_Main.Command> replyToMain) {
        super(context);
        this.replyToMain=replyToMain;
        int numOfCompanies=5;
//        getContext().getLog().info("QuoteGenerator Created");
        for (int company=0;company<numOfCompanies;company++)
        {
             double value= RandomNumber(1,30);
            actorsReferance.put(Integer.toString(company + 1),context.spawn(Finance_Company.create(Integer.toString(company + 1), value,getContext().getSelf()), "Company" + (company + 1)));
        }


    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ChangeValues.class,this::onChangeValues)
                .onMessage(GetLatestValues.class,this::onGetLatestValues)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<Command> onGetLatestValues(GetLatestValues getLatestValues) {

        replyToMain.tell(new Finance_Main.PublishToTraders(getLatestValues.companyId,getLatestValues.companyValue));

        return this;
    }

    private Behavior<Command> onChangeValues(ChangeValues changeValues) {
        for (Map.Entry<String, ActorRef<Finance_Company.Command>> entry : actorsReferance.entrySet()) {
            entry.getValue().tell(new Finance_Company.ChangeValue(entry.getKey(), RandomNumber(1, 5)));
        }

        return this;
    }



    private Behavior<Finance_QuoteGenerator.Command> onPostStop() {
        getContext().getLog().info("Company actor stopped");
        return Behaviors.stopped();
    }





}





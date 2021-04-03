package com.example;



import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


import java.io.IOException;
import java.util.*;

class Main {
    public static void main(String[] args) {
        ActorSystem<Finance_Main.Command> financialSystem = ActorSystem.create(Finance_Main.create(), "Financial_System");
        financialSystem.tell(Finance_Main.ChangeValues.INSTANCE);

        while(true){
            try {
                    System.in.read();
                } catch (IOException ignored) {
                } finally {
                    financialSystem.tell(Finance_Main.RespondBalance.INSTANCE);
                }
        }
    }
}
public class Finance_Main extends AbstractBehavior<Finance_Main.Command> {

    public interface Command{}

    public enum Run_System implements Command
    {
        INSTANCE
    }
    public enum ChangeValues implements Command
    {
        INSTANCE
    }
    public static class PublishToTraders implements Command
    {
        String companyId;
        double companyValue;
        public PublishToTraders(String companyId,double companyValue)
        {
            this.companyId=companyId;
            this.companyValue=companyValue;
        }
    }
    public enum RespondBalance implements Command{INSTANCE}

      private final Map<String, ActorRef > actorsReferance = new HashMap<>();




    static Behavior<Command> create() {
        return Behaviors.setup(Finance_Main::new);
    }


    public Finance_Main(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("System Started");
        actorsReferance.put("Audit",context.spawn(Finance_audit.create(getContext().getSelf()), "Audit"));
        actorsReferance.put("Portfolio",context.spawn(Finance_Portfolio.create(10000.0,getContext().getSelf()), "Portfolio"));
        actorsReferance.put("QuoteGenerator",context.spawn(Finance_QuoteGenerator.create(getContext().getSelf()), "QuoteGenerator"));
        actorsReferance.put("Traders",context.spawn(Finance_Traders.create(getContext().getSelf()), "Traders"));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals(Run_System.INSTANCE, Behaviors::same)
                .onMessageEquals(ChangeValues.INSTANCE,this::onChangeValues)
                .onMessage(PublishToTraders.class,this::onPublishToTraders)
                .onMessageEquals(RespondBalance.INSTANCE,this::onRespondBalance)
                .onSignal(akka.actor.typed.PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<Command> onRespondBalance() {

        actorsReferance.get("Portfolio").tell(Finance_Portfolio.RespondBalance.INSTANCE);
        return this;
    }

    private Behavior<Command> onPublishToTraders(PublishToTraders publishToTraders) {
        actorsReferance.get("Traders").tell(new Finance_Traders.SubNewValue(publishToTraders.companyId, publishToTraders.companyValue,actorsReferance.get("Portfolio")));
//        actorsReferance.get("Portfolio").tell(new Finance_Portfolio.SubNewValue(publishToTraders.companyId, publishToTraders.companyValue));
        return this;
    }

    private Behavior<Command> onChangeValues() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {actorsReferance.get("QuoteGenerator").tell(new Finance_QuoteGenerator.ChangeValues(actorsReferance.get("Portfolio"),actorsReferance.get("Traders")) ); }}, 5000, 10000);
        return this;
    }
    private Behavior<Command> onPostStop() {
        getContext().getLog().info("System Stopped !!!");
        return this;
    }
}







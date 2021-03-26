package com.example;


// Import
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


class Main {
    public static void main(String[] args) {
        ActorSystem<Finance_Main.Command> financialSystem = ActorSystem.create(Finance_Main.create(), "Financial_System");
        financialSystem.tell(Finance_Main.Run_System.INSTANCE);

    }
}
public class Finance_Main extends AbstractBehavior<Finance_Main.Command> {

    public interface Command{}
    public enum Run_System implements Command
    {
        INSTANCE
    }


    static Behavior<Command> create() {
        return Behaviors.setup(Finance_Main::new);
    }

    public Finance_Main(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("System Started");
        context.spawn(Finance_User.create(), "User");
        context.spawn(Finance_Traders.create(), "Traders");
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals(Run_System.INSTANCE, Behaviors::same)
                .onSignal(akka.actor.typed.PostStop.class, signal -> onPostStop())
                .build();
    }


    // never used function Delete this later;
//    private Behavior<Command> onStartingSystem() {
//        return this;
//    }



    private Behavior<Command> onPostStop() {
        getContext().getLog().info("System Stopped !!!");
        return this;
    }
}





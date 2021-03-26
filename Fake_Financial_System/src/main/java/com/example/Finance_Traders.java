package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Finance_Traders extends AbstractBehavior<Finance_Traders.Command> {

    public interface Command{}
    static Behavior<Command> create() {
        return Behaviors.setup(Finance_Traders::new);
    }

    public Finance_Traders(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("Trader Created");
    }

    @Override
    public Receive<Command> createReceive() {
        return null;
    }
}

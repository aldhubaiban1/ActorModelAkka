package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Finance_Portfolio extends AbstractBehavior<Finance_Portfolio.Command> {

    public interface Command{}
    static Behavior<Finance_Portfolio.Command> create() {
        return Behaviors.setup(Finance_Portfolio::new);
    }
    public Finance_Portfolio(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("Portfolio Created");
    }

    @Override
    public Receive<Command> createReceive() {
        return null;
    }
}

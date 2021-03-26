package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Finance_User extends AbstractBehavior<Finance_User.Command> {

    public interface Command{}
    static Behavior<Command> create() {
        return Behaviors.setup(Finance_User::new);
    }

    public Finance_User(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("User Created");
        context.spawn(Finance_audit.create(), "Audit");
        context.spawn(Finance_Portfolio.create(), "Portfolio");
        context.spawn(Finance_QuoteGenerator.create(), "QuoteGenerator");


    }

    @Override
    public Receive<Command> createReceive() {
        return null;
    }
}

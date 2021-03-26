package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Finance_audit extends AbstractBehavior<Finance_audit.Command> {



    public interface Command{}
    static Behavior<Finance_audit.Command> create() {
        return Behaviors.setup(Finance_audit::new);
    }
    public Finance_audit(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("Audit Created");

    }

    @Override
    public Receive<Command> createReceive() {
        return null;
    }
}

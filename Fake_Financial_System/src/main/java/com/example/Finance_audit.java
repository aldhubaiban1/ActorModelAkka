package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.stream.alpakka.slick.javadsl.SlickSession;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.*;
import akka.stream.alpakka.slick.javadsl.*;
public class Finance_audit extends AbstractBehavior<Finance_audit.Command> {



    public interface Command{}

    public static final class GetRef implements Finance_audit.Command
    {
        public GetRef()
        {

        }
    }


    static Behavior<Finance_audit.Command> create(ActorRef<Finance_Main.Command> replyToMain) {

        return Behaviors.setup(context -> new Finance_audit(context,replyToMain));//replyToUser
    }
    private final ActorRef<Finance_Main.Command> replyToMain;
    public Finance_audit(ActorContext<Command> context,ActorRef<Finance_Main.Command> replyToMain) {
        super(context);
        this.replyToMain=replyToMain;
//        getContext().getLog().info("Audit Created");
        // for testing

    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .build();
    }
}

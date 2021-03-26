package com.example;

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




    static Behavior<Finance_QuoteGenerator.Command> create() {
        return Behaviors.setup(Finance_QuoteGenerator::new);
    }


    private double RandomNumber (double min,double max){
         DecimalFormat df2 = new DecimalFormat("#.##");

        double random_double = Math.random() * (max - min + 1) + min;
        // two digits convert to String then convert back to double
        random_double=Double.valueOf(df2.format(random_double));
        return  random_double;
    }

    public Finance_QuoteGenerator(ActorContext<Command> context) {
        super(context);
        getContext().getLog().info("QuoteGenerator Created");
        for (int company=0;company<5;company++)
        {
             double value= RandomNumber(1,30);
            context.spawn(Finance_Company.create(Integer.toString(company+1),value), "Company"+(company+1));
        }

    }

    @Override
    public Receive<Command> createReceive() {
// no messages yet
        return newReceiveBuilder()
                // Temporary delete this after testing
//                .onMessageEquals(Finance_QuoteGenerator.Run_System.INSTANCE, Behaviors::same)
                // Temporary delete this after testing
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }


    private Behavior<Finance_QuoteGenerator.Command> onPostStop() {
        getContext().getLog().info("Company actor stopped");
        return Behaviors.stopped();
    }





}




